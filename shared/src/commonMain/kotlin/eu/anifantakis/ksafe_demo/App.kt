package eu.anifantakis.ksafe_demo

import androidx.compose.runtime.Composable
import eu.anifantakis.ksafe_demo.app.AppContent
import eu.anifantakis.ksafe_demo.app.startup.AppPreload
import eu.anifantakis.ksafe_demo.app.startup.AppSplashMode
import eu.anifantakis.ksafe_demo.app.startup.AppStartupHost
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

private val appPreload: AppPreload = {
    // Application work required before the first usable frame goes here. KSafe readiness is
    // already guaranteed by the pipeline before this lambda runs — no barrier call needed.
    // Illustrative (RemoteConfigRepository is not a real demo class — substitute your own):
    // get<RemoteConfigRepository>().preload()
}

/**
 * Application entry point.
 *
 * Add first-frame work directly to [preload]. KSafe readiness is guaranteed BEFORE the lambda
 * runs (the coordinator's pipeline owns that barrier), and persisted theme/language are
 * resolved after it.
 */
@Composable
fun App(
    splashMode: AppSplashMode = AppSplashMode.CUSTOM,
    minimumSplashDuration: Duration = 0.milliseconds,
    onPlatformSplashReadyToDismiss: () -> Unit = {},
    preload: AppPreload = appPreload,
) {
    AppStartupHost(
        splashMode = splashMode,
        minimumSplashDuration = minimumSplashDuration,
        onPlatformSplashReadyToDismiss = onPlatformSplashReadyToDismiss,
        preload = preload,
    ) {
        AppContent()
    }
}
