package eu.anifantakis.ksafe_demo.core.presentation.design_system.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark

@Composable
fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier.minimumInteractiveComponentSize(),
        enabled = enabled,
        content = content,
    )
}

@Composable
fun AppButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: AppTextStyle = AppTextStyle.ACTION,
) {
    AppButton(onClick = onClick, modifier = modifier, enabled = enabled) {
        AppText(text = label, style = textStyle)
    }
}

@PreviewLightDark
@Composable
private fun PreviewAppButton() {
    AppPreview {
        AppButton(
            label = "Continue",
            onClick = {},
        )
    }
}
