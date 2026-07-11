package eu.anifantakis.ksafe_demo.screens.flows

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FlowDelegatesScreen(
    viewModel: FlowDelegatesViewModel = koinViewModel()
) {
    val scrollState = rememberScrollState()

    val moviesState by viewModel.moviesState.collectAsState()
    val username by viewModel.username.collectAsState()
    val darkMode by viewModel.darkMode.collectAsState(initial = false)
    val themeLabel by viewModel.themeLabel.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(vertical = 10.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text("Flow Delegates (1.8.0)", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(
                "New APIs that bring MutableStateFlow + encryption + persistence together",
                fontSize = 12.sp, color = Color.Gray
            )

            // ═════════════════════════════════════════════════════════════
            // 1. asMutableStateFlow — Movies example from docs
            // ═════════════════════════════════════════════════════════════
            SectionHeader("asMutableStateFlow — drop-in MutableStateFlow")

            CodeSnippet(
                "// Standard:  private val _state = MutableStateFlow(MoviesListState())\n" +
                "// KSafe:     private val _state by kSafe.asMutableStateFlow(MoviesListState(), viewModelScope)\n" +
                "// Same API: .update{}, .value =, .asStateFlow() — all persist instantly"
            )

            // Movies state card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .padding(8.dp)
            ) {
                Text("MoviesListState (persisted + encrypted)", fontSize = 12.sp, color = Color.Gray)
                Spacer(Modifier.height(4.dp))

                when {
                    moviesState.loading -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            CircularProgressIndicator(modifier = Modifier.padding(4.dp))
                            Text("Loading movies...", fontSize = 14.sp)
                        }
                    }
                    moviesState.error != null -> {
                        Text("Error: ${moviesState.error}", fontSize = 14.sp, color = Color.Red)
                    }
                    moviesState.movies.isNotEmpty() -> {
                        moviesState.movies.forEach { movie ->
                            Text("  $movie", fontSize = 14.sp)
                        }
                    }
                    else -> {
                        Text("No movies loaded", fontSize = 14.sp, color = Color.Gray)
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Button(onClick = viewModel::loadMovies) { Text("Load Movies") }
                Button(onClick = viewModel::clearMovies) { Text("Clear") }
            }

            Text(
                "_moviesState.update { it.copy(loading = true) } — persists the entire state atomically",
                fontSize = 11.sp, color = Color.Gray
            )

            SectionDivider()

            // ═════════════════════════════════════════════════════════════
            // 2. asStateFlow & asFlow — Settings example from docs
            // ═════════════════════════════════════════════════════════════
            SectionHeader("asStateFlow & asFlow — read-only reactive")

            CodeSnippet(
                "// Hot — always holds current value:\n" +
                "val username: StateFlow<String> by kSafe.asStateFlow(\"Guest\", viewModelScope)\n\n" +
                "// Cold — transform, combine, then expose:\n" +
                "val darkMode: Flow<Boolean> by kSafe.asFlow(defaultValue = false)\n" +
                "val themeLabel = darkMode.map { if (it) \"Dark\" else \"Light\" }.stateIn(...)"
            )

            // Username input
            OutlinedTextField(
                value = username,
                onValueChange = viewModel::onNameChanged,
                label = { Text("Username (asStateFlow)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Dark mode toggle with derived label
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Dark Mode (asFlow)", fontSize = 14.sp)
                    Text(
                        "Derived: $themeLabel",
                        fontSize = 12.sp,
                        color = Color(0xFF2E7D32),
                        fontWeight = FontWeight.Bold
                    )
                }
                Switch(checked = darkMode, onCheckedChange = { viewModel.toggleDarkMode() })
            }

            Text(
                "Type a name → asStateFlow emits. Toggle switch → asFlow emits → " +
                    "themeLabel derived via .map{}.stateIn()",
                fontSize = 11.sp, color = Color.Gray
            )

            SectionDivider()

            // ═════════════════════════════════════════════════════════════
            // 3. Cross-screen sync — observing Storage screen's count2
            // ═════════════════════════════════════════════════════════════
            SectionHeader("mutableStateOf(scope) — cross-screen sync")

            CodeSnippet(
                "// Storage screen (LibCounterViewModel) has:\n" +
                "//   var count2 by ksafe.mutableStateOf(2000)  // no scope\n\n" +
                "// This screen observes the SAME key \"count2\" two ways:\n\n" +
                "// No scope: reads once at init, stays stale\n" +
                "var isolated by kSafe.mutableStateOf(2000, key = \"count2\")\n\n" +
                "// With scope: live subscription from Storage screen\n" +
                "var synced by kSafe.mutableStateOf(2000, key = \"count2\", scope = viewModelScope)"
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                ValueCard(
                    label = "isolated (no scope)",
                    value = viewModel.storageCountIsolated.toString(),
                    modifier = Modifier.weight(1f)
                )
                ValueCard(
                    label = "synced (with scope)",
                    value = viewModel.storageCountSynced.toString(),
                    modifier = Modifier.weight(1f)
                )
            }

            Column (verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Button(onClick = viewModel::incrementFromFlowsScreen) {
                    Text("+1 from this screen", fontSize = 13.sp)
                }
                Button(onClick = viewModel::refreshIsolated) {
                    Text("Refresh isolated", fontSize = 13.sp)
                }
            }

            Text(
                "Tap \"+1\" or go to Storage tab and tap \"+\". " +
                    "The synced value updates in real-time. The isolated value stays frozen.",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1565C0)
            )
            Text(
                "\"Frozen\" does NOT mean the data is lost — it IS persisted on disk. " +
                    "Without scope, the delegate reads the cache once at init and never observes changes. " +
                    "Tap \"Refresh isolated\" to manually re-read from KSafe — this is what you'd " +
                    "have to do without scope. With scope, it happens automatically.",
                fontSize = 11.sp,
                color = Color.Gray
            )

            SectionDivider()

            // ═════════════════════════════════════════════════════════════
            // Clear all
            // ═════════════════════════════════════════════════════════════
            Spacer(modifier = Modifier.padding(4.dp))
            Button(onClick = viewModel::clearAll) {
                Text("Clear All Flow Demos", fontSize = 16.sp)
            }
        }
    }
}

// ─── Reusable components ─────────────────────────────────────────────────────

@Composable
private fun SectionHeader(text: String) {
    Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1565C0))
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
}

@Composable
private fun CodeSnippet(code: String) {
    Text(
        text = code,
        fontSize = 11.sp,
        fontFamily = FontFamily.Monospace,
        color = Color(0xFF4E342E),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFFFF3E0), RoundedCornerShape(4.dp))
            .padding(6.dp)
    )
}

@Composable
private fun ValueCard(label: String, value: String, modifier: Modifier = Modifier.fillMaxWidth(0.5f)) {
    Column(
        modifier = modifier
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Text(text = value, fontSize = 26.sp, fontWeight = FontWeight.Bold)
    }
}
