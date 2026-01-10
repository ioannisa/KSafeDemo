package eu.anifantakis.ksafe_demo.screens.counters

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
        authInfo = counterViewModel.authInfo,
        onIncrement = counterViewModel::increment,
        onClear = counterViewModel::clear,
        onBioIncrement = counterViewModel::bioCounterIncrement
    )
}

@Composable
fun LibCounterScreenContent(
    count1: Int,
    count2: Int,
    count3: Int,
    bioCount: Int,
    authInfo: AuthInfo,
    onIncrement: () -> Unit,
    onClear: () -> Unit,
    onBioIncrement: () -> Unit
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
                .padding(vertical = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(7.dp),
        ) {

            Text(text = "mutableStateOf", fontSize = 17.sp)
            LabelCard(label = "Counter 1:", lines = persistentListOf(count1.toString()))

            Text(text = "mutableStateOf - KSafe - Encrypted", fontSize = 17.sp)
            LabelCard(label = "Counter 2:", lines = persistentListOf(count2.toString()))

            Text(text = "mutableStateOf - KSafe - Not Encrypted", fontSize = 17.sp)
            LabelCard(label = "Counter 3:", lines = persistentListOf(count3.toString()))

            Text(text = "mutableStateOf - KSafe - Encrypted", fontSize = 17.sp)
            LabelCard(
                label = "data class AuthInfo",
                lines = persistentListOf(
                    "accessToken: ${authInfo.accessToken}",
                    "refreshToken: ${authInfo.refreshToken}",
                    "expiresIn: ${authInfo.expiresIn}"
                )
            )

            Spacer(modifier = Modifier.padding(15.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onIncrement) {
                    Text(text = "+", fontSize = 23.sp)
                }

                Button(onClick = { showDialog = true }) {
                    Text(text = "Clear", fontSize = 23.sp)
                }
            }

            Button(onClick = onBioIncrement) {
                Text(text = "Biometric Count: $bioCount", fontSize = 23.sp)
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Restart the app") },
                text = { Text("Just deleting the keys doesn't update the UI. Restart the app to see the changes.") },
                confirmButton = {
                    TextButton(onClick = {
                        onClear()
                        showDialog = false
                    }) {
                        Text("OK")
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
fun LabelCard(
    label: String,
    lines: ImmutableList<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .border(
                width = 2.dp,
                color = Color.Black,
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = Color.White,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(7.dp)
            .padding(horizontal = 44.dp)
    ) {
        Text(
            text = label,
            fontSize = 22.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 3.dp)
        )
        lines.forEach {
            Text(
                text = it,
                fontSize = if (lines.count() == 1) 44.sp else 19.sp,
                fontWeight = if (lines.count() == 1) FontWeight.Bold else FontWeight.Normal
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
        authInfo = AuthInfo("abc_token", "ref_token", 9999),
        onIncrement = {},
        onClear = {},
        onBioIncrement = {}
    )
}