package eu.anifantakis.ksafe_demo

import androidx.compose.runtime.Composable
import eu.anifantakis.ksafe_demo.app.AppContent
import eu.anifantakis.ksafe_demo.app.startup.AppPreload
import eu.anifantakis.ksafe_demo.app.startup.AppSplashMode
import eu.anifantakis.ksafe_demo.app.startup.AppStartupHost

private val appPreload: AppPreload = {
    // Application work required before the first usable frame goes here. KSafe readiness is
    // already guaranteed by the pipeline before this lambda runs — no barrier call needed.
    // get<RemoteConfigRepository>().preload()
}

/**
 * Application entry point.
 *
 * Add first-frame work directly to [preload]. KSafe readiness is guaranteed BEFORE the lambda
 * runs (the loader owns that barrier), and persisted theme/language are resolved after it.
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
