package eu.anifantakis.ksafe_demo.core.presentation.helper

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

fun <T> StateFlow<T>.toComposeState(scope: CoroutineScope): State<T> {
    val composeState = mutableStateOf(value)
    scope.launch {
        collect { composeState.value = it }
    }
    return composeState
}

