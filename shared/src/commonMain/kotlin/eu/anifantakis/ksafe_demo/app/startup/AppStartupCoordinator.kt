package eu.anifantakis.ksafe_demo.app.startup

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import eu.anifantakis.ksafe_demo.core.domain.preferences.AppLanguageStore
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.Language
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.LocalizationManager
import eu.anifantakis.ksafe_demo.features.preferences.domain.model.ThemeMode
import eu.anifantakis.ksafe_demo.features.preferences.domain.repository.ThemePreferenceRepository
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeout
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@Immutable
sealed interface AppStartupState {
    data object Loading : AppStartupState

    data class Ready(val themeMode: ThemeMode) : AppStartupState

    data object Failed : AppStartupState
}

/**
 * Selects which surface owns the startup loading period.
 *
 * [NATIVE_UNTIL_READY] keeps the platform launch surface visible until startup reaches a
 * terminal state. [CUSTOM] hands off after Compose's first frame so [AppStartupState.Loading]
 * is rendered by the shared UI.
 */
@Immutable
enum class AppSplashMode {
    NATIVE_UNTIL_READY,
    CUSTOM,
}

/**
 * Owns the single startup gate shared by every platform — and IS the startup pipeline.
 *
 * What the splash waits for is exactly [runStartupPipeline], in guaranteed order:
 *
 * 1. [awaitStoresReady] — the KSafe cache-hydration barrier.
 * 2. The [AppPreload] lambda the app passed to `App(preload = …)` — the ONE seam an app
 *    customizes; it reaches this class through [initialize].
 * 3. The theme/language reads whose values [AppStartupState.Ready] publishes.
 *
 * A failure anywhere renders a retry screen instead of leaving the application blank.
 */
@Stable
class AppStartupCoordinator internal constructor(
    private val themePreferenceRepository: ThemePreferenceRepository,
    private val appLanguageStore: AppLanguageStore,
    private val preloadScope: AppPreloadScope,
    private val awaitStoresReady: suspend () -> Unit,
    private val timeout: Duration = 15.seconds,
) {
    private val _state = MutableStateFlow<AppStartupState>(AppStartupState.Loading)
    val state: StateFlow<AppStartupState> = _state.asStateFlow()

    suspend fun initialize(
        minimumSplashDuration: Duration = 0.milliseconds,
        preload: AppPreload = {},
    ) {
        require(minimumSplashDuration.inWholeMilliseconds >= 0L) {
            "minimumSplashDuration must be non-negative"
        }
        if (_state.value is AppStartupState.Ready) return

        _state.value = AppStartupState.Loading
        try {
            val (themeMode, language) = coroutineScope {
                // Named apart from the parameter on purpose: `val minimumSplashDuration =
                // async { delay(minimumSplashDuration) }` reads as self-reference even
                // though Kotlin resolves it to the parameter.
                val splashFloor = async {
                    delay(minimumSplashDuration)
                }
                val loaded = withTimeout(timeout) {
                    runStartupPipeline(preload)
                }
                splashFloor.await()
                loaded
            }
            LocalizationManager.setLanguage(language)
            _state.value = AppStartupState.Ready(themeMode)
        } catch (error: TimeoutCancellationException) {
            failStartup(error)
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            failStartup(error)
        }
    }

    private suspend fun runStartupPipeline(preload: AppPreload): Pair<ThemeMode, Language> {
        awaitStoresReady()
        preloadScope.preload()
        return themePreferenceRepository.themeMode.first() to
            LocalizationManager.resolveStartup(appLanguageStore.languageCode)
    }

    private fun failStartup(error: Throwable) {
        println("KSafeDemo startup failed: ${error.message ?: "unknown error"}")
        _state.value = AppStartupState.Failed
    }
}
