package eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content

import androidx.compose.foundation.layout.Column
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
fun AppStatusCard(
    title: String,
    status: String,
    description: String,
    accentColor: Color,
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
            verticalAlignment = Alignment.Top,
        ) {
            AppStatusDot(color = accentColor)
            Column(
                modifier = Modifier
                    .padding(start = UIConst.paddingRegular)
                    .weight(1f),
            ) {
                AppText(
                    text = title,
                    style = AppTextStyle.CARD_TITLE,
                    fontWeight = FontWeight.Bold,
                )
                AppText(
                    text = status,
                    style = AppTextStyle.SMALL,
                    color = accentColor,
                    fontWeight = FontWeight.Bold,
                )
                AppText(description, AppTextStyle.EYEBROW)
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewAppStatusCard() {
    AppPreview {
        AppStatusCard(
            title = "Debug Build Detection",
            status = "WARNING",
            description = "Debug builds may expose more information.",
            accentColor = AppColor.Warning,
            containerColor = AppColor.WarningBackground,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
