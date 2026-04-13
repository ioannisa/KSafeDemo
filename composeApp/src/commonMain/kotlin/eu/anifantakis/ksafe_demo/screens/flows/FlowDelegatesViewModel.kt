package eu.anifantakis.ksafe_demo.screens.flows

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.asFlow
import eu.anifantakis.lib.ksafe.asMutableStateFlow
import eu.anifantakis.lib.ksafe.asStateFlow
import eu.anifantakis.lib.ksafe.compose.mutableStateOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

// ─── Data classes ────────────────────────────────────────────────────────────

@Serializable
data class MoviesListState(
    val loading: Boolean = false,
    val movies: List<String> = emptyList(),
    val error: String? = null
)

// ─── ViewModel ───────────────────────────────────────────────────────────────

/**
 * Demonstrates all new 1.8.0 flow delegate APIs with real-world patterns
 * from the KSafe documentation examples.
 *
 * ## What's new in 1.8.0
 *
 * | Before 1.8.0                                    | New in 1.8.0                                              |
 * |-------------------------------------------------|-----------------------------------------------------------|
 * | `var x by ksafe(0)` — non-reactive              | `asStateFlow()` — read-only hot StateFlow                 |
 * | `var x by ksafe.mutableStateOf(0)` — no sync    | `asFlow()` — read-only cold Flow                          |
 * | `kSafe.getStateFlow("key", 0, scope)` — manual  | `asMutableStateFlow()` — read/write drop-in replacement   |
 * |                                                  | `mutableStateOf(scope=)` — Compose + cross-screen sync    |
 */
@Stable
class FlowDelegatesViewModel(
    private val ksafe: KSafe
) : ViewModel() {

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

    private val _moviesState by ksafe.asMutableStateFlow(MoviesListState(), viewModelScope)
    val moviesState: StateFlow<MoviesListState> get() = _moviesState

    fun loadMovies() {
        _moviesState.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            try {
                // Simulate API call
                kotlinx.coroutines.delay(800)
                val movies = listOf("Inception", "Interstellar", "The Matrix", "Blade Runner 2049")
                _moviesState.update { it.copy(loading = false, movies = movies) }
            } catch (e: Exception) {
                _moviesState.update { it.copy(loading = false, error = e.message) }
            }
        }
    }

    fun clearMovies() {
        _moviesState.value = MoviesListState()
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 2. asStateFlow & asFlow — READ-ONLY REACTIVE OBSERVATION
    //
    //    asStateFlow: HOT — always holds current value, needs scope.
    //                 Use in ViewModels to expose state to the UI.
    //
    //    asFlow:      COLD — only active when collected, no scope needed.
    //                 Use in repositories, or when you want to transform/
    //                 combine before exposing to the UI.
    //
    //    Write with kSafe.put() — both flows auto-update.
    // ═══════════════════════════════════════════════════════════════════════

    // Hot — always holds current value
    val username: StateFlow<String> by ksafe.asStateFlow("Guest", viewModelScope)

    // Cold — only active when collected, great for transformations
    val darkMode: Flow<Boolean> by ksafe.asFlow(defaultValue = false)

    // Cold flow transformed into a derived StateFlow — real-world pattern
    val themeLabel: StateFlow<String> = darkMode
        .map { isDark -> if (isDark) "Dark Mode" else "Light Mode" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Light Mode")

    fun onNameChanged(name: String) {
        viewModelScope.launch { ksafe.put("username", name) }
    }

    fun toggleDarkMode() {
        viewModelScope.launch {
            val current = ksafe.getDirect("darkMode", false)
            ksafe.put("darkMode", !current)
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // 3. CROSS-SCREEN SYNC — real demo with the Storage screen
    //
    //    The Storage screen (LibCounterViewModel) has:
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
    var storageCountIsolated by ksafe.mutableStateOf(2000, key = "count2")
        private set

    // WITH scope — observes "count2" via flow, updates when Storage screen increments it
    var storageCountSynced by ksafe.mutableStateOf(2000, key = "count2", scope = viewModelScope)
        private set

    /** Write to "count2" from THIS screen — proves cross-screen sync works both ways.
     *  Uses kSafe.put() to simulate an external write (like the Storage screen does).
     *  The synced delegate picks it up via flow; the isolated one stays frozen. */
    fun incrementFromFlowsScreen() {
        viewModelScope.launch {
            val current = ksafe.getDirect("count2", 2000)
            ksafe.put("count2", current + 1)
        }
    }

    /** Manual refresh for the isolated (no-scope) delegate.
     *  This is what you'd have to do WITHOUT scope: explicitly re-read from KSafe.
     *  With scope, this happens automatically via flow observation. */
    fun refreshIsolated() {
        storageCountIsolated = ksafe.getDirect("count2", 2000)
    }

    // ═══════════════════════════════════════════════════════════════════════
    // Clear all
    // ═══════════════════════════════════════════════════════════════════════

    fun clearAll() {
        viewModelScope.launch {
            ksafe.delete("moviesState")
            ksafe.delete("username")
            ksafe.delete("darkMode")
            // Note: we don't delete "count2" here — that belongs to the Storage screen.
            // The cross-screen demo reads it, not owns it.
        }
        _moviesState.value = MoviesListState()
        storageCountIsolated = 2000
        storageCountSynced = 2000
    }
}
