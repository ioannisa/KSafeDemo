package eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.UIConst
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppCard
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppPreview
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppText
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppTextStyle

@Composable
fun AppProfileCard(
    label: String,
    name: String,
    createdAt: String,
    favoriteColor: String,
    favoriteColorSwatch: Color,
    modifier: Modifier = Modifier,
) {
    AppCard(modifier = modifier, bordered = true) {
        Column(
            modifier = Modifier
                .padding(UIConst.paddingSmall)
                .padding(horizontal = UIConst.screenHorizontalPadding),
        ) {
            AppText(label, AppTextStyle.CAPTION)
            AppText("name: $name", AppTextStyle.BODY)
            AppText("createdAt: $createdAt", AppTextStyle.BODY)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(UIConst.paddingSmall),
            ) {
                AppText("favoriteColor: $favoriteColor", AppTextStyle.BODY)
                Box(
                    modifier = Modifier
                        .size(UIConst.colorSwatchSize)
                        .clip(CircleShape)
                        .background(favoriteColorSwatch),
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewAppProfileCard() {
    AppPreview {
        AppProfileCard(
            label = "Profile",
            name = "Ada",
            createdAt = "2026-07-29",
            favoriteColor = "Purple",
            favoriteColorSwatch = Color(0xFF6D4CB3),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
