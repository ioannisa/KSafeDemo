package eu.anifantakis.ksafe_demo.core.presentation.global_state

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GlobalStateContainer {
    private val _state = MutableStateFlow(GlobalState())
    val state = _state.asStateFlow()

    private var loadingCount = 0
    private var criticalCount = 0

    private val _effects = MutableSharedFlow<GlobalEffect>(
        replay = 0,
        extraBufferCapacity = 32,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val effects = _effects.asSharedFlow()

    fun dispatch(intent: GlobalIntent) {
        when (intent) {
            is GlobalIntent.ShowLoading -> {
                loadingCount++
                if (intent.critical) criticalCount++
                _state.update {
                    it.copy(isLoading = true, isCriticalLoading = criticalCount > 0)
                }
            }
            is GlobalIntent.HideLoading -> {
                loadingCount = (loadingCount - 1).coerceAtLeast(0)
                if (intent.critical) criticalCount = (criticalCount - 1).coerceAtLeast(0)
                _state.update {
                    it.copy(
                        isLoading = loadingCount > 0,
                        isCriticalLoading = criticalCount > 0,
                    )
                }
            }
            is GlobalIntent.ShowSnackbar -> {
                _effects.tryEmit(GlobalEffect.SnackbarMessage(intent.message))
            }
        }
    }
}

