package eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.AppColor
import eu.anifantakis.ksafe_demo.core.presentation.design_system.UIConst
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppCard
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppPreview
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppText
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppTextStyle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun AppLabelCard(
    label: String,
    lines: ImmutableList<String>,
    modifier: Modifier = Modifier,
) {
    AppCard(modifier = modifier, bordered = true) {
        Column(
            modifier = Modifier
                .padding(UIConst.paddingSmall)
                .padding(horizontal = UIConst.screenHorizontalPadding),
        ) {
            AppText(label, AppTextStyle.BODY, color = AppColor.Muted)
            lines.forEach { line ->
                AppText(line, AppTextStyle.BODY)
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewAppLabelCard() {
    AppPreview {
        AppLabelCard(
            label = "AuthInfo",
            lines = persistentListOf(
                "accessToken: abc_3608",
                "expiresIn: 3608",
            ),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
