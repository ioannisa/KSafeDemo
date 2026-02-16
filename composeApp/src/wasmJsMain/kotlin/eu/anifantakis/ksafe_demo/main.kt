package eu.anifantakis.ksafe_demo

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import eu.anifantakis.ksafe_demo.di.createKoinConfiguration
import eu.anifantakis.lib.ksafe.KSafe
import kotlinx.browser.document
import org.koin.compose.KoinMultiplatformApplication
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.mp.KoinPlatform.getKoin

@OptIn(ExperimentalComposeUiApi::class, KoinExperimentalAPI::class)
fun main() {
    val body = document.body ?: return
    ComposeViewport(body) {
        KoinMultiplatformApplication(
            config = createKoinConfiguration()
        ) {
            var cacheReady by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                val ksafe: KSafe = getKoin().get()
                ksafe.awaitCacheReady()
                cacheReady = true
            }

            if (cacheReady) {
                AppContent()
            }
        }
    }
}
