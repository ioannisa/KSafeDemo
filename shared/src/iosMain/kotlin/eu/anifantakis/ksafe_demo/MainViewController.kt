package eu.anifantakis.ksafe_demo

import androidx.compose.ui.window.ComposeUIViewController
import eu.anifantakis.ksafe_demo.app.startup.AppSplashMode
import kotlin.time.Duration.Companion.milliseconds

fun MainViewController() = ComposeUIViewController { App() }

// Swift cannot construct kotlin.time.Duration, so the boundary stays Long millis and the
// conversion happens here, on the Kotlin side.
fun ConfiguredMainViewController(
    splashMode: AppSplashMode,
    minimumSplashDurationMillis: Long,
    onPlatformSplashReadyToDismiss: () -> Unit,
) = ComposeUIViewController {
    App(
        splashMode = splashMode,
        minimumSplashDuration = minimumSplashDurationMillis.milliseconds,
        onPlatformSplashReadyToDismiss = onPlatformSplashReadyToDismiss,
    )
}
