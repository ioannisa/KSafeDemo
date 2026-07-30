package eu.anifantakis.ksafe_demo

import androidx.compose.runtime.Composable
import eu.anifantakis.ksafe_demo.app.AppContent
import eu.anifantakis.ksafe_demo.app.startup.AppPreload
import eu.anifantakis.ksafe_demo.app.startup.AppSplashMode
import eu.anifantakis.ksafe_demo.app.startup.AppStartupHost

private val appPreload: AppPreload = {
    awaitKSafeReady()

    // Call application work required before the first usable frame here.
    // get<RemoteConfigRepository>().preload()
}

/**
 * Application entry point.
 *
 * Add first-frame work directly to [preload]. The lambda owns KSafe readiness and any additional
 * application preload, and runs before persisted theme and language are resolved.
 */
@Composable
fun App(
    splashMode: AppSplashMode = AppSplashMode.CUSTOM,
    minimumSplashDurationMillis: Long = 0L,
    onPlatformSplashReadyToDismiss: () -> Unit = {},
    preload: AppPreload = appPreload,
) {
    AppStartupHost(
        splashMode = splashMode,
        minimumSplashDurationMillis = minimumSplashDurationMillis,
        onPlatformSplashReadyToDismiss = onPlatformSplashReadyToDismiss,
        preload = preload,
    ) {
        AppContent()
    }
}
