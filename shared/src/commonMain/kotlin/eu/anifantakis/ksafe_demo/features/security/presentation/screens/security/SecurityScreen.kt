package eu.anifantakis.ksafe_demo.features.security.presentation.screens.security

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.AppColor
import eu.anifantakis.ksafe_demo.core.presentation.design_system.AppTheme
import eu.anifantakis.ksafe_demo.core.presentation.design_system.UIConst
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppButton
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppCard
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppPreview
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppText
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppTextStyle
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content.AppDetailCard
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content.AppLabelValue
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content.AppStatusBanner
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content.AppStatusCard
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
    val protectionInfo = state.protectionInfo
    val protectionDegraded = protectionInfo.effectiveLevel < protectionInfo.intendedLevel
    val (protectionContainerColor, protectionAccentColor) =
        when (protectionInfo.effectiveLevel) {
            KSafeProtectionLevel.SOFTWARE -> AppColor.ErrorBackground to AppColor.Error
            KSafeProtectionLevel.SANDBOX_PROTECTED -> AppColor.InfoBackground to AppColor.Primary
            KSafeProtectionLevel.HARDWARE_BACKED ->
                AppColor.SuccessBackground to AppColor.Success

            KSafeProtectionLevel.HARDWARE_ISOLATED ->
                AppTheme.colors.hardwareIsolatedBackground to AppTheme.colors.hardwareIsolatedAccent
        }
    val protectionDetails = persistentListOf(
        AppLabelValue("Intended", protectionInfo.intendedLevel.name),
        AppLabelValue("Effective", protectionInfo.effectiveLevel.name),
        AppLabelValue("Custody", protectionInfo.custody),
    ).let { details ->
        if (protectionInfo.notes.isEmpty()) {
            details
        } else {
            details.adding(AppLabelValue("Notes", protectionInfo.notes.joinToString()))
        }
    }

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

        AppStatusBanner(
            text = if (state.violations.isEmpty()) {
                "Secure Environment"
            } else {
                "${state.violations.size} Warning(s) Detected"
            },
            contentColor = if (state.violations.isEmpty()) AppColor.Success else AppColor.Error,
            containerColor = if (state.violations.isEmpty()) {
                AppColor.SuccessBackground
            } else {
                AppColor.ErrorBackground
            },
            modifier = Modifier.fillMaxWidth(),
        )

        AppText(
            text = "Key Protection",
            style = AppTextStyle.SECTION_HEADING,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold,
        )
        AppDetailCard(
            title = protectionInfo.effectiveLevel.name,
            titleColor = protectionAccentColor,
            containerColor = protectionContainerColor,
            details = protectionDetails,
            subtitle = if (protectionDegraded) {
                "Degraded from ${protectionInfo.intendedLevel.name}"
            } else {
                null
            },
            modifier = Modifier.fillMaxWidth(),
        )
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
            val isViolated = state.violations.any { it.violation == violationType }
            AppStatusCard(
                title = violationTitle(violationType),
                status = if (isViolated) "WARNING" else "OK",
                description = violationDescription(violationType),
                accentColor = if (isViolated) AppColor.Warning else AppColor.Success,
                containerColor = if (isViolated) AppColor.WarningBackground else AppColor.Surface,
                modifier = Modifier.fillMaxWidth(),
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

@PreviewLightDark
@Composable
private fun PreviewSecurityScreen() {
    AppPreview {
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
