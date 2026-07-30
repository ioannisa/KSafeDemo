package eu.anifantakis.ksafe_demo.app.startup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import eu.anifantakis.ksafe_demo.core.presentation.design_system.KSafeDemoTheme
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppStartupScreen
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.LocalizationProvider
import eu.anifantakis.ksafe_demo.di.createKoinConfiguration
import eu.anifantakis.ksafe_demo.features.preferences.domain.model.ThemeMode
import eu.anifantakis.ksafe_demo.features.preferences.domain.repository.ThemePreferenceRepository
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.logger.Level

@OptIn(KoinExperimentalAPI::class)
@Composable
internal fun AppStartupHost(
    splashMode: AppSplashMode,
    minimumSplashDurationMillis: Long,
    onPlatformSplashReadyToDismiss: () -> Unit,
    preload: AppPreload,
    content: @Composable () -> Unit,
) {
    require(minimumSplashDurationMillis >= 0L) {
        "minimumSplashDurationMillis must be non-negative"
    }

    KoinApplication(
        configuration = createKoinConfiguration(),
        logLevel = Level.INFO,
    ) {
        AppStartupGate(
            splashMode = splashMode,
            minimumSplashDurationMillis = minimumSplashDurationMillis,
            onPlatformSplashReadyToDismiss = onPlatformSplashReadyToDismiss,
            preload = preload,
            content = content,
        )
    }
}

@Composable
private fun AppStartupGate(
    splashMode: AppSplashMode,
    minimumSplashDurationMillis: Long,
    onPlatformSplashReadyToDismiss: () -> Unit,
    preload: AppPreload,
    content: @Composable () -> Unit,
    coordinator: AppStartupCoordinator = koinInject(),
) {
    val startupState by coordinator.state.collectAsState()
    val currentPreload by rememberUpdatedState(preload)
    val currentOnPlatformSplashReadyToDismiss by rememberUpdatedState(
        onPlatformSplashReadyToDismiss,
    )
    var startupAttempt by remember(coordinator) { mutableIntStateOf(0) }
    var platformSplashDismissed by remember { mutableStateOf(false) }

    LaunchedEffect(coordinator, startupAttempt, minimumSplashDurationMillis) {
        coordinator.initialize(
            minimumSplashDurationMillis = minimumSplashDurationMillis,
            preload = currentPreload,
        )
    }
    LaunchedEffect(splashMode, startupState) {
        val shouldDismissPlatformSplash =
            when (splashMode) {
                AppSplashMode.CUSTOM -> true
                AppSplashMode.NATIVE_UNTIL_READY -> startupState !is AppStartupState.Loading
            }

        if (shouldDismissPlatformSplash && !platformSplashDismissed) {
            withFrameNanos { }
            currentOnPlatformSplashReadyToDismiss()
            platformSplashDismissed = true
        }
    }

    LocalizationProvider {
        when (val state = startupState) {
            AppStartupState.Loading ->
                KSafeDemoTheme(themeMode = ThemeMode.SYSTEM) {
                    AppStartupScreen(
                        failed = false,
                        onRetry = {},
                    )
                }

            AppStartupState.Failed ->
                KSafeDemoTheme(themeMode = ThemeMode.SYSTEM) {
                    AppStartupScreen(
                        failed = true,
                        onRetry = { startupAttempt++ },
                    )
                }

            is AppStartupState.Ready ->
                ThemeAwareAppContent(
                    initialThemeMode = state.themeMode,
                    content = content,
                )
        }
    }
}

/**
 * Uses the startup snapshot for the first ready frame, then observes preference changes.
 */
@Composable
private fun ThemeAwareAppContent(
    initialThemeMode: ThemeMode,
    content: @Composable () -> Unit,
    themePreferenceRepository: ThemePreferenceRepository = koinInject(),
) {
    val themeMode by themePreferenceRepository.themeMode.collectAsState(
        initial = initialThemeMode,
    )

    KSafeDemoTheme(
        themeMode = themeMode,
        content = content,
    )
}
