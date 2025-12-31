package eu.anifantakis.ksafe_demo.screens.counters

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LibCounterScreen(counterViewModel: LibCounterViewModel = koinViewModel()) {
    // UI elements
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        var showDialog by remember { mutableStateOf(false) }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            Text(text = "mutableStateOf", fontSize = 18.sp)
            LabelCard(label = "Counter 1:", lines = listOf(counterViewModel.count1.toString()))

            Text(text = "mutableStateOf - KSafe - Encrypted", fontSize = 18.sp)
            LabelCard(label = "Counter 2:", lines = listOf(counterViewModel.count2.toString()))

            Text(text = "mutableStateOf - KSafe - Not Encrypted", fontSize = 18.sp)
            LabelCard(label = "Counter 3:", lines = listOf(counterViewModel.count3.toString()))

            Text(text = "mutableStateOf - KSafe - Encrypted", fontSize = 18.sp)
            LabelCard(label = "data class AuthInfo", lines =
                listOf(
                    "accessToken: ${counterViewModel.authInfo.accessToken}",
                    "refreshToken: ${counterViewModel.authInfo.refreshToken}",
                    "expiresIn: ${counterViewModel.authInfo.expiresIn}"
                ),
            )

            Spacer(modifier = Modifier.padding(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = { counterViewModel.increment() }) {
                    Text(text = "+", fontSize = 24.sp)
                }

                Button(onClick = { showDialog = true }) {
                    Text(text = "Clear", fontSize = 24.sp)
                }
            }

            Button(onClick = { counterViewModel.bioCounterIncrement() }) {
                Text(text = "Biometric Count: ${counterViewModel.bioCount}", fontSize = 24.sp)
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Restart the app") },
                text = { Text("Just deleting the keys doesn't update the UI. Restart the app to see the changes.") },
                confirmButton = {
                    TextButton(onClick = {
                        counterViewModel.clear()
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
    lines: List<String>,
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
            .padding(8.dp)
            .padding(horizontal = 48.dp)
    ) {
        Text(
            text = label,
            fontSize = 24.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        lines.forEach {
            Text(
                text = it,
                fontSize = if (lines.count() == 1) 48.sp else 20.sp,
                fontWeight = if (lines.count() == 1) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}