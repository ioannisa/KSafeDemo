package eu.anifantakis.ksafe_demo.features.security.presentation.screens.security

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import eu.anifantakis.ksafe_demo.core.presentation.design_system.AppColor
import eu.anifantakis.ksafe_demo.core.presentation.design_system.AppTheme
import eu.anifantakis.ksafe_demo.core.presentation.design_system.KSafeDemoTheme
import eu.anifantakis.ksafe_demo.core.presentation.design_system.UIConst
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppButton
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppCard
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppText
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppTextStyle
import eu.anifantakis.lib.ksafe.KSafeProtectionInfo
import eu.anifantakis.lib.ksafe.KSafeProtectionLevel
import eu.anifantakis.lib.ksafe.SecurityViolation
import eu.anifantakis.lib.ksafe.compose.UiSecurityViolation
import kotlinx.collections.immutable.persistentListOf
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SecurityScreenRoot(
    viewModel: SecurityViewModel = koinViewModel(),
) {
    SecurityScreen(
        state = viewModel.state.value,
        onIntent = viewModel::onAction,
    )
}

@Composable
private fun SecurityScreen(
    state: SecurityState,
    onIntent: (SecurityIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(UIConst.paddingRegular),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(UIConst.paddingSmall),
    ) {
        AppText(
            text = "Security Status",
            style = AppTextStyle.SCREEN_TITLE_LARGE,
            fontWeight = FontWeight.Bold,
        )
        AppText(
            text = "KSafe security policy and key-custody demo",
            style = AppTextStyle.BODY,
        )

        StatusBadge(
            isSecure = state.violations.isEmpty(),
            violationCount = state.violations.size,
        )

        AppText(
            text = "Key Protection",
            style = AppTextStyle.SECTION_HEADING,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold,
        )
        KeyProtectionCard(info = state.protectionInfo)
        AppButton(
            label = "Refresh Security Status",
            onClick = { onIntent(SecurityIntent.Refresh) },
        )
        AppText(
            text = "Protection info is live and can change after key rotation or recovery.",
            style = AppTextStyle.SMALL,
        )

        Spacer(modifier = Modifier.height(UIConst.paddingSmall))
        AppText(
            text = "Security Checks",
            style = AppTextStyle.SECTION_HEADING,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold,
        )
        allViolationTypes.forEach { violationType ->
            SecurityCheckCard(
                title = violationTitle(violationType),
                description = violationDescription(violationType),
                isViolated = state.violations.any { it.violation == violationType },
            )
        }

        AppCard(
            modifier = Modifier.fillMaxWidth(),
            containerColor = AppColor.InfoBackground,
        ) {
            Column(modifier = Modifier.padding(UIConst.paddingRegular)) {
                AppText(
                    text = "Current Policy: WarnOnly",
                    style = AppTextStyle.SECTION_TITLE,
                    fontWeight = FontWeight.Bold,
                )
                AppText(
                    text = "The demo reports security issues without blocking functionality. " +
                        "Production apps handling sensitive data should choose a policy based " +
                        "on their threat model.",
                    style = AppTextStyle.BODY,
                )
            }
        }
    }
}

private val allViolationTypes = listOf(
    SecurityViolation.RootedDevice,
    SecurityViolation.DebuggerAttached,
    SecurityViolation.DebugBuild,
    SecurityViolation.Emulator,
)

@Composable
private fun KeyProtectionCard(info: KSafeProtectionInfo) {
    val degraded = info.effectiveLevel < info.intendedLevel
    val (containerColor, accentColor) = when (info.effectiveLevel) {
        KSafeProtectionLevel.SOFTWARE -> AppColor.ErrorBackground to AppColor.Error
        KSafeProtectionLevel.SANDBOX_PROTECTED -> AppColor.InfoBackground to AppColor.Primary
        KSafeProtectionLevel.HARDWARE_BACKED -> AppColor.SuccessBackground to AppColor.Success
        KSafeProtectionLevel.HARDWARE_ISOLATED ->
            AppTheme.colors.hardwareIsolatedBackground to AppTheme.colors.hardwareIsolatedAccent
    }
    AppCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = containerColor,
    ) {
        Column(modifier = Modifier.padding(UIConst.paddingRegular)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(UIConst.paddingSmall),
            ) {
                StatusDot(color = accentColor)
                AppText(
                    text = info.effectiveLevel.name,
                    style = AppTextStyle.CARD_TITLE,
                    color = accentColor,
                    fontWeight = FontWeight.Bold,
                )
            }
            if (degraded) {
                AppText(
                    text = "Degraded from ${info.intendedLevel.name}",
                    style = AppTextStyle.CAPTION,
                    color = AppColor.Error,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(UIConst.paddingSmall))
            ProtectionDetailRow("Intended", info.intendedLevel.name)
            ProtectionDetailRow("Effective", info.effectiveLevel.name)
            ProtectionDetailRow("Custody", info.custody)
            if (info.notes.isNotEmpty()) {
                ProtectionDetailRow("Notes", info.notes.joinToString())
            }
        }
    }
}

