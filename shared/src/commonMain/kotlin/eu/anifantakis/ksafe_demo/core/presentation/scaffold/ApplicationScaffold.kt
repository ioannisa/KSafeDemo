package eu.anifantakis.ksafe_demo.core.presentation.scaffold

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppLoadingIndicator
import eu.anifantakis.ksafe_demo.core.presentation.global_state.GlobalEffect
import eu.anifantakis.ksafe_demo.core.presentation.global_state.GlobalStateContainer
import eu.anifantakis.ksafe_demo.core.presentation.helper.ObserveEffects
import org.koin.compose.koinInject

@Composable
fun ApplicationScaffold(
    bottomBar: @Composable () -> Unit,
    globalStateContainer: GlobalStateContainer = koinInject(),
    content: @Composable (PaddingValues) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val globalState by globalStateContainer.state.collectAsState()

    ObserveEffects(globalStateContainer.effects) { effect ->
        when (effect) {
            is GlobalEffect.SnackbarMessage -> snackbarHostState.showSnackbar(effect.message)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = bottomBar,
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            content(padding)
            AppLoadingIndicator(isLoading = globalState.isLoading)
        }
    }
}

