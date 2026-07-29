package eu.anifantakis.ksafe_demo.core.presentation.design_system.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.AppTheme
import eu.anifantakis.ksafe_demo.core.presentation.design_system.UIConst

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Unspecified,
    bordered: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    val resolvedContainerColor = when {
        containerColor != Color.Unspecified -> containerColor
        bordered -> AppTheme.colors.surface
        else -> Color.Unspecified
    }
    Card(
        modifier = modifier,
        shape = if (bordered) {
            RoundedCornerShape(UIConst.cornerRadius)
        } else {
            MaterialTheme.shapes.medium
        },
        colors = if (resolvedContainerColor == Color.Unspecified) {
            CardDefaults.cardColors()
        } else {
            CardDefaults.cardColors(containerColor = resolvedContainerColor)
        },
        border = if (bordered) {
            BorderStroke(UIConst.borderWidth, AppTheme.colors.cardBorder)
        } else {
            null
        },
        content = content,
    )
}

@PreviewLightDark
@Composable
private fun PreviewAppCard() {
    AppPreview {
        AppCard(bordered = true) {
            AppText(
                text = "Reusable card content",
                style = AppTextStyle.BODY,
                modifier = Modifier.padding(UIConst.paddingRegular),
            )
        }
    }
}
