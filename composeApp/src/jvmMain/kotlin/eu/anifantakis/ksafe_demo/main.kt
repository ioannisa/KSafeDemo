package eu.anifantakis.ksafe_demo

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "KSafeDemo",
    ) {
        App()
    }
}