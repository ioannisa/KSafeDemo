package eu.anifantakis.ksafe_demo.features.storage.presentation.screens.storage

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
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppCard
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppDialog
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppDivider
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppPreview
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppText
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppTextStyle
import eu.anifantakis.ksafe_demo.features.storage.domain.model.AuthInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StorageScreenRoot(
    viewModel: StorageViewModel = koinViewModel(),
) {
    StorageScreen(
        state = viewModel.state.value,
        onIntent = viewModel::onAction,
    )
}

@Composable
private fun StorageScreen(
    state: StorageState,
    onIntent: (StorageIntent) -> Unit,
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
                text = "mutableStateOf (no persistence)",
                style = AppTextStyle.EYEBROW,
            )
            ValueCard(
                label = "Counter 1",
                sublabel = "plain state — resets on restart",
                value = state.count1.toString(),
                modifier = Modifier.fillMaxWidth(0.5f),
            )

            SectionGap()
            AppText(
                text = "ksafe.mutableStateOf (persisted)",
                style = AppTextStyle.EYEBROW,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(UIConst.paddingCompact),
            ) {
                ValueCard(
                    label = "Counter 2",
                    sublabel = "encrypted — observed on Flows tab",
                    value = state.count2.toString(),
                    modifier = Modifier.weight(1f),
                )
                ValueCard(
                    label = "Counter 3",
                    sublabel = "unencrypted",
                    value = state.count3.toString(),
                    modifier = Modifier.weight(1f),
                )
            }
            AppText(
                text = "Counter 2 is also observed on the Flows tab. Tap \"+\" here, then check " +
                    "Flows — the synced value updates in real-time.",
                style = AppTextStyle.SMALL,
                color = AppColor.Primary,
            )
            AppText(
                text = "If the Flows tab wrote to Counter 2, tap Refresh to see the latest value " +
                    "(no scope = manual refresh needed).",
                style = AppTextStyle.SMALL,
            )
            AppButton(
                label = "Refresh Counter 2",
                onClick = { onIntent(StorageIntent.RefreshCount2) },
                textStyle = AppTextStyle.ACTION_SMALL,
            )

            SectionGap()
            AppText(
                text = "ksafe.asMutableStateFlow (persisted, reactive)",
                style = AppTextStyle.EYEBROW,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(UIConst.paddingCompact),
            ) {
                ValueCard(
                    label = "Counter 2b",
                    sublabel = "MutableStateFlow — no refresh needed",
                    value = state.count2b.toString(),
                    modifier = Modifier.weight(0.5f),
                )
                ValueCard(
                    label = "Counter 2c",
                    sublabel = "MutableStateFlow — no refresh needed",
                    value = state.count2c.toString(),
                    modifier = Modifier.weight(0.5f),
                )
            }
            AppText(
                text = "Same storage, different shape: a MutableStateFlow instead of a Compose " +
                    "State. It takes a scope, so it subscribes to its key and picks up outside " +
                    "writes by itself — that is the button above that it doesn't need.",
                style = AppTextStyle.SMALL,
            )

            SectionGap()
            AppText(
                text = "ksafe.mutableStateOf — data class (persisted, encrypted)",
                style = AppTextStyle.EYEBROW,
            )
            LabelCard(
                label = "AuthInfo",
                lines = persistentListOf(
                    "accessToken: ${state.authInfo.accessToken}",
                    "refreshToken: ${state.authInfo.refreshToken}",
                    "expiresIn: ${state.authInfo.expiresIn}",
                ),
            )

            Row(horizontalArrangement = Arrangement.spacedBy(UIConst.paddingSmall)) {
                AppButton(
                    label = "+",
                    onClick = { onIntent(StorageIntent.Increment) },
                    textStyle = AppTextStyle.ACTION_LARGE,
                )
                AppButton(
                    label = "Clear",
                    onClick = { showClearDialog = true },
                    textStyle = AppTextStyle.ACTION_LARGE,
                )
                AppButton(
                    label = "Bio: ${state.bioCount}",
                    onClick = { onIntent(StorageIntent.BiometricIncrement) },
                    textStyle = AppTextStyle.ACTION_LARGE,
                )
            }

            SectionGap()
            AppText(
                text = "Hardware-Secured Vault (StrongBox / Secure Enclave)",
                style = AppTextStyle.EYEBROW,
            )
            LabelCard(
                label = "Vault Token",
                lines = persistentListOf(
                    state.secureToken.ifEmpty { "No token stored" },
                ),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(UIConst.paddingSmall)) {
                AppButton(
                    label = "Generate Token",
                    onClick = { onIntent(StorageIntent.GenerateToken) },
                )
                AppButton(
                    label = "Clear Vault",
                    onClick = { onIntent(StorageIntent.ClearVault) },
                )
            }

            Spacer(modifier = Modifier.height(UIConst.paddingSmall))

            if (state.bioAuthRemaining > 0) {
                AppText(
                    text = "Bio auth window open — no prompt for ${state.bioAuthRemaining}s",
                    style = AppTextStyle.EYEBROW,
                    fontWeight = FontWeight.Bold,
                    color = AppColor.Success,
                )
            }

            AppDivider(modifier = Modifier.padding(vertical = UIConst.paddingSmall))
            AppText(
                text = "Key Rotation",
                style = AppTextStyle.SECTION_TITLE,
                fontWeight = FontWeight.Bold,
            )
            AppText(
                text = "Re-encrypts every encrypted entry under a fresh key generation and " +
                    "drops the superseded keys. Whole-store — the values themselves never change, " +
                    "so the counters above stay exactly as they are.",
                style = AppTextStyle.SMALL,
            )
            AppButton(
                label = if (state.isRotating) "Rotating..." else "Rotate Keys",
                onClick = { onIntent(StorageIntent.RotateKeys) },
                enabled = !state.isRotating,
            )

            AppDivider(modifier = Modifier.padding(vertical = UIConst.paddingSmall))
            AppText(
                text = "Lock-State Policy Test",
                style = AppTextStyle.SECTION_TITLE,
                fontWeight = FontWeight.Bold,
            )
            AppText(
                text = "Run from Home Screen, not Xcode, for accurate results",
                style = AppTextStyle.SMALL,
            )
            if (state.isLockTestRunning) {
                AppText(
                    text = if (state.lockTestCountdown > 0) {
                        "Lock your device now! Reading in ${state.lockTestCountdown}s..."
                    } else {
                        "Attempting encrypted read..."
                    },
                    style = AppTextStyle.SECTION_TITLE,
                    fontWeight = FontWeight.Bold,
                    color = if (state.lockTestCountdown > 0) AppColor.Error else AppColor.Muted,
                )
            } else {
                Row(horizontalArrangement = Arrangement.spacedBy(UIConst.paddingSmall)) {
                    AppButton(
                        label = "Test Lock (Default)",
                        onClick = { onIntent(StorageIntent.StartLockTest(false)) },
                    )
                    AppButton(
                        label = "Test Lock (Hardware)",
                        onClick = { onIntent(StorageIntent.StartLockTest(true)) },
                    )
                }
            }
        }
    }

    state.lockTestResult?.let { result ->
        AppDialog(
            title = "Lock Test Result",
            text = result,
            confirmLabel = "OK",
            onConfirm = { onIntent(StorageIntent.DismissLockTestResult) },
            onDismiss = { onIntent(StorageIntent.DismissLockTestResult) },
        )
    }
    state.rotationResult?.let { result ->
        AppDialog(
            title = "Key Rotation",
            text = result,
            confirmLabel = "OK",
            onConfirm = { onIntent(StorageIntent.DismissRotationResult) },
            onDismiss = { onIntent(StorageIntent.DismissRotationResult) },
        )
    }
    if (showClearDialog) {
        AppDialog(
            title = "Clear all values?",
            text = "This will delete all stored keys and reset counters to their defaults.",
            confirmLabel = "Clear",
            onConfirm = {
                onIntent(StorageIntent.Clear)
                showClearDialog = false
            },
            onDismiss = { showClearDialog = false },
            dismissLabel = "Cancel",
        )
    }
}

