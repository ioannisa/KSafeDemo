package eu.anifantakis.ksafe_demo

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document

// Regular Kotlin/JS (non-WASM) does not use the awaitCacheReady gate.
// ComposeViewport already waits for Skiko WASM via onSkikoReady before
// calling any composable content, so the rendering path is safe.
// KSafe initialises lazily on first access; skipping the suspend gate here
// mirrors the behaviour of the Android, Desktop, iOS, and macOS targets.
@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val body = document.body ?: return
    ComposeViewport(body) {
        App()
    }
}
