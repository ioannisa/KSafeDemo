package eu.anifantakis.ksafe_demo.app.startup

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.Language
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.LocalizationManager
import eu.anifantakis.ksafe_demo.features.preferences.domain.model.ThemeMode
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withTimeout

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

@Immutable
internal data class AppStartupPreferences(
    val themeMode: ThemeMode,
    val language: Language,
)

internal fun interface AppStartupLoader {
    suspend fun load(preload: AppPreload): AppStartupPreferences
}

/**
 * Owns the single startup gate shared by every platform.
 *
 * A failure renders a retry screen instead of leaving the application blank.
 */
@Stable
class AppStartupCoordinator internal constructor(
    private val loader: AppStartupLoader,
    private val timeoutMillis: Long = APP_STARTUP_TIMEOUT_MILLIS,
) {
    private val _state = MutableStateFlow<AppStartupState>(AppStartupState.Loading)
    val state: StateFlow<AppStartupState> = _state.asStateFlow()

    suspend fun initialize(
        minimumSplashDurationMillis: Long = 0L,
        preload: AppPreload = {},
    ) {
        require(minimumSplashDurationMillis >= 0L) {
            "minimumSplashDurationMillis must be non-negative"
        }
        if (_state.value is AppStartupState.Ready) return

        _state.value = AppStartupState.Loading
        try {
            val preferences = coroutineScope {
                val minimumSplashDuration = async {
                    delay(minimumSplashDurationMillis)
                }
                val loadedPreferences = withTimeout(timeoutMillis) {
                    loader.load(preload)
                }
                minimumSplashDuration.await()
                loadedPreferences
            }
            LocalizationManager.setLanguage(preferences.language)
            _state.value = AppStartupState.Ready(preferences.themeMode)
        } catch (error: TimeoutCancellationException) {
            failStartup(error)
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            failStartup(error)
        }
    }

    private fun failStartup(error: Throwable) {
        println("KSafeDemo startup failed: ${error.message ?: "unknown error"}")
        _state.value = AppStartupState.Failed
    }

    private companion object {
        const val APP_STARTUP_TIMEOUT_MILLIS = 15_000L
    }
}
