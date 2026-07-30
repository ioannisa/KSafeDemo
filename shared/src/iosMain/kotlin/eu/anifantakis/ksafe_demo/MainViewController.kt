package eu.anifantakis.ksafe_demo

import androidx.compose.ui.window.ComposeUIViewController
import eu.anifantakis.ksafe_demo.app.startup.AppSplashMode

fun MainViewController() = ComposeUIViewController { App() }

fun ConfiguredMainViewController(
    splashMode: AppSplashMode,
    minimumSplashDurationMillis: Long,
    onPlatformSplashReadyToDismiss: () -> Unit,
) = ComposeUIViewController {
    App(
        splashMode = splashMode,
        minimumSplashDurationMillis = minimumSplashDurationMillis,
        onPlatformSplashReadyToDismiss = onPlatformSplashReadyToDismiss,
    )
}
