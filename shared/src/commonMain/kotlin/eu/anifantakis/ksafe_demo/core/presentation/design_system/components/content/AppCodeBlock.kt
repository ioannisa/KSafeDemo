package eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.AppColor
import eu.anifantakis.ksafe_demo.core.presentation.design_system.UIConst
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppPreview
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppText
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppTextStyle

enum class AppCodeBlockStyle {
    DEFAULT,
    WARM,
}

@Composable
fun AppCodeBlock(
    code: String,
    modifier: Modifier = Modifier,
    style: AppCodeBlockStyle = AppCodeBlockStyle.DEFAULT,
) {
    val (backgroundColor, textColor) = when (style) {
        AppCodeBlockStyle.DEFAULT -> AppColor.CodeBackground to AppColor.CodeText
        AppCodeBlockStyle.WARM -> AppColor.WarmCodeBackground to AppColor.WarmCodeText
    }
    AppText(
        text = code,
        style = AppTextStyle.CODE,
        color = textColor,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(UIConst.cornerRadius),
            )
            .padding(UIConst.paddingSmall),
    )
}

@PreviewLightDark
@Composable
private fun PreviewAppCodeBlock() {
    AppPreview {
        AppCodeBlock(code = "var counter by ksafe.mutableStateOf(0)")
    }
}
