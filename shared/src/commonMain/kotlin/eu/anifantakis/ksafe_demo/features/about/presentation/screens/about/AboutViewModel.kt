package eu.anifantakis.ksafe_demo.features.about.presentation.screens.about

import androidx.compose.runtime.State
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import eu.anifantakis.ksafe_demo.core.presentation.global_state.BaseGlobalViewModel
import eu.anifantakis.lib.ksafe.KSafe
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class AboutState(
    val kSafeVersion: String,
    val developerName: String = "Ioannis Anifantakis",
    val developerWebsite: String = "https://anifantakis.eu",
    val developerEmail: String = "ioannisanif@gmail.com",
    val developerGitHub: String = "https://github.com/ioannisa",
    val kSafeRepository: String = "https://github.com/ioannisa/KSafe",
    val kSafeDemoRepository: String = "https://github.com/ioannisa/KSafeDemo",
)

sealed interface AboutIntent {
    data class OpenLink(val uri: String) : AboutIntent
}

sealed interface AboutEffect {
    data class OpenUri(val uri: String) : AboutEffect
}

@Stable
class AboutViewModel(
    ksafe: KSafe,
) : BaseGlobalViewModel() {
    private val _state = mutableStateOf(
        AboutState(kSafeVersion = ksafe.protectionInfo.kSafeVersion),
    )
    val state: State<AboutState> = _state

    private val eventChannel = Channel<AboutEffect>()
    val events = eventChannel.receiveAsFlow()

    fun onAction(intent: AboutIntent) {
        when (intent) {
            is AboutIntent.OpenLink -> viewModelScope.launch {
                eventChannel.send(AboutEffect.OpenUri(intent.uri))
            }
        }
    }
}
