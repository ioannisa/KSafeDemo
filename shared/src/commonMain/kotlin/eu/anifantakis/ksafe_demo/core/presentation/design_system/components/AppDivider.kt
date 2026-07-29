package eu.anifantakis.ksafe_demo.core.presentation.design_system.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.UIConst

@Composable
fun AppDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(modifier = modifier)
}

@PreviewLightDark
@Composable
private fun PreviewAppDivider() {
    AppPreview {
        AppDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(UIConst.paddingRegular),
        )
    }
}
