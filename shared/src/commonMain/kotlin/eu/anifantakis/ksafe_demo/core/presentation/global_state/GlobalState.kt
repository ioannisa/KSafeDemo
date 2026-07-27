package eu.anifantakis.ksafe_demo.core.presentation.global_state

import androidx.compose.runtime.Immutable

@Immutable
data class GlobalState(
    val isLoading: Boolean = false,
    val isCriticalLoading: Boolean = false,
)

sealed interface GlobalIntent {
    data class ShowLoading(val critical: Boolean = false) : GlobalIntent
    data class HideLoading(val critical: Boolean = false) : GlobalIntent
    data class ShowSnackbar(val message: String) : GlobalIntent
}

sealed interface GlobalEffect {
    data class SnackbarMessage(val message: String) : GlobalEffect
}

