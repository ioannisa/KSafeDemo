package eu.anifantakis.ksafe_demo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import eu.anifantakis.ksafe_demo.di.createKoinConfiguration
import eu.anifantakis.ksafe_demo.screens.counters.LibCounterScreen
import eu.anifantakis.ksafe_demo.screens.customjson.CustomJsonScreen
import eu.anifantakis.ksafe_demo.screens.flows.FlowDelegatesScreen
import eu.anifantakis.ksafe_demo.screens.security.SecurityScreen
import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeEncryptedProtection
import eu.anifantakis.lib.ksafe.KSafeWriteMode
import eu.anifantakis.lib.ksafe.compose.rememberKSafeState
import kotlinx.serialization.Serializable
import org.koin.compose.KoinApplication
import org.koin.compose.koinInject
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.logger.Level

@Serializable
enum class Screen(val title: String) {
    Storage("Storage"),
    Flows("Flows"),
    CustomJson("Custom JSON"),
    Security("Security")
}

@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
fun App() {
    KoinApplication(configuration = createKoinConfiguration(), logLevel = Level.INFO, content = {
        AppContent()
    })
}

@Composable
fun AppContent() {
    // Persisted across app restarts via KSafe — the bottom-tab selection
    // survives process death without any boilerplate. Compare with the
    // pre-rememberKSafeState version that used `remember { mutableStateOf(...) }`,
    // which only survived recomposition.
    val ksafe: KSafe = koinInject()
    var currentScreen by ksafe.rememberKSafeState(Screen.Storage)

    // Log the negotiated key-custody once per process startup. `protectionInfo`
    // is fixed for the instance's lifetime, so a single read here is enough.
    // Output goes to Logcat (Android via stdout), the terminal (JVM / macOS),
    // the browser dev tools console (Web), or NSLog (iOS).
    LaunchedEffect(Unit) {
        val info = ksafe.protectionInfo
        println(
            "KSafe protection: " +
                "intended=${info.intendedLevel} " +
                "effective=${info.effectiveLevel} " +
                "custody=\"${info.custody}\" " +
                "notes=${info.notes}"
        )

        // ---- Per-key protection-level demo ----
        // Write three sample values with different write modes, then log what
        // each key ACTUALLY ended up with. The HARDWARE_ISOLATED case is the
        // most interesting: on a device without StrongBox / Secure Enclave the
        // engine silently demotes to HARDWARE_BACKED, and on JVM/Web it
        // demotes further (SANDBOX_PROTECTED or SOFTWARE). `getKeyInfo` is how
        // a real app verifies that a specific sensitive write got the custody
        // it asked for. Clean up after the demo so we don't leave demo data.
        // Intentionally reads both the new `level` (KSafeProtectionLevel) and
        // the legacy `storage` (KSafeKeyStorage) so the log shows side-by-side
        // what the deprecation actually changes — useful for consumers
        // migrating off `storage`. `@Suppress("DEPRECATION")` here is explicit
        // acknowledgement that we're touching the deprecated field on purpose.
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                Screen.entries.forEach { screen ->
                    NavigationBarItem(
                        selected = currentScreen == screen,
                        onClick = { currentScreen = screen },
                        label = { Text(screen.title) },
                        icon = { }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentScreen) {
                Screen.Storage -> LibCounterScreen()
                Screen.Flows -> FlowDelegatesScreen()
                Screen.CustomJson -> CustomJsonScreen()
                Screen.Security -> SecurityScreen()
            }
        }
    }
}