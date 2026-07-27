package eu.anifantakis.ksafe_demo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import eu.anifantakis.ksafe_demo.app.navigation.AppRoute
import eu.anifantakis.ksafe_demo.app.navigation.NavigationRoot
import eu.anifantakis.ksafe_demo.core.presentation.design_system.KSafeDemoTheme
import eu.anifantakis.ksafe_demo.di.createKoinConfiguration
import eu.anifantakis.ksafe_demo.di.customJsonKSafe
import eu.anifantakis.ksafe_demo.di.preferencesKSafe
import eu.anifantakis.ksafe_demo.features.preferences.domain.model.ThemeMode
import eu.anifantakis.ksafe_demo.features.preferences.domain.repository.ThemePreferenceRepository
import eu.anifantakis.ksafe_demo.util.awaitKSafeCachesReady
import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeEncryptedProtection
import eu.anifantakis.lib.ksafe.KSafeWriteMode
import eu.anifantakis.lib.ksafe.compose.rememberKSafeState
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.logger.Level

@OptIn(KoinExperimentalAPI::class)
@Composable
fun App() {
    KoinApplication(
        configuration = createKoinConfiguration(),
        logLevel = Level.INFO,
    ) {
        KSafeReadyAppContent()
    }
}

/**
 * Waits for every app-lifetime KSafe store before the first synchronous read. The operation is
 * required on JS/WasmJS and a no-op on Android, Apple, and JVM.
 */
@Composable
private fun KSafeReadyAppContent() {
    val defaultStore: KSafe = koinInject()
    val customJsonStore: KSafe = koinInject(qualifier = customJsonKSafe)
    val preferencesStore: KSafe = koinInject(qualifier = preferencesKSafe)
    var cacheReady by remember { mutableStateOf(false) }

    LaunchedEffect(defaultStore, customJsonStore, preferencesStore) {
        awaitKSafeCachesReady(defaultStore, customJsonStore, preferencesStore)
        cacheReady = true
    }

    if (cacheReady) {
        ThemeAwareAppContent()
    }
}

@Composable
private fun ThemeAwareAppContent(
    themePreferenceRepository: ThemePreferenceRepository = koinInject(),
) {
    val themeMode: ThemeMode? by produceState<ThemeMode?>(
        initialValue = null,
        key1 = themePreferenceRepository,
    ) {
        themePreferenceRepository.themeMode.collect { value = it }
    }

    themeMode?.let { resolvedThemeMode ->
        KSafeDemoTheme(themeMode = resolvedThemeMode) {
            AppContent()
        }
    }
}

@Composable
fun AppContent() {
    val ksafe: KSafe = koinInject()
    var currentRoute: AppRoute by ksafe.rememberKSafeState(AppRoute.Storage)

    LaunchedEffect(ksafe) {
        val info = ksafe.protectionInfo
        println(
            "KSafe protection: " +
                "intended=${info.intendedLevel} " +
                "effective=${info.effectiveLevel} " +
                "custody=\"${info.custody}\" " +
                "notes=${info.notes}"
        )

        @Suppress("DEPRECATION")
        suspend fun demo(key: String, value: String, requested: String, mode: KSafeWriteMode) {
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

        demo(
            key = "demo_plain_theme",
            value = "dark",
            requested = "Plain",
            mode = KSafeWriteMode.Plain,
        )
        demo(
            key = "demo_encrypted_token",
            value = "abc-123",
            requested = "Encrypted(DEFAULT)",
            mode = KSafeWriteMode.Encrypted(),
        )
        demo(
            key = "demo_hw_isolated_secret",
            value = "supersecret",
            requested = "Encrypted(HARDWARE_ISOLATED)",
            mode = KSafeWriteMode.Encrypted(KSafeEncryptedProtection.HARDWARE_ISOLATED),
        )
    }

    NavigationRoot(
        selectedRoute = currentRoute,
        onRouteSelected = { currentRoute = it },
    )
}
