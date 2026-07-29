package eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.AppColor
import eu.anifantakis.ksafe_demo.core.presentation.design_system.UIConst
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppCard
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppPreview
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppText
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppTextStyle

@Composable
fun AppStatusBanner(
    text: String,
    contentColor: Color,
    containerColor: Color,
    modifier: Modifier = Modifier,
) {
    AppCard(
        modifier = modifier,
        containerColor = containerColor,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(UIConst.paddingRegular),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            AppStatusDot(color = contentColor)
            AppText(
                text = text,
                style = AppTextStyle.SECTION_HEADING,
                color = contentColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = UIConst.paddingSmall),
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewAppStatusBanner() {
    AppPreview {
        AppStatusBanner(
            text = "1 Warning Detected",
            contentColor = AppColor.Error,
            containerColor = AppColor.ErrorBackground,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
