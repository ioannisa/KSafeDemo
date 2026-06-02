package eu.anifantakis.ksafe_demo

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

// KSafe-on-Desktop demo notes:
//
// 1. The release distributable (`./gradlew :composeApp:runReleaseDistributable`,
//    `packageReleaseDistributable`, …) bundles a jlink-trimmed JRE. KSafe
//    needs the `jdk.unsupported` module in that runtime (JNA → sun.misc.Unsafe).
//    The relevant `modules("jdk.unsupported")` line is in this module's
//    build.gradle.kts under `compose.desktop.application.nativeDistributions`.
//
// 2. To see whether the OS vault is actually active at runtime, open the
//    Security screen (`screens/security/SecurityScreen.kt`) — it renders
//    `KSafe.protectionInfo`. Green card + custody = "macOS Keychain …" /
//    "Windows DPAPI …" / "Linux Secret Service …" means the OS vault is
//    healthy. Red card + note `jvm_os_vault_unavailable` means KSafe
//    degraded to the software vault (e.g. because `jdk.unsupported` is
//    missing from the bundled runtime).
//
// 3. Background and KSafe-side docs:
//    docs/JVM_PROTECTION.md#compose-desktop-release-distributables-jdkunsupported
//    https://github.com/ioannisa/KSafe/issues/32
fun main() {

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "KSafeDemo",
        ) {
            App()
        }
    }
}