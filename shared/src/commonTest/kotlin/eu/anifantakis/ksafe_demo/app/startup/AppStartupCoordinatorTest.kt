package eu.anifantakis.ksafe_demo.app.startup

import eu.anifantakis.ksafe_demo.core.presentation.string_resources.Language
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.LocalizationManager
import eu.anifantakis.ksafe_demo.features.preferences.domain.model.ThemeMode
import eu.anifantakis.ksafe_demo.features.preferences.domain.repository.ThemePreferenceRepository
import eu.anifantakis.ksafe_demo.core.domain.preferences.AppLanguageStore
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.koin.dsl.koinApplication
import kotlin.time.Duration.Companion.milliseconds

class AppStartupCoordinatorTest {
    @AfterTest
    fun resetLanguage() {
        LocalizationManager.setLanguage(Language.FALLBACK)
    }

    @Test
    fun successfulInitializationPublishesThemeAndLanguage() = runTest {
        val coordinator = AppStartupCoordinator(
            loader = AppStartupLoader { _ ->
                AppStartupPreferences(
                    themeMode = ThemeMode.NIGHT,
                    language = Language.EL,
                )
            },
        )

        coordinator.initialize()

        assertEquals(AppStartupState.Ready(ThemeMode.NIGHT), coordinator.state.value)
        assertEquals(Language.EL, LocalizationManager.current)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun successfulInitializationKeepsLoadingVisibleForTheMinimumDuration() = runTest {
        val coordinator = AppStartupCoordinator(
            loader = AppStartupLoader { _ ->
                AppStartupPreferences(
                    themeMode = ThemeMode.SYSTEM,
                    language = Language.EN,
                )
            },
        )

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
        val coordinator = AppStartupCoordinator(
            loader = AppStartupLoader { _ ->
                error("unavailable")
            },
        )

        coordinator.initialize()

        assertEquals(AppStartupState.Failed, coordinator.state.value)
    }

    @Test
    fun initializationCanBeRetriedAfterFailure() = runTest {
        var attempts = 0
        val coordinator = AppStartupCoordinator(
            loader = AppStartupLoader { _ ->
                attempts++
                if (attempts == 1) {
                    error("first attempt fails")
                }
                AppStartupPreferences(
                    themeMode = ThemeMode.DAY,
                    language = Language.FR,
                )
            },
        )

        coordinator.initialize()
        assertEquals(AppStartupState.Failed, coordinator.state.value)

        coordinator.initialize()

        assertEquals(AppStartupState.Ready(ThemeMode.DAY), coordinator.state.value)
        assertEquals(Language.FR, LocalizationManager.current)
        assertEquals(2, attempts)
    }

    @Test
    fun customPreloadLambdaRunsBeforeReady() = runTest {
        val application = koinApplication()
        val preloadScope = AppPreloadScope(koin = application.koin)
        var preloadCompleted = false
        val coordinator = AppStartupCoordinator(
            loader = AppStartupLoader { preload ->
                preloadScope.preload()
                assertTrue(preloadCompleted)
                AppStartupPreferences(
                    themeMode = ThemeMode.SYSTEM,
                    language = Language.EN,
                )
            },
        )

        try {
            coordinator.initialize(
                preload = {
                    preloadCompleted = true
                },
            )

            assertEquals(AppStartupState.Ready(ThemeMode.SYSTEM), coordinator.state.value)
        } finally {
            application.close()
        }
    }

    /**
     * The pipeline's whole point: KSafe readiness is the LOADER's first step, never the
     * lambda's responsibility — and the preference reads happen only after both.
     */
    @Test
    fun loaderGuaranteesBarrierBeforePreloadBeforePreferenceReads() = runTest {
        val application = koinApplication()
        val order = mutableListOf<String>()
        val loader = DefaultAppStartupLoader(
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

        try {
            loader.load(preload = { order += "preload" })

            assertEquals(
                listOf("ksafe-ready", "preload", "theme-read", "language-read"),
                order,
                "the barrier must precede the lambda, and both must precede the reads",
            )
        } finally {
            application.close()
        }
    }
}
