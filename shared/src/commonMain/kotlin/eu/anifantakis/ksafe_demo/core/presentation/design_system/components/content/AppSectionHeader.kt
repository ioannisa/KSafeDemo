package eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.AppColor
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppPreview
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppText
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppTextStyle

@Composable
fun AppSectionHeader(
    text: String,
    modifier: Modifier = Modifier,
    textStyle: AppTextStyle = AppTextStyle.BODY,
) {
    AppText(
        text = text,
        style = textStyle,
        modifier = modifier,
        color = AppColor.Primary,
        fontWeight = FontWeight.Bold,
    )
}

@PreviewLightDark
@Composable
private fun PreviewAppSectionHeader() {
    AppPreview {
        AppSectionHeader(text = "Persistent counter")
    }
}
