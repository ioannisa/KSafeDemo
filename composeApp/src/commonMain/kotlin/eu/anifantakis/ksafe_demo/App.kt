package eu.anifantakis.ksafe_demo

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import eu.anifantakis.ksafe_demo.di.platformModule
import eu.anifantakis.ksafe_demo.di.sharedModule
import eu.anifantakis.ksafe_demo.screens.counters.LibCounterScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinMultiplatformApplication
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.KoinConfiguration

@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
fun App() {

    KoinMultiplatformApplication(
        config = KoinConfiguration {
            modules(sharedModule, platformModule)
        }
    ) {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            LibCounterScreen()
        }
    }
}