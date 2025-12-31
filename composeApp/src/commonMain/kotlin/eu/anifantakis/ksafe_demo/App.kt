package eu.anifantakis.ksafe_demo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import eu.anifantakis.ksafe_demo.screens.counters.LibCounterScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinContext

@Composable
@Preview
fun App() {
    KoinContext {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            LibCounterScreen()
        }
    }
}