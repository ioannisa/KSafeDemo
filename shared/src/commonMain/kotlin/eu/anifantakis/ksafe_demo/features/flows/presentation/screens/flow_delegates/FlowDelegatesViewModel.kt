package eu.anifantakis.ksafe_demo.features.flows.presentation.screens.flow_delegates

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.lifecycle.viewModelScope
import eu.anifantakis.ksafe_demo.core.presentation.global_state.BaseGlobalViewModel
import eu.anifantakis.ksafe_demo.core.presentation.helper.toComposeState
import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.asFlow
import eu.anifantakis.lib.ksafe.asMutableStateFlow
import eu.anifantakis.lib.ksafe.compose.mutableStateOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Duration.Companion.milliseconds

// ─── Data classes ────────────────────────────────────────────────────────────

@Immutable
@Serializable
data class MoviesListState(
    val loading: Boolean = false,
    val movies: List<String> = emptyList(),
    val error: String? = null
)

@Immutable
data class FlowDelegatesState(
    val movies: MoviesListState = MoviesListState(),
    val username: String = "Guest",
    val toggleMode: Boolean = false,
    val toggleLabel: String = "Off Mode",
    val storageCountIsolated: Int = 2000,
    val storageCountSynced: Int = 2000,
)

sealed interface FlowDelegatesIntent {
    data object LoadMovies : FlowDelegatesIntent
    data object ClearMovies : FlowDelegatesIntent
    data class NameChanged(val name: String) : FlowDelegatesIntent
    data object ToggleMode : FlowDelegatesIntent
    data object IncrementStorageCounter : FlowDelegatesIntent
    data object RefreshIsolated : FlowDelegatesIntent
    data object ClearAll : FlowDelegatesIntent
}

/** This screen has no one-time local effects. */
sealed interface FlowDelegatesEffect

// ─── ViewModel ───────────────────────────────────────────────────────────────

/**
 * Demonstrates all new 1.8.0 flow delegate APIs with real-world patterns
 * from the KSafe documentation examples.
 *
 * ## What's new in 1.8.0
 *
 * | Before 1.8.0                                    | New in 1.8.0                                              |
 * |-------------------------------------------------|-----------------------------------------------------------|
 * | `var x by ksafe(0)` — non-reactive              | `asFlow()` — read-only cold Flow                           |
 * | `kSafe.getStateFlow("key", 0, scope)` — manual  | `asMutableStateFlow()` — read/write drop-in replacement   |
 * |                                                  | `mutableStateOf(scope=)` — Compose + cross-screen sync    |
 */
