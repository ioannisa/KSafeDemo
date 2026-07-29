package eu.anifantakis.ksafe_demo.core.presentation.design_system.components

import androidx.compose.material3.RadioButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark

@Composable
fun AppRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    RadioButton(
        selected = selected,
        onClick = onClick,
        modifier = modifier.minimumInteractiveComponentSize(),
    )
}

@PreviewLightDark
@Composable
private fun PreviewAppRadioButton() {
    AppPreview {
        AppRadioButton(
            selected = true,
            onClick = {},
        )
    }
}
