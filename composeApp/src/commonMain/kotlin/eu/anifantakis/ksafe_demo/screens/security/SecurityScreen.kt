package eu.anifantakis.ksafe_demo.screens.security

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import eu.anifantakis.lib.ksafe.SecurityViolation
import eu.anifantakis.lib.ksafe.compose.UiSecurityViolation
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SecurityScreen(
    viewModel: SecurityViewModel = koinViewModel()
) {
    SecurityScreenContent(
        violations = viewModel.violations.toImmutableList(),
        violationDescriptionProvider = viewModel::getViolationDescription
    )
}

@Composable
fun SecurityScreenContent(
    violations: ImmutableList<UiSecurityViolation>,
    violationDescriptionProvider: (SecurityViolation) -> String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Security Status",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "KSafe 1.4.0 Security Policy Demo",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Overall status
        val isSecure = violations.isEmpty()
        StatusBadge(
            isSecure = isSecure,
            violationCount = violations.size
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Security checks header
        Text(
            text = "Security Checks",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        // Define the list of checks we want to display
        val allViolationTypes = remember {
            listOf(
                SecurityViolation.RootedDevice,
                SecurityViolation.DebuggerAttached,
                SecurityViolation.DebugBuild,
                SecurityViolation.Emulator
            )
        }

        // Render the list
        allViolationTypes.forEach { violationType ->
            // Check if this specific type is in our violations list
            // We need to unwrap the UiSecurityViolation to check the type
            val isViolated = violations.any { it.violation == violationType }

            SecurityCheckCard(
                title = getViolationTitle(violationType),
                description = violationDescriptionProvider(violationType),
                isViolated = isViolated
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Policy info footer
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE3F2FD)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Current Policy: WarnOnly",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "This demo uses the WarnOnly policy which detects security issues " +
                            "and logs them, but doesn't block app functionality. " +
                            "For production apps handling sensitive data, consider using " +
                            "the Strict policy.",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

// --- Helper Composables (Unchanged) ---

@Composable
private fun StatusBadge(isSecure: Boolean, violationCount: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSecure) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(if (isSecure) Color(0xFF4CAF50) else Color(0xFFF44336))
            )
            Text(
                text = if (isSecure) "  Secure Environment" else "  $violationCount Warning(s) Detected",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isSecure) Color(0xFF2E7D32) else Color(0xFFC62828)
            )
        }
    }
}

@Composable
private fun SecurityCheckCard(
    title: String,
    description: String,
    isViolated: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isViolated) Color(0xFFFFF3E0) else Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Status indicator dot
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(
                        if (isViolated) Color(0xFFFF9800) else Color(0xFF4CAF50)
                    )
            )

            Column(
                modifier = Modifier
                    .padding(start = 12.dp)
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = if (isViolated) Color(0xFFE65100) else Color.Black
                )
                Text(
                    text = if (isViolated) "WARNING" else "OK",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isViolated) Color(0xFFFF9800) else Color(0xFF4CAF50)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

private fun getViolationTitle(violation: SecurityViolation): String {
    return when (violation) {
        SecurityViolation.RootedDevice -> "Root/Jailbreak Detection"
        SecurityViolation.DebuggerAttached -> "Debugger Detection"
        SecurityViolation.DebugBuild -> "Debug Build Detection"
        SecurityViolation.Emulator -> "Emulator Detection"
    }
}

// --- Preview Section ---

@Preview(showBackground = true, name = "Secure State")
@Composable
fun PreviewSecurityScreenSecure() {
    SecurityScreenContent(
        violations = persistentListOf(),
        violationDescriptionProvider = { "Mock description for preview" }
    )
}

@Preview(showBackground = true, name = "Violated State")
@Composable
fun PreviewSecurityScreenViolated() {
    SecurityScreenContent(
        violations = persistentListOf(
            UiSecurityViolation(SecurityViolation.RootedDevice),
            UiSecurityViolation(SecurityViolation.Emulator)
        ),
        violationDescriptionProvider = { "Mock description for preview" }
    )
}