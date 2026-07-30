package eu.anifantakis.ksafe_demo.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import eu.anifantakis.ksafe_demo.app.navigation.AppRoute
import eu.anifantakis.ksafe_demo.app.navigation.NavigationRoot
import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeEncryptedProtection
import eu.anifantakis.lib.ksafe.KSafeWriteMode
import eu.anifantakis.lib.ksafe.compose.rememberKSafeState
import org.koin.compose.koinInject

@Composable
internal fun AppContent() {
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
