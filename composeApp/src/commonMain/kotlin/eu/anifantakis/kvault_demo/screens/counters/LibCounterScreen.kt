package eu.anifantakis.kvault_demo.screens.counters

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            Text(text = "mutableStateOf", fontSize = 18.sp)
            LabelCard(label = "Counter 1:", content = "${counterViewModel.count1}")

            Text(text = "mutableStateOf - KVault - Encrypted", fontSize = 18.sp)
            LabelCard(label = "Counter 2:", content = "${counterViewModel.count2}")

            Text(text = "mutableStateOf - KVault - Not Encrypted", fontSize = 18.sp)
            LabelCard(label = "Counter 3:", content = "${counterViewModel.count3}")

            Spacer(modifier = Modifier.padding(16.dp))

            Button(onClick = { counterViewModel.increment() }) {
                Text(text = "Increment", fontSize = 24.sp)
            }
        }
    }
}

@Composable
fun LabelCard(
    label: String,
    content: String,
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
            .padding(16.dp)
            .padding(horizontal = 48.dp)
    ) {
        Text(
            text = label,
            fontSize = 24.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = content,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
    }
}