package eu.anifantakis.ksafe_demo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import eu.anifantakis.ksafe_demo.di.createKoinConfiguration
import eu.anifantakis.ksafe_demo.screens.counters.LibCounterScreen
import eu.anifantakis.ksafe_demo.screens.customjson.CustomJsonScreen
import eu.anifantakis.ksafe_demo.screens.flows.FlowDelegatesScreen
import eu.anifantakis.ksafe_demo.screens.security.SecurityScreen
import eu.anifantakis.lib.ksafe.KSafe
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