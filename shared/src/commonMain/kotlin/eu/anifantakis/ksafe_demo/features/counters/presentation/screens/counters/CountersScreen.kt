package eu.anifantakis.ksafe_demo.features.counters.presentation.screens.counters

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.AppColor
import eu.anifantakis.ksafe_demo.core.presentation.design_system.UIConst
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppButton
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppDialog
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppDivider
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppPreview
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppText
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppTextStyle
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content.AppLabelCard
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content.AppValueCard
import eu.anifantakis.ksafe_demo.features.counters.domain.model.AuthInfo
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.Strings
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.withArgs
import kotlinx.collections.immutable.persistentListOf
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CountersScreenRoot(
    viewModel: CountersViewModel = koinViewModel(),
) {
    CountersScreen(
        state = viewModel.state.value,
        onIntent = viewModel::onAction,
    )
}

@Composable
private fun CountersScreen(
    state: CountersState,
    onIntent: (CountersIntent) -> Unit,
) {
    var showClearDialog by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(rememberScrollState())
                .padding(
                    vertical = UIConst.screenVerticalPadding,
                    horizontal = UIConst.screenHorizontalPadding,
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(UIConst.paddingExtraSmall),
        ) {
            AppText(
                text = Strings[StringKey.COUNTERS_MUTABLE_STATE_NO_PERSISTENCE],
                style = AppTextStyle.EYEBROW,
            )
            AppValueCard(
                label = Strings[StringKey.COUNTERS_COUNTER_1],
                sublabel = Strings[StringKey.COUNTERS_PLAIN_STATE_RESETS],
                value = state.count1.toString(),
                modifier = Modifier.fillMaxWidth(0.5f),
            )

            Spacer(modifier = Modifier.height(UIConst.paddingExtraSmall))
            AppText(
                text = Strings[StringKey.COUNTERS_KSAFE_MUTABLE_STATE_PERSISTED],
                style = AppTextStyle.EYEBROW,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(UIConst.paddingCompact),
            ) {
                AppValueCard(
                    label = Strings[StringKey.COUNTERS_COUNTER_2],
                    sublabel = Strings[StringKey.COUNTERS_ENCRYPTED_OBSERVED_ON_FLOWS],
                    value = state.count2.toString(),
                    modifier = Modifier.weight(1f),
                )
                AppValueCard(
                    label = Strings[StringKey.COUNTERS_COUNTER_3],
                    sublabel = Strings[StringKey.COUNTERS_UNENCRYPTED],
                    value = state.count3.toString(),
                    modifier = Modifier.weight(1f),
                )
            }
            AppText(
                text = Strings[StringKey.COUNTERS_COUNTER_2_SYNC_DESCRIPTION],
                style = AppTextStyle.SMALL,
                color = AppColor.Primary,
            )
            AppText(
                text = Strings[StringKey.COUNTERS_COUNTER_2_REFRESH_DESCRIPTION],
                style = AppTextStyle.SMALL,
            )
            AppButton(
                label = Strings[StringKey.COUNTERS_REFRESH_COUNTER_2],
                onClick = { onIntent(CountersIntent.RefreshCount2) },
                textStyle = AppTextStyle.ACTION_SMALL,
            )

            Spacer(modifier = Modifier.height(UIConst.paddingExtraSmall))
            AppText(
                text = Strings[StringKey.COUNTERS_MUTABLE_STATE_FLOW_PERSISTED],
                style = AppTextStyle.EYEBROW,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(UIConst.paddingCompact),
            ) {
                AppValueCard(
                    label = Strings[StringKey.COUNTERS_COUNTER_2B],
                    sublabel = Strings[StringKey.COUNTERS_FLOW_NO_REFRESH],
                    value = state.count2b.toString(),
                    modifier = Modifier.weight(0.5f),
                )
                AppValueCard(
                    label = Strings[StringKey.COUNTERS_COUNTER_2C],
                    sublabel = Strings[StringKey.COUNTERS_FLOW_NO_REFRESH],
                    value = state.count2c.toString(),
                    modifier = Modifier.weight(0.5f),
                )
            }
            AppText(
                text = Strings[StringKey.COUNTERS_FLOW_EXPLANATION],
                style = AppTextStyle.SMALL,
            )

            Spacer(modifier = Modifier.height(UIConst.paddingExtraSmall))
            AppText(
                text = Strings[StringKey.COUNTERS_DATA_CLASS_PERSISTED],
                style = AppTextStyle.EYEBROW,
            )
            AppLabelCard(
                label = "AuthInfo",
                lines = persistentListOf(
                    "accessToken: ${state.authInfo.accessToken}",
                    "refreshToken: ${state.authInfo.refreshToken}",
                    "expiresIn: ${state.authInfo.expiresIn}",
                ),
                modifier = Modifier.fillMaxWidth(),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(UIConst.paddingSmall),
            ) {
                // "+" keeps its intrinsic width; the two text buttons split the rest equally.
                AppButton(
                    label = "+",
                    onClick = { onIntent(CountersIntent.Increment) },
                    textStyle = AppTextStyle.ACTION_LARGE,
                )
                AppButton(
                    label = Strings[StringKey.COMMON_CLEAR],
                    onClick = { showClearDialog = true },
                    modifier = Modifier.weight(1f),
                    textStyle = AppTextStyle.ACTION_LARGE,
                )
                AppButton(
                    label = Strings[StringKey.COUNTERS_BIOMETRIC_COUNT]
                        .withArgs(listOf(state.bioCount)),
                    onClick = { onIntent(CountersIntent.BiometricIncrement) },
                    modifier = Modifier.weight(1f),
                    textStyle = AppTextStyle.ACTION_LARGE,
                )
            }

            Spacer(modifier = Modifier.height(UIConst.paddingExtraSmall))
            AppText(
                text = Strings[StringKey.COUNTERS_HARDWARE_SECURED_VAULT],
                style = AppTextStyle.EYEBROW,
            )
            AppLabelCard(
                label = Strings[StringKey.COUNTERS_VAULT_TOKEN],
                lines = persistentListOf(
                    state.secureToken.ifEmpty {
                        Strings[StringKey.COUNTERS_NO_TOKEN_STORED]
                    },
                ),
                modifier = Modifier.fillMaxWidth(),
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(UIConst.paddingSmall),
            ) {
                AppButton(
                    label = Strings[StringKey.COUNTERS_GENERATE_TOKEN],
                    onClick = { onIntent(CountersIntent.GenerateToken) },
                    modifier = Modifier.weight(1f),
                )
                AppButton(
                    label = Strings[StringKey.COUNTERS_CLEAR_VAULT],
                    onClick = { onIntent(CountersIntent.ClearVault) },
                    modifier = Modifier.weight(1f),
                )
            }

            Spacer(modifier = Modifier.height(UIConst.paddingSmall))

            if (state.bioAuthRemaining > 0) {
                AppText(
                    text = Strings[StringKey.COUNTERS_BIO_AUTH_WINDOW]
                        .withArgs(listOf(state.bioAuthRemaining)),
                    style = AppTextStyle.EYEBROW,
                    fontWeight = FontWeight.Bold,
                    color = AppColor.Success,
                )
            }

            AppDivider(modifier = Modifier.padding(vertical = UIConst.paddingSmall))
            AppText(
                text = Strings[StringKey.COUNTERS_KEY_ROTATION],
                style = AppTextStyle.SECTION_TITLE,
                fontWeight = FontWeight.Bold,
            )
            AppText(
                text = Strings[StringKey.COUNTERS_KEY_ROTATION_DESCRIPTION],
                style = AppTextStyle.SMALL,
            )
            AppButton(
                label = Strings[
                    if (state.isRotating) {
                        StringKey.COUNTERS_ROTATING
                    } else {
                        StringKey.COUNTERS_ROTATE_KEYS
                    }
                ],
                onClick = { onIntent(CountersIntent.RotateKeys) },
                enabled = !state.isRotating,
            )

            AppDivider(modifier = Modifier.padding(vertical = UIConst.paddingSmall))
            AppText(
                text = Strings[StringKey.COUNTERS_LOCK_STATE_POLICY_TEST],
                style = AppTextStyle.SECTION_TITLE,
                fontWeight = FontWeight.Bold,
            )
            AppText(
                text = Strings[StringKey.COUNTERS_LOCK_TEST_INSTRUCTIONS],
                style = AppTextStyle.SMALL,
            )
            if (state.isLockTestRunning) {
                AppText(
                    text = if (state.lockTestCountdown > 0) {
                        Strings[StringKey.COUNTERS_LOCK_DEVICE_COUNTDOWN]
                            .withArgs(listOf(state.lockTestCountdown))
                    } else {
                        Strings[StringKey.COUNTERS_ATTEMPTING_ENCRYPTED_READ]
                    },
                    style = AppTextStyle.SECTION_TITLE,
                    fontWeight = FontWeight.Bold,
                    color = if (state.lockTestCountdown > 0) AppColor.Error else AppColor.Muted,
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(UIConst.paddingSmall),
                ) {
                    AppButton(
                        label = Strings[StringKey.COUNTERS_TEST_LOCK_DEFAULT],
                        onClick = { onIntent(CountersIntent.StartLockTest(false)) },
                        modifier = Modifier.weight(1f),
                    )
                    AppButton(
                        label = Strings[StringKey.COUNTERS_TEST_LOCK_HARDWARE],
                        onClick = { onIntent(CountersIntent.StartLockTest(true)) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }

    state.lockTestResult?.let { result ->
        AppDialog(
            title = Strings[StringKey.COUNTERS_LOCK_TEST_RESULT],
            text = result.asString(),
            confirmLabel = Strings[StringKey.COMMON_OK],
            onConfirm = { onIntent(CountersIntent.DismissLockTestResult) },
            onDismiss = { onIntent(CountersIntent.DismissLockTestResult) },
        )
    }
    state.rotationResult?.let { result ->
        AppDialog(
            title = Strings[StringKey.COUNTERS_KEY_ROTATION],
            text = result.asString(),
            confirmLabel = Strings[StringKey.COMMON_OK],
            onConfirm = { onIntent(CountersIntent.DismissRotationResult) },
            onDismiss = { onIntent(CountersIntent.DismissRotationResult) },
        )
    }
    if (showClearDialog) {
        AppDialog(
            title = Strings[StringKey.COUNTERS_CLEAR_ALL_TITLE],
            text = Strings[StringKey.COUNTERS_CLEAR_ALL_DESCRIPTION],
            confirmLabel = Strings[StringKey.COMMON_CLEAR],
            onConfirm = {
                onIntent(CountersIntent.Clear)
                showClearDialog = false
            },
            onDismiss = { showClearDialog = false },
            dismissLabel = Strings[StringKey.COMMON_CANCEL],
        )
    }
}

@PreviewLightDark
@Composable
private fun CountersScreenPreview() {
    AppPreview {
        CountersScreen(
            state = CountersState(
                bioCount = 5,
                authInfo = AuthInfo("abc_token", "ref_token", 9999),
                secureToken = "550e8400-e29b-41d4-a716-446655440000",
            ),
            onIntent = {},
        )
    }
}
