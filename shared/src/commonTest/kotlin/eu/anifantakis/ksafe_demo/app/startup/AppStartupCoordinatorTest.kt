package eu.anifantakis.ksafe_demo.app.startup

import eu.anifantakis.ksafe_demo.core.presentation.string_resources.Language
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.LocalizationManager
import eu.anifantakis.ksafe_demo.features.preferences.domain.model.ThemeMode
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

        advanceTimeBy(799L)
        runCurrent()
        assertEquals(AppStartupState.Loading, coordinator.state.value)

        advanceTimeBy(1L)
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
        val preloadScope = AppPreloadScope(
            koin = application.koin,
            kSafeReady = {},
        )
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
}
