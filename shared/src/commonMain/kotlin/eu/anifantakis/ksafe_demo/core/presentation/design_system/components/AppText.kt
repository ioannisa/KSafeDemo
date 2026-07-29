package eu.anifantakis.ksafe_demo.core.presentation.design_system.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.AppTheme

enum class AppTextStyle {
    SCREEN_TITLE,
    SCREEN_TITLE_COMPACT,
    SCREEN_TITLE_LARGE,
    SECTION_HEADING,
    SECTION_TITLE,
    STEP_TITLE,
    CARD_TITLE,
    BODY,
    CAPTION,
    EYEBROW,
    SMALL,
    MICRO,
    CODE,
    VALUE,
    ACTION,
    ACTION_SMALL,
    ACTION_LARGE,
    NAVIGATION_LABEL,
}

@Composable
fun AppText(
    text: String,
    style: AppTextStyle,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
) {
    val typography = AppTheme.typography
    val textStyle = when (style) {
        AppTextStyle.SCREEN_TITLE -> typography.screenTitle
        AppTextStyle.SCREEN_TITLE_COMPACT -> typography.compactScreenTitle
        AppTextStyle.SCREEN_TITLE_LARGE -> typography.largeScreenTitle
        AppTextStyle.SECTION_HEADING -> typography.sectionHeading
        AppTextStyle.SECTION_TITLE -> typography.sectionTitle
        AppTextStyle.STEP_TITLE -> typography.stepTitle
        AppTextStyle.CARD_TITLE -> typography.cardTitle
        AppTextStyle.BODY -> typography.body
        AppTextStyle.CAPTION -> typography.caption
        AppTextStyle.EYEBROW -> typography.eyebrow
        AppTextStyle.SMALL -> typography.small
        AppTextStyle.MICRO -> typography.micro
        AppTextStyle.CODE -> typography.code.copy(fontFamily = FontFamily.Monospace)
        AppTextStyle.VALUE -> typography.value
        AppTextStyle.ACTION -> typography.action
        AppTextStyle.ACTION_SMALL -> typography.smallAction
        AppTextStyle.ACTION_LARGE -> typography.largeAction
        AppTextStyle.NAVIGATION_LABEL -> typography.navigationLabel
    }
    val defaultColor = when (style) {
        AppTextStyle.CAPTION,
        AppTextStyle.EYEBROW,
        AppTextStyle.SMALL,
        AppTextStyle.MICRO -> AppTheme.colors.muted

        // Button/TextButton/NavigationBarItem provide their state-aware content color.
        // Keeping it unspecified preserves selected, disabled, light and dark behavior.
        AppTextStyle.ACTION,
        AppTextStyle.ACTION_SMALL,
        AppTextStyle.ACTION_LARGE,
        AppTextStyle.NAVIGATION_LABEL -> Color.Unspecified

        else -> AppTheme.colors.onSurface
    }

    Text(
        text = text,
        modifier = modifier,
        style = textStyle,
        color = if (color == Color.Unspecified) defaultColor else color,
        fontWeight = fontWeight,
    )
}

@PreviewLightDark
@Composable
private fun PreviewAppText() {
    AppPreview {
        AppText(
            text = "Semantic app text",
            style = AppTextStyle.SECTION_HEADING,
        )
    }
}
