package eu.anifantakis.ksafe_demo.features.helpers.presentation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import eu.anifantakis.ksafe_demo.core.presentation.global_state.BaseGlobalViewModel
import eu.anifantakis.ksafe_demo.core.presentation.helper.toComposeState
import eu.anifantakis.lib.ksafe.KSafeEncrypted
import eu.anifantakis.lib.ksafe.KSafeHardwareIsolated
import eu.anifantakis.lib.ksafe.KSafePlain
import eu.anifantakis.lib.ksafe.compose.mutableStateOf
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Demonstrates the 3.1.0 mode-typed helper views: [eu.anifantakis.lib.ksafe.KSafePlain], [eu.anifantakis.lib.ksafe.KSafeEncrypted] and
 * [eu.anifantakis.lib.ksafe.KSafeHardwareIsolated]. Each handle freezes its write mode at the type level, so no
 * call below ever passes a `mode` argument — writing through `ksafePlain` is always
 * plain, through `ksafeHardwareIsolated` always requests StrongBox / Secure Enclave.
 *
 * Deliberately NOT MVI (unlike the other screens): nine plain states, three per view,
 * so each declaration style stays readable on its own:
 *
 *  1. `mutableStateOf`      — Compose state; the screen reads the property directly.
 *  2. `asMutableStateFlow`  — exposed as read-only [kotlinx.coroutines.flow.StateFlow]; the screen collects it.
 *  3. `asMutableStateFlow` + [toComposeState] — pre-collected in the ViewModel, so the
 *     screen reads a [androidx.compose.runtime.State] with no `collectAsState()` at the call site.
 */
@Stable
class HelpersViewModel(
    ksafePlain: KSafePlain,
    ksafeEncrypted: KSafeEncrypted,
    ksafeHardwareIsolated: KSafeHardwareIsolated,
) : BaseGlobalViewModel() {

    // ── 1. mutableStateOf — Compose state, read directly off the ViewModel ──

    var plainCounter1 by ksafePlain.mutableStateOf(0)
        private set

    var encryptedCounter1 by ksafeEncrypted.mutableStateOf(0)
        private set

    var hardwareIsolatedCounter1 by ksafeHardwareIsolated.mutableStateOf(0)
        private set

    // ── 2. asMutableStateFlow — read-only StateFlow, collectAsState() in the screen ──

    private val _plainCounter2 by ksafePlain.asMutableStateFlow(0, viewModelScope, key = "plainCounter2")
    val plainCounter2: StateFlow<Int> = _plainCounter2.asStateFlow()

    private val _encryptedCounter2 by ksafeEncrypted.asMutableStateFlow(0, viewModelScope, key = "encryptedCounter2")
    val encryptedCounter2: StateFlow<Int> = _encryptedCounter2.asStateFlow()

    private val _hardwareIsolatedCounter2 by ksafeHardwareIsolated.asMutableStateFlow(0, viewModelScope, key = "hardwareIsolatedCounter2")
    val hardwareIsolatedCounter2: StateFlow<Int> = _hardwareIsolatedCounter2.asStateFlow()

    // ── 3. asMutableStateFlow + toComposeState — no collect at the call site ──

    private val _plainCounter3 by ksafePlain.asMutableStateFlow(0, viewModelScope, key = "plainCounter3")
    val plainCounter3: State<Int> = _plainCounter3.toComposeState(viewModelScope)

    private val _encryptedCounter3 by ksafeEncrypted.asMutableStateFlow(0, viewModelScope, key = "encryptedCounter3")
    val encryptedCounter3: State<Int> = _encryptedCounter3.toComposeState(viewModelScope)

    private val _hardwareIsolatedCounter3 by ksafeHardwareIsolated.asMutableStateFlow(0, viewModelScope, key = "hardwareIsolatedCounter3")
    val hardwareIsolatedCounter3: State<Int> = _hardwareIsolatedCounter3.toComposeState(viewModelScope)

    // ── Increments — one per counter; persistence rides on the delegates ──

    fun incrementPlainCounter1() { plainCounter1++ }
    fun incrementEncryptedCounter1() { encryptedCounter1++ }
    fun incrementHardwareIsolatedCounter1() { hardwareIsolatedCounter1++ }

    fun incrementPlainCounter2() = _plainCounter2.update { it + 1 }
    fun incrementEncryptedCounter2() = _encryptedCounter2.update { it + 1 }
    fun incrementHardwareIsolatedCounter2() = _hardwareIsolatedCounter2.update { it + 1 }

    fun incrementPlainCounter3() = _plainCounter3.update { it + 1 }
    fun incrementEncryptedCounter3() = _encryptedCounter3.update { it + 1 }
    fun incrementHardwareIsolatedCounter3() = _hardwareIsolatedCounter3.update { it + 1 }
}