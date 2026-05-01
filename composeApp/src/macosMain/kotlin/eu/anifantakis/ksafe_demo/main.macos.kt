package eu.anifantakis.ksafe_demo

import androidx.compose.ui.window.Window
import kotlinx.cinterop.ExperimentalForeignApi
import platform.AppKit.NSApp
import platform.AppKit.NSApplication
import platform.AppKit.NSApplicationActivationPolicy

/**
 * Native macOS entry point.
 *
 * Compose Multiplatform on native macOS (1.10.x) does not yet ship a
 * [`application { ... }` block][androidx.compose.ui.window] like Compose
 * Desktop on JVM does. We bootstrap NSApplication manually, hand control to
 * Compose's `Window(...)` factory to mount the Skia layer in an `NSWindow`,
 * then enter the AppKit run loop.
 *
 * The function name (`main`) and the Kotlin-Native `entryPoint` configured
 * in the build script must match.
 */
@OptIn(ExperimentalForeignApi::class)
fun main() {
    // Initialise the singleton NSApplication and become a regular GUI app.
    // Without `Regular` the binary runs as a faceless background process —
    // no Dock icon, no menu bar focus, the window never becomes key.
    NSApplication.sharedApplication
    val app = checkNotNull(NSApp) { "NSApplication.sharedApplication did not produce an NSApp" }
    // Kotlin/Native 2.3.x exposes NS_ENUM constants as members of the enum
    // typealias rather than top-level vals. Regular = 0 (faceless = -1, accessory = 1).
    app.setActivationPolicy(NSApplicationActivationPolicy.NSApplicationActivationPolicyRegular)

    // Create the Compose-backed NSWindow with our root composable.
    Window(title = "KSafe Demo") {
        App()
    }

    app.activateIgnoringOtherApps(true)
    app.run()
}
