package eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.UIConst
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppDivider
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppPreview

@Composable
fun AppSectionDivider(modifier: Modifier = Modifier) {
    AppDivider(modifier = modifier.padding(vertical = UIConst.paddingSmall))
}

@PreviewLightDark
@Composable
private fun PreviewAppSectionDivider() {
    AppPreview {
        AppSectionDivider()
    }
}
