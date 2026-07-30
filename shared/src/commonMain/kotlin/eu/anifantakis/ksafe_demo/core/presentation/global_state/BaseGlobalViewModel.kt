package eu.anifantakis.ksafe_demo.core.presentation.global_state

import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.anifantakis.ksafe_demo.core.presentation.helper.toComposeState
import eu.anifantakis.ksafe_demo.core.presentation.helper.UiText
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey
import kotlin.coroutines.cancellation.CancellationException
import org.koin.mp.KoinPlatform

abstract class BaseGlobalViewModel(
    protected val globalStateContainer: GlobalStateContainer = KoinPlatform.getKoin().get(),
) : ViewModel() {
    val globalUiState by globalStateContainer.state.toComposeState(viewModelScope)

    protected fun showSnackbar(message: UiText) {
        globalStateContainer.dispatch(GlobalIntent.ShowSnackbar(message))
    }

    protected fun showSnackbar(key: StringKey, vararg args: Any) {
        showSnackbar(UiText.res(key, *args))
    }

    protected suspend fun <T> withLoading(
        critical: Boolean = false,
        block: suspend () -> T,
    ): Result<T> {
        globalStateContainer.dispatch(GlobalIntent.ShowLoading(critical))
        return try {
            Result.success(block())
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            Result.failure(error)
        } finally {
            globalStateContainer.dispatch(GlobalIntent.HideLoading(critical))
        }
    }
}
