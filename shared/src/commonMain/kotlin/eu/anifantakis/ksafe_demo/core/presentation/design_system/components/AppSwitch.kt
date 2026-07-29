package eu.anifantakis.ksafe_demo.core.presentation.design_system.components

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark

@Composable
fun AppSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
    )
}

@PreviewLightDark
@Composable
private fun PreviewAppSwitch() {
    AppPreview {
        AppSwitch(
            checked = true,
            onCheckedChange = {},
        )
    }
}