@Composable
private fun ProtectionDetailRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = UIConst.paddingExtraSmall)) {
        AppText(
            text = "$label: ",
            style = AppTextStyle.EYEBROW,
            fontWeight = FontWeight.Bold,
        )
        AppText(value, AppTextStyle.EYEBROW)
    }
}

@Composable
private fun StatusBadge(isSecure: Boolean, violationCount: Int) {
    val color = if (isSecure) AppColor.Success else AppColor.Error
    AppCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = if (isSecure) AppColor.SuccessBackground else AppColor.ErrorBackground,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(UIConst.paddingRegular),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            StatusDot(color)
            AppText(
                text = if (isSecure) {
                    "Secure Environment"
                } else {
                    "$violationCount Warning(s) Detected"
                },
                style = AppTextStyle.SECTION_HEADING,
                color = color,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = UIConst.paddingSmall),
            )
        }
    }
}

@Composable
private fun SecurityCheckCard(
    title: String,
    description: String,
    isViolated: Boolean,
) {
    val accent = if (isViolated) AppColor.Warning else AppColor.Success
    AppCard(
        modifier = Modifier.fillMaxWidth(),
        containerColor = if (isViolated) {
            AppColor.WarningBackground
        } else {
            AppColor.Surface
        },
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(UIConst.paddingRegular),
            verticalAlignment = Alignment.Top,
        ) {
            StatusDot(accent)
            Column(
                modifier = Modifier
                    .padding(start = UIConst.paddingRegular)
                    .weight(1f),
            ) {
                AppText(
                    text = title,
                    style = AppTextStyle.CARD_TITLE,
                    fontWeight = FontWeight.Bold,
                )
                AppText(
                    text = if (isViolated) "WARNING" else "OK",
                    style = AppTextStyle.SMALL,
                    color = accent,
                    fontWeight = FontWeight.Bold,
                )
                AppText(description, AppTextStyle.EYEBROW)
            }
        }
    }
}

@Composable
private fun StatusDot(color: Color) {
    Box(
        modifier = Modifier
            .size(UIConst.paddingRegular)
            .clip(CircleShape)
            .background(color),
    )
}

private fun violationTitle(violation: SecurityViolation): String = when (violation) {
    SecurityViolation.RootedDevice -> "Root/Jailbreak Detection"
    SecurityViolation.DebuggerAttached -> "Debugger Detection"
    SecurityViolation.DebugBuild -> "Debug Build Detection"
    SecurityViolation.Emulator -> "Emulator Detection"
}

private fun violationDescription(violation: SecurityViolation): String = when (violation) {
    SecurityViolation.RootedDevice ->
        "The device is rooted or jailbroken, which can weaken application sandboxing."

    SecurityViolation.DebuggerAttached ->
        "A debugger can inspect runtime memory, including values while they are decrypted."

    SecurityViolation.DebugBuild ->
        "Debug builds may expose more information and use weaker operational controls."

    SecurityViolation.Emulator ->
        "Emulators and simulators do not provide the same hardware-backed guarantees as devices."
}

private val previewProtectionInfo = KSafeProtectionInfo(
    intendedLevel = KSafeProtectionLevel.HARDWARE_BACKED,
    effectiveLevel = KSafeProtectionLevel.HARDWARE_BACKED,
    custody = "Android Keystore (TEE)",
    notes = emptyList(),
)

@Preview(showBackground = true)
@Composable
private fun PreviewSecurityScreen() {
    KSafeDemoTheme {
        SecurityScreen(
            state = SecurityState(
                protectionInfo = previewProtectionInfo,
                violations = persistentListOf(
                    UiSecurityViolation(SecurityViolation.DebugBuild),
                ),
            ),
            onIntent = {},
        )
    }
}
