package eu.anifantakis.ksafe_demo.core.presentation.design_system.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark

@Composable
fun AppDialog(
    title: String,
    text: String,
    confirmLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    dismissLabel: String? = null,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { AppText(title, AppTextStyle.SCREEN_TITLE_LARGE) },
        text = { AppText(text, AppTextStyle.BODY) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                AppText(confirmLabel, AppTextStyle.ACTION)
            }
        },
        dismissButton = dismissLabel?.let { label ->
            {
                TextButton(onClick = onDismiss) {
                    AppText(label, AppTextStyle.ACTION)
                }
            }
        },
    )
}

@PreviewLightDark
@Composable
private fun PreviewAppDialog() {
    AppPreview {
        AppDialog(
            title = "Clear stored values?",
            text = "This action cannot be undone.",
            confirmLabel = "Clear",
            dismissLabel = "Cancel",
            onConfirm = {},
            onDismiss = {},
        )
    }
}
