package eu.anifantakis.ksafe_demo.app.startup

import eu.anifantakis.ksafe_demo.core.domain.preferences.AppLanguageStore
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.Language
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.LocalizationManager
import eu.anifantakis.ksafe_demo.features.preferences.domain.model.ThemeMode
import eu.anifantakis.ksafe_demo.features.preferences.domain.repository.ThemePreferenceRepository
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.koin.dsl.koinApplication
import kotlin.time.Duration.Companion.milliseconds

class AppStartupCoordinatorTest {
    private val application = koinApplication()

    @AfterTest
    fun tearDown() {
        application.close()
        LocalizationManager.setLanguage(Language.FALLBACK)
    }

    private fun coordinatorOf(
        themeFlow: Flow<ThemeMode> = flowOf(ThemeMode.SYSTEM),
        initialLanguageCode: String = "en",
        awaitStoresReady: suspend () -> Unit = {},
        timeoutMillis: Long = 15_000L,
    ) = AppStartupCoordinator(
        themePreferenceRepository = object : ThemePreferenceRepository {
            override val themeMode = themeFlow

            override fun setThemeMode(themeMode: ThemeMode) = Unit
        },
        appLanguageStore = object : AppLanguageStore {
            override var languageCode: String = initialLanguageCode
        },
        preloadScope = AppPreloadScope(koin = application.koin),
        awaitStoresReady = awaitStoresReady,
        timeoutMillis = timeoutMillis,
    )

    @Test
    fun successfulInitializationPublishesThemeAndLanguage() = runTest {
        val coordinator = coordinatorOf(
            themeFlow = flowOf(ThemeMode.NIGHT),
            initialLanguageCode = "el",
        )

        coordinator.initialize()

        assertEquals(AppStartupState.Ready(ThemeMode.NIGHT), coordinator.state.value)
        assertEquals(Language.EL, LocalizationManager.current)
    }

    @Test
    fun aBarrierThatNeverAnswersBecomesFailedAtTheTimeout() = runTest {
        val coordinator = coordinatorOf(
            awaitStoresReady = { CompletableDeferred<Unit>().await() },
            timeoutMillis = 5_000L,
        )

        coordinator.initialize()

        assertEquals(AppStartupState.Failed, coordinator.state.value)
        @OptIn(ExperimentalCoroutinesApi::class)
        assertEquals(5_000L, currentTime, "Failed must arrive AT the timeout, not after")
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun successfulInitializationKeepsLoadingVisibleForTheMinimumDuration() = runTest {
        val coordinator = coordinatorOf()

        val initialization = launch {
            coordinator.initialize(minimumSplashDurationMillis = 800L)
        }
        runCurrent()
        assertEquals(AppStartupState.Loading, coordinator.state.value)

        advanceTimeBy(799.milliseconds)
        runCurrent()
        assertEquals(AppStartupState.Loading, coordinator.state.value)

        advanceTimeBy(1.milliseconds)
        runCurrent()
        initialization.join()
        assertEquals(AppStartupState.Ready(ThemeMode.SYSTEM), coordinator.state.value)
    }

    @Test
    fun failedInitializationPublishesRetryableFailure() = runTest {
        val coordinator = coordinatorOf(
            awaitStoresReady = { error("unavailable") },
        )

        coordinator.initialize()

        assertEquals(AppStartupState.Failed, coordinator.state.value)
    }

    @Test
    fun initializationCanBeRetriedAfterFailure() = runTest {
        var attempts = 0
        val coordinator = coordinatorOf(
            themeFlow = flowOf(ThemeMode.DAY),
            initialLanguageCode = "he",
            awaitStoresReady = {
                attempts++
                if (attempts == 1) {
                    error("first attempt fails")
                }
            },
        )

        coordinator.initialize()
        assertEquals(AppStartupState.Failed, coordinator.state.value)
        assertEquals(Language.FALLBACK, LocalizationManager.current, "Failed must not apply a language")

        coordinator.initialize()

        assertEquals(AppStartupState.Ready(ThemeMode.DAY), coordinator.state.value)
        assertEquals(Language.HE, LocalizationManager.current, "a successful retry applies the loaded language")

        coordinator.initialize()   // already Ready → the pipeline must NOT run again
        assertEquals(2, attempts)
    }

    @Test
    fun customPreloadLambdaRunsBeforeReady() = runTest {
        var preloadCompleted = false
        val coordinator = coordinatorOf()

        coordinator.initialize(
            preload = {
                preloadCompleted = true
            },
        )

        assertTrue(preloadCompleted, "the pipeline must run the lambda before publishing Ready")
        assertEquals(AppStartupState.Ready(ThemeMode.SYSTEM), coordinator.state.value)
    }

    /**
     * The pipeline's whole point: KSafe readiness is the coordinator's FIRST step, never the
     * lambda's responsibility — and the preference reads happen only after both.
     */
    @Test
    fun pipelineGuaranteesBarrierBeforePreloadBeforePreferenceReads() = runTest {
        val order = mutableListOf<String>()
        val coordinator = AppStartupCoordinator(
            themePreferenceRepository = object : ThemePreferenceRepository {
                override val themeMode = kotlinx.coroutines.flow.flow {
                    order += "theme-read"
                    emit(ThemeMode.DAY)
                }

                override fun setThemeMode(themeMode: ThemeMode) = Unit
            },
            appLanguageStore = object : AppLanguageStore {
                override var languageCode: String = "en"
                    get() {
                        order += "language-read"
                        return field
                    }
            },
            preloadScope = AppPreloadScope(koin = application.koin),
            awaitStoresReady = { order += "ksafe-ready" },
        )

        coordinator.initialize(preload = { order += "preload" })

        assertEquals(
            listOf("ksafe-ready", "preload", "theme-read", "language-read"),
            order,
            "the barrier must precede the lambda, and both must precede the reads",
        )
        assertEquals(AppStartupState.Ready(ThemeMode.DAY), coordinator.state.value)
    }
}
