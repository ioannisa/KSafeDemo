package eu.anifantakis.ksafe_demo.core.presentation.design_system.components

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark

@Composable
fun AppProgressIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(modifier = modifier)
}

@PreviewLightDark
@Composable
private fun PreviewAppProgressIndicator() {
    AppPreview {
        AppProgressIndicator()
    }
}