@Stable
class FlowDelegatesViewModel(
    private val ksafe: KSafe
) : BaseGlobalViewModel() {

    // ═══════════════════════════════════════════════════════════════════════
    // 1. asMutableStateFlow — DROP-IN REPLACEMENT FOR MutableStateFlow
    //
    //    Standard Kotlin:
    //      private val _state = MutableStateFlow(MoviesListState())
    //      val state = _state.asStateFlow()
    //
    //    KSafe equivalent — same API, but persisted + encrypted:
    //      private val _state by kSafe.asMutableStateFlow(MoviesListState(), viewModelScope)
    //      val state: StateFlow<MoviesListState> get() = _state
    //
    //    All standard operations work: .value, .update{}, .compareAndSet()
    // ═══════════════════════════════════════════════════════════════════════

    private val _moviesState by ksafe.asMutableStateFlow(MoviesListState(), viewModelScope, key = "moviesState")
    private val moviesState: StateFlow<MoviesListState> get() = _moviesState

    private fun loadMovies() {
        _moviesState.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            try {
                // Simulate API call
                kotlinx.coroutines.delay(800.milliseconds)
                val movies = listOf("Inception", "Interstellar", "The Matrix", "Blade Runner 2049")
                _moviesState.update { it.copy(loading = false, movies = movies) }
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _moviesState.update { it.copy(loading = false, error = error.message) }
            }
        }
    }

    private fun clearMovies() {
        _moviesState.value = MoviesListState()
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 2. asFlow & TWO-WAY BINDING
    //
    //    asFlow:  COLD — only active when collected, no scope needed.
    //             Use in repositories, or when you want to transform/
    //             combine before exposing to the UI (see themeLabel).
    //
    //    The username shows the standard shape for editable persisted state:
    //    a private asMutableStateFlow the screen writes through, exposed
    //    publicly as a read-only StateFlow.
    // ═══════════════════════════════════════════════════════════════════════

    private val _username: MutableStateFlow<String> by ksafe.asMutableStateFlow("Guest", viewModelScope, key = "username")
    private val username: StateFlow<String> get() = _username

    // Cold — only active when collected, great for transformations
    private val toggleMode: Flow<Boolean> by ksafe.asFlow(defaultValue = false)

    // Cold flow transformed into a derived StateFlow — real-world pattern
    private val toggleLabel: StateFlow<String> = toggleMode
        .map { isOn -> if (isOn) "On Mode" else "Off Mode" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Light Mode")

    private fun onNameChanged(name: String) {
        _username.update { name }
    }

    private fun toggleMode() {
        viewModelScope.launch {
            val current = ksafe.getDirect("toggleMode", false)
            ksafe.put("toggleMode", !current)
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 3. CROSS-SCREEN SYNC — real demo with the Storage screen
    //
    //    The Storage screen (StorageViewModel) has:
    //      var count2 by ksafe.mutableStateOf(2000)  // key = "count2", NO scope
    //
    //    Here we observe the SAME key "count2" two ways:
    //      - without scope: reads once at init, stays stale
    //      - with scope: live subscription, updates when Storage screen changes it
    //
    //    DEMO: Go to Storage tab, tap "+", come back here.
    //    Only the synced value updates. The isolated one is frozen.
    // ═══════════════════════════════════════════════════════════════════════

    // NO scope — reads "count2" from cache at init, won't see Storage screen's writes
    private var storageCountIsolated by ksafe.mutableStateOf(2000, key = "count2")
        private set

    // WITH scope — observes "count2" via flow, updates when Storage screen increments it
    private var storageCountSynced by ksafe.mutableStateOf(2000, key = "count2", scope = viewModelScope)
        private set

    /** Write to "count2" from THIS screen — proves cross-screen sync works both ways.
     *  Uses kSafe.put() to simulate an external write (like the Storage screen does).
     *  The synced delegate picks it up via flow; the isolated one stays frozen. */
    private fun incrementFromFlowsScreen() {
        viewModelScope.launch {
            val current = ksafe.getDirect("count2", 2000)
            ksafe.put("count2", current + 1)
        }
    }

    /** Manual refresh for the isolated (no-scope) delegate.
     *  This is what you'd have to do WITHOUT scope: explicitly re-read from KSafe.
     *  With scope, this happens automatically via flow observation. */
    private fun refreshIsolated() {
        storageCountIsolated = ksafe.getDirect("count2", 2000)
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Clear all
    // ═══════════════════════════════════════════════════════════════════════

    private fun clearAll() {
        viewModelScope.launch {
            ksafe.delete("moviesState")
            ksafe.delete("username")
            ksafe.delete("toggleMode")
            // Note: we don't delete "count2" here — that belongs to the Storage screen.
            // The cross-screen demo reads it, not owns it.
        }
        _moviesState.value = MoviesListState()
        storageCountIsolated = 2000
        storageCountSynced = 2000
    }

    private val moviesComposeState = moviesState.toComposeState(viewModelScope)
    private val usernameComposeState = username.toComposeState(viewModelScope)
    private val toggleModeState = toggleMode
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
        .toComposeState(viewModelScope)
    private val toggleLabelComposeState = toggleLabel.toComposeState(viewModelScope)

    val state: State<FlowDelegatesState> = derivedStateOf {
        FlowDelegatesState(
            movies = moviesComposeState.value,
            username = usernameComposeState.value,
            toggleMode = toggleModeState.value,
            toggleLabel = toggleLabelComposeState.value,
            storageCountIsolated = storageCountIsolated,
            storageCountSynced = storageCountSynced,
        )
    }

    fun onAction(intent: FlowDelegatesIntent) {
        when (intent) {
            FlowDelegatesIntent.LoadMovies -> loadMovies()
            FlowDelegatesIntent.ClearMovies -> clearMovies()
            is FlowDelegatesIntent.NameChanged -> onNameChanged(intent.name)
            FlowDelegatesIntent.ToggleMode -> toggleMode()
            FlowDelegatesIntent.IncrementStorageCounter -> incrementFromFlowsScreen()
            FlowDelegatesIntent.RefreshIsolated -> refreshIsolated()
            FlowDelegatesIntent.ClearAll -> clearAll()
        }
    }
}
