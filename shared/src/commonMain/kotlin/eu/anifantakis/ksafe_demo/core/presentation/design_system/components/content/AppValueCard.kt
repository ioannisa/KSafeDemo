package eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.UIConst
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppCard
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppPreview
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppText
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppTextStyle

@Composable
fun AppValueCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    sublabel: String? = null,
) {
    AppCard(modifier = modifier, bordered = true) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(UIConst.paddingCompact),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            AppText(label, AppTextStyle.CAPTION)
            AppText(value, AppTextStyle.VALUE, fontWeight = FontWeight.Bold)
            sublabel?.let { AppText(it, AppTextStyle.MICRO) }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewAppValueCard() {
    AppPreview {
        AppValueCard(
            label = "Counter 2",
            value = "2008",
            sublabel = "encrypted",
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
