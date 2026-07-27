package eu.anifantakis.ksafe_demo.features.security.presentation.screens.security

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import eu.anifantakis.ksafe_demo.core.presentation.global_state.BaseGlobalViewModel
import eu.anifantakis.ksafe_demo.di.SecurityViolationsHolder
import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeProtectionInfo
import eu.anifantakis.lib.ksafe.compose.UiSecurityViolation
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class SecurityState(
    val protectionInfo: KSafeProtectionInfo,
    val violations: ImmutableList<UiSecurityViolation>,
)

sealed interface SecurityIntent {
    data object Refresh : SecurityIntent
}

/** This screen has no one-time local effects. */
sealed interface SecurityEffect

@Stable
class SecurityViewModel(
    private val ksafe: KSafe,
) : BaseGlobalViewModel() {
    private val mutableState = mutableStateOf(readState())
    val state: State<SecurityState> = mutableState

    fun onAction(intent: SecurityIntent) {
        when (intent) {
            SecurityIntent.Refresh -> mutableState.value = readState()
        }
    }

    private fun readState(): SecurityState = SecurityState(
        // This value is live and may change after rotation or recovery.
        protectionInfo = ksafe.protectionInfo,
        violations = SecurityViolationsHolder.violations
            .map(::UiSecurityViolation)
            .toImmutableList(),
    )
}
