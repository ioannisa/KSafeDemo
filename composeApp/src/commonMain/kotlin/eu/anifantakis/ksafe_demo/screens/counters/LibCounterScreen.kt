package eu.anifantakis.ksafe_demo.screens.counters

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LibCounterScreen(
    counterViewModel: LibCounterViewModel = koinViewModel()
) {
    LibCounterScreenContent(
        count1 = counterViewModel.count1,
        count2 = counterViewModel.count2,
        count3 = counterViewModel.count3,
        bioCount = counterViewModel.bioCount,
        bioAuthRemaining = counterViewModel.bioAuthRemaining,
        authInfo = counterViewModel.authInfo,
        lockTestCountdown = counterViewModel.lockTestCountdown,
        lockTestResult = counterViewModel.lockTestResult,
        isLockTestRunning = counterViewModel.isLockTestRunning,
        onIncrement = counterViewModel::increment,
        onClear = counterViewModel::clear,
        onBioIncrement = counterViewModel::bioCounterIncrement,
        onStartLockTest = counterViewModel::startLockTest,
        onDismissLockTestResult = counterViewModel::dismissLockTestResult
    )
}

@Composable
fun LibCounterScreenContent(
    count1: Int,
    count2: Int,
    count3: Int,
    bioCount: Int,
    bioAuthRemaining: Int,
    authInfo: AuthInfo,
    lockTestCountdown: Int,
    lockTestResult: String?,
    isLockTestRunning: Boolean,
    onIncrement: () -> Unit,
    onClear: () -> Unit,
    onBioIncrement: () -> Unit,
    onStartLockTest: () -> Unit,
    onDismissLockTestResult: () -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // UI State (Dialog visibility) is local to this screen, which is fine
        var showDialog by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(scrollState)
                .padding(vertical = 10.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {

            // --- Counter 1: regular mutableStateOf ---
            Text(
                text = "mutableStateOf (no persistence)",
                fontSize = 13.sp,
                color = Color.Gray
            )
            CompactCard(
                label = "Counter 1",
                sublabel = "plain state — resets on restart",
                value = count1.toString(),
                modifier = Modifier.fillMaxWidth(0.5f)
            )

            Spacer(modifier = Modifier.padding(2.dp))

            // --- Counters 2-3: ksafe.mutableStateOf ---
            Text(
                text = "ksafe.mutableStateOf (persisted)",
                fontSize = 13.sp,
                color = Color.Gray
            )
            Row(
                modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                CompactCard(
                    label = "Counter 2",
                    sublabel = "encrypted",
                    value = count2.toString(),
                    modifier = Modifier.weight(1f)
                )
                CompactCard(
                    label = "Counter 3",
                    sublabel = "unencrypted",
                    value = count3.toString(),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.padding(2.dp))

            // --- AuthInfo: data class with ksafe.mutableStateOf ---
            Text(
                text = "ksafe.mutableStateOf — data class (persisted, encrypted)",
                fontSize = 13.sp,
                color = Color.Gray
            )
            LabelCard(
                label = "AuthInfo",
                lines = persistentListOf(
                    "accessToken: ${authInfo.accessToken}",
                    "refreshToken: ${authInfo.refreshToken}",
                    "expiresIn: ${authInfo.expiresIn}"
                )
            )

            Spacer(modifier = Modifier.padding(4.dp))

            // --- Action buttons ---
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onIncrement) {
                    Text(text = "+", fontSize = 20.sp)
                }
                Button(onClick = { showDialog = true }) {
                    Text(text = "Clear", fontSize = 20.sp)
                }
                Button(onClick = onBioIncrement) {
                    Text(text = "Bio: $bioCount", fontSize = 20.sp)
                }
            }

            if (bioAuthRemaining > 0) {
                Text(
                    text = "Bio auth window open — no prompt for ${bioAuthRemaining}s",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E7D32) // green
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 6.dp))

            // --- Lock-State Policy Test ---
            Text(text = "Lock-State Policy Test", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "Run from Home Screen, not Xcode, for accurate results",
                fontSize = 11.sp,
                color = Color.Gray
            )

            if (isLockTestRunning) {
                Text(
                    text = if (lockTestCountdown > 0)
                        "Lock your device now! Reading in $lockTestCountdown s..."
                    else
                        "Attempting encrypted read...",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (lockTestCountdown > 0) Color.Red else Color.Gray
                )
            } else {
                Button(onClick = onStartLockTest) {
                    Text(text = "Test Lock Feature", fontSize = 20.sp)
                }
            }
        }

        if (lockTestResult != null) {
            AlertDialog(
                onDismissRequest = onDismissLockTestResult,
                title = { Text("Lock Test Result") },
                text = { Text(lockTestResult) },
                confirmButton = {
                    TextButton(onClick = onDismissLockTestResult) {
                        Text("OK")
                    }
                }
            )
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Clear all values?") },
                text = { Text("This will delete all stored keys and reset counters to their defaults.") },
                confirmButton = {
                    TextButton(onClick = {
                        onClear()
                        showDialog = false
                    }) {
                        Text("Clear")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun CompactCard(
    label: String,
    sublabel: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Text(text = value, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Text(text = sublabel, fontSize = 10.sp, color = Color.Gray)
    }
}

@Composable
fun LabelCard(
    label: String,
    lines: ImmutableList<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .padding(horizontal = 12.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 2.dp)
        )
        lines.forEach {
            Text(
                text = it,
                fontSize = 14.sp
            )
        }
    }
}

// --- Preview Section ---

@Preview(showBackground = true, name = "Standard State")
@Composable
fun PreviewLibCounterScreen() {
    LibCounterScreenContent(
        count1 = 1000,
        count2 = 2000,
        count3 = 3000,
        bioCount = 5,
        bioAuthRemaining = 0,
        authInfo = AuthInfo("abc_token", "ref_token", 9999),
        lockTestCountdown = -1,
        lockTestResult = null,
        isLockTestRunning = false,
        onIncrement = {},
        onClear = {},
        onBioIncrement = {},
        onStartLockTest = {},
        onDismissLockTestResult = {}
    )
}