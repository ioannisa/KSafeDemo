package eu.anifantakis.ksafe_demo

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val body = document.body ?: return
    ComposeViewport(body) {
        App(
            onPlatformSplashReadyToDismiss = {
                document
                    .getElementById("app-startup-placeholder")
                    ?.let { placeholder -> placeholder.parentNode?.removeChild(placeholder) }
            },
        )
    }
}