@Composable
private fun ValueCard(
    label: String,
    sublabel: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    AppCard(modifier = modifier, bordered = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(UIConst.paddingCompact),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AppText(label, AppTextStyle.CAPTION)
            AppText(value, AppTextStyle.VALUE, fontWeight = FontWeight.Bold)
            AppText(sublabel, AppTextStyle.MICRO)
        }
    }
}

@Composable
private fun LabelCard(
    label: String,
    lines: ImmutableList<String>,
) {
    AppCard(modifier = Modifier.fillMaxWidth(), bordered = true) {
        Column(
            modifier = Modifier
                .padding(UIConst.paddingSmall)
                .padding(horizontal = UIConst.screenHorizontalPadding),
        ) {
            AppText(label, AppTextStyle.BODY, color = AppColor.Muted)
            lines.forEach { line ->
                AppText(line, AppTextStyle.BODY)
            }
        }
    }
}

@Composable
private fun SectionGap() {
    Spacer(modifier = Modifier.height(UIConst.paddingExtraSmall))
}

@PreviewLightDark
@Composable
private fun StorageScreenPreview() {
    AppPreview {
        StorageScreen(
            state = StorageState(
                bioCount = 5,
                authInfo = AuthInfo("abc_token", "ref_token", 9999),
                secureToken = "550e8400-e29b-41d4-a716-446655440000",
            ),
            onIntent = {},
        )
    }
}
