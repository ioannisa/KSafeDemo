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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import eu.anifantakis.ksafe_demo.di.createKoinConfiguration
import eu.anifantakis.ksafe_demo.screens.counters.LibCounterScreen
import eu.anifantakis.ksafe_demo.screens.customjson.CustomJsonScreen
import eu.anifantakis.ksafe_demo.screens.flows.FlowDelegatesScreen
import eu.anifantakis.ksafe_demo.screens.security.SecurityScreen
import org.koin.compose.KoinApplication
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.logger.Level

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
    var currentScreen by remember { mutableStateOf(Screen.Storage) }

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