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
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.Strings
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.withArgs
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
        AppLabelValue(
            Strings[StringKey.SECURITY_INTENDED],
            protectionInfo.intendedLevel.name,
        ),
        AppLabelValue(
            Strings[StringKey.SECURITY_EFFECTIVE],
            protectionInfo.effectiveLevel.name,
        ),
        AppLabelValue(
            Strings[StringKey.SECURITY_CUSTODY],
            protectionInfo.custody,
        ),
    ).let { details ->
        if (protectionInfo.notes.isEmpty()) {
            details
        } else {
            details.adding(
                AppLabelValue(
                    Strings[StringKey.SECURITY_NOTES],
                    protectionInfo.notes.joinToString(),
                ),
            )
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
            text = Strings[StringKey.SECURITY_TITLE],
            style = AppTextStyle.SCREEN_TITLE_LARGE,
            fontWeight = FontWeight.Bold,
        )
        AppText(
            text = Strings[StringKey.SECURITY_SUBTITLE],
            style = AppTextStyle.BODY,
        )

        AppStatusBanner(
            text = if (state.violations.isEmpty()) {
                Strings[StringKey.SECURITY_SECURE_ENVIRONMENT]
            } else {
                Strings[StringKey.SECURITY_WARNINGS_DETECTED]
                    .withArgs(listOf(state.violations.size))
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
            text = Strings[StringKey.SECURITY_KEY_PROTECTION],
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
                Strings[StringKey.SECURITY_DEGRADED_FROM]
                    .withArgs(listOf(protectionInfo.intendedLevel.name))
            } else {
                null
            },
            modifier = Modifier.fillMaxWidth(),
        )
        AppButton(
            label = Strings[StringKey.SECURITY_REFRESH_STATUS],
            onClick = { onIntent(SecurityIntent.Refresh) },
        )
        AppText(
            text = Strings[StringKey.SECURITY_LIVE_INFO],
            style = AppTextStyle.SMALL,
        )

        Spacer(modifier = Modifier.height(UIConst.paddingSmall))
        AppText(
            text = Strings[StringKey.SECURITY_CHECKS],
            style = AppTextStyle.SECTION_HEADING,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold,
        )
        allViolationTypes.forEach { violationType ->
            val isViolated = state.violations.any { it.violation == violationType }
            AppStatusCard(
                title = Strings[violationTitleKey(violationType)],
                status = Strings[
                    if (isViolated) StringKey.SECURITY_WARNING else StringKey.SECURITY_OK
                ],
                description = Strings[violationDescriptionKey(violationType)],
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
                    text = Strings[StringKey.SECURITY_CURRENT_POLICY]
                        .withArgs(listOf("WarnOnly")),
                    style = AppTextStyle.SECTION_TITLE,
                    fontWeight = FontWeight.Bold,
                )
                AppText(
                    text = Strings[StringKey.SECURITY_POLICY_DESCRIPTION],
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

private fun violationTitleKey(violation: SecurityViolation): StringKey = when (violation) {
    SecurityViolation.RootedDevice -> StringKey.SECURITY_ROOTED_TITLE
    SecurityViolation.DebuggerAttached -> StringKey.SECURITY_DEBUGGER_TITLE
    SecurityViolation.DebugBuild -> StringKey.SECURITY_DEBUG_BUILD_TITLE
    SecurityViolation.Emulator -> StringKey.SECURITY_EMULATOR_TITLE
}

private fun violationDescriptionKey(violation: SecurityViolation): StringKey = when (violation) {
    SecurityViolation.RootedDevice -> StringKey.SECURITY_ROOTED_DESCRIPTION
    SecurityViolation.DebuggerAttached -> StringKey.SECURITY_DEBUGGER_DESCRIPTION
    SecurityViolation.DebugBuild -> StringKey.SECURITY_DEBUG_BUILD_DESCRIPTION
    SecurityViolation.Emulator -> StringKey.SECURITY_EMULATOR_DESCRIPTION
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
