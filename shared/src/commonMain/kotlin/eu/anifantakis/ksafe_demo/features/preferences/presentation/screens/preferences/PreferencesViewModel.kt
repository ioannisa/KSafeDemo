package eu.anifantakis.ksafe_demo.features.preferences.presentation.screens.preferences

import androidx.compose.runtime.State
import androidx.compose.runtime.Stable
import androidx.lifecycle.viewModelScope
import eu.anifantakis.ksafe_demo.core.presentation.global_state.BaseGlobalViewModel
import eu.anifantakis.ksafe_demo.core.presentation.helper.toComposeState
import eu.anifantakis.ksafe_demo.features.preferences.domain.model.ThemeMode
import eu.anifantakis.ksafe_demo.features.preferences.domain.repository.ThemePreferenceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

data class PreferencesState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
)

sealed interface PreferencesIntent {
    data class ThemeSelected(val themeMode: ThemeMode) : PreferencesIntent
}

/** This screen has no one-time local effects. */
sealed interface PreferencesEffect

@Stable
class PreferencesViewModel(
    private val themePreferenceRepository: ThemePreferenceRepository,
) : BaseGlobalViewModel() {
    private val stateFlow = themePreferenceRepository.themeMode
        .map(::PreferencesState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = PreferencesState(),
        )

    val state: State<PreferencesState> = stateFlow.toComposeState(viewModelScope)

    fun onAction(intent: PreferencesIntent) {
        when (intent) {
            is PreferencesIntent.ThemeSelected ->
                themePreferenceRepository.setThemeMode(intent.themeMode)
        }
    }
}
