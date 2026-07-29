package eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class AppLabelValue(
    val label: String,
    val value: String,
)

@Composable
fun AppDetailCard(
    title: String,
    titleColor: Color,
    containerColor: Color,
    details: ImmutableList<AppLabelValue>,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    subtitleColor: Color = AppColor.Error,
) {
    AppCard(
        modifier = modifier,
        containerColor = containerColor,
    ) {
        Column(modifier = Modifier.padding(UIConst.paddingRegular)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(UIConst.paddingSmall),
            ) {
                AppStatusDot(color = titleColor)
                AppText(
                    text = title,
                    style = AppTextStyle.CARD_TITLE,
                    color = titleColor,
                    fontWeight = FontWeight.Bold,
                )
            }
            subtitle?.let {
                AppText(
                    text = it,
                    style = AppTextStyle.CAPTION,
                    color = subtitleColor,
                    fontWeight = FontWeight.Bold,
                )
            }
            Spacer(modifier = Modifier.height(UIConst.paddingSmall))
            details.forEach { detail ->
                Row(modifier = Modifier.padding(vertical = UIConst.paddingExtraSmall)) {
                    AppText(
                        text = "${detail.label}: ",
                        style = AppTextStyle.EYEBROW,
                        fontWeight = FontWeight.Bold,
                    )
                    AppText(detail.value, AppTextStyle.EYEBROW)
                }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewAppDetailCard() {
    AppPreview {
        AppDetailCard(
            title = "HARDWARE_BACKED",
            titleColor = AppColor.Success,
            containerColor = AppColor.SuccessBackground,
            details = persistentListOf(
                AppLabelValue("Intended", "HARDWARE_BACKED"),
                AppLabelValue("Custody", "Android Keystore"),
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
