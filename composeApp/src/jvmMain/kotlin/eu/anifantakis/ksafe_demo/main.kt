package eu.anifantakis.ksafe_demo

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import eu.anifantakis.ksafe_demo.di.platformModule
import eu.anifantakis.ksafe_demo.di.sharedModule
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(sharedModule, platformModule)
    }

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "KSafeDemo",
        ) {
            App()
        }
    }
}