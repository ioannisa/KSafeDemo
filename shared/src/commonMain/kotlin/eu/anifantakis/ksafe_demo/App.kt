package eu.anifantakis.ksafe_demo

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
import eu.anifantakis.ksafe_demo.app.navigation.AppRoute
import eu.anifantakis.ksafe_demo.app.navigation.NavigationRoot
import eu.anifantakis.ksafe_demo.app.startup.AppStartupCoordinator
import eu.anifantakis.ksafe_demo.app.startup.AppStartupState
import eu.anifantakis.ksafe_demo.app.startup.AppSplashMode
import eu.anifantakis.ksafe_demo.core.presentation.design_system.KSafeDemoTheme
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppStartupScreen
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.LocalizationProvider
import eu.anifantakis.ksafe_demo.di.createKoinConfiguration
import eu.anifantakis.ksafe_demo.features.preferences.domain.model.ThemeMode
import eu.anifantakis.ksafe_demo.features.preferences.domain.repository.ThemePreferenceRepository
import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeEncryptedProtection
import eu.anifantakis.lib.ksafe.KSafeWriteMode
import eu.anifantakis.lib.ksafe.compose.rememberKSafeState
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.logger.Level

/**
 * Starts the application after its KSafe stores and persisted UI preferences are ready.
 *
 * @param splashMode chooses whether loading is owned by the platform launch surface or the
 * shared Compose splash.
 * @param minimumSplashDurationMillis optional minimum visibility time. It runs concurrently
 * with real loading and defaults to no artificial delay.
 * @param onPlatformSplashReadyToDismiss platform bridge invoked at the selected hand-off point.
 */
@OptIn(KoinExperimentalAPI::class)
@Composable
fun App(
    splashMode: AppSplashMode = AppSplashMode.CUSTOM,
    minimumSplashDurationMillis: Long = 0L,
    onPlatformSplashReadyToDismiss: () -> Unit = {},
) {
    require(minimumSplashDurationMillis >= 0L) {
        "minimumSplashDurationMillis must be non-negative"
    }

    KoinApplication(
        configuration = createKoinConfiguration(),
        logLevel = Level.INFO,
    ) {
        AppStartupHost(
            splashMode = splashMode,
            minimumSplashDurationMillis = minimumSplashDurationMillis,
            onPlatformSplashReadyToDismiss = onPlatformSplashReadyToDismiss,
        )
    }
}

@Composable
private fun AppStartupHost(
    splashMode: AppSplashMode,
    minimumSplashDurationMillis: Long,
    onPlatformSplashReadyToDismiss: () -> Unit,
    coordinator: AppStartupCoordinator = koinInject(),
) {
    val startupState by coordinator.state.collectAsState()
    val currentOnPlatformSplashReadyToDismiss by rememberUpdatedState(
        onPlatformSplashReadyToDismiss,
    )
    var startupAttempt by remember(coordinator) { mutableIntStateOf(0) }
    var platformSplashDismissed by remember { mutableStateOf(false) }

    LaunchedEffect(coordinator, startupAttempt, minimumSplashDurationMillis) {
        coordinator.initialize(minimumSplashDurationMillis)
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

            is AppStartupState.Ready -> {
                ThemeAwareAppContent(initialThemeMode = state.themeMode)
            }
        }
    }
}

/**
 * Uses the startup snapshot for the first ready frame, then keeps the root theme synchronized
 * with preference changes made while the application is running.
 */
@Composable
private fun ThemeAwareAppContent(
    initialThemeMode: ThemeMode,
    themePreferenceRepository: ThemePreferenceRepository = koinInject(),
) {
    val themeMode by themePreferenceRepository.themeMode.collectAsState(
        initial = initialThemeMode,
    )

    KSafeDemoTheme(themeMode = themeMode) {
        AppContent()
    }
}

@Composable
fun AppContent() {
    val ksafe: KSafe = koinInject()
    var currentRoute: AppRoute by ksafe.rememberKSafeState(AppRoute.Counters)
    val selectedBottomRoute =
        currentRoute.takeIf { it in AppRoute.bottomNavigationEntries } ?: AppRoute.Counters

    LaunchedEffect(ksafe) {
        runKSafeProtectionDiagnostics(ksafe)
    }
    LaunchedEffect(currentRoute, selectedBottomRoute) {
        if (currentRoute != selectedBottomRoute) {
            currentRoute = selectedBottomRoute
        }
    }

    NavigationRoot(
        selectedRoute = selectedBottomRoute,
        onRouteSelected = { currentRoute = it },
    )
}

private suspend fun runKSafeProtectionDiagnostics(ksafe: KSafe) {
    val info = ksafe.protectionInfo
    println(
        "KSafe protection: " +
            "intended=${info.intendedLevel} " +
            "effective=${info.effectiveLevel} " +
            "custody=\"${info.custody}\" " +
            "notes=${info.notes}"
    )

    logKSafeEntryProtection(
        ksafe = ksafe,
        key = "demo_plain_theme",
        value = "dark",
        requested = "Plain",
        mode = KSafeWriteMode.Plain,
    )
    logKSafeEntryProtection(
        ksafe = ksafe,
        key = "demo_encrypted_token",
        value = "abc-123",
        requested = "Encrypted(DEFAULT)",
        mode = KSafeWriteMode.Encrypted(),
    )
    logKSafeEntryProtection(
        ksafe = ksafe,
        key = "demo_hw_isolated_secret",
        value = "supersecret",
        requested = "Encrypted(HARDWARE_ISOLATED)",
        mode = KSafeWriteMode.Encrypted(KSafeEncryptedProtection.HARDWARE_ISOLATED),
    )
}

@Suppress("DEPRECATION")
private suspend fun logKSafeEntryProtection(
    ksafe: KSafe,
    key: String,
    value: String,
    requested: String,
    mode: KSafeWriteMode,
) {
    ksafe.put(key, value, mode)
    val keyInfo = ksafe.getKeyInfo(key)
    println(
        "KSafe per-key: key=$key " +
            "requested=$requested " +
            "achieved=${keyInfo?.level} " +
            "legacy.storage=${keyInfo?.storage}"
    )
    ksafe.delete(key)
}
