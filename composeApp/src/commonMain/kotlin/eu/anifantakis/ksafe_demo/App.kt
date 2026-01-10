package eu.anifantakis.ksafe_demo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import eu.anifantakis.ksafe_demo.screens.counters.LibCounterScreen
import eu.anifantakis.ksafe_demo.screens.security.SecurityScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

enum class Screen(val title: String) {
    Storage("Storage"),
    Security("Security")
}

@Composable
@Preview
fun App() {
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
                Screen.Security -> SecurityScreen()
            }
        }
    }
}