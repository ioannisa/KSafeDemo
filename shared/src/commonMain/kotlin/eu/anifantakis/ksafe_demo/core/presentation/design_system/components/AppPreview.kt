package eu.anifantakis.ksafe_demo.core.presentation.design_system.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.KSafeDemoTheme
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.LocalizationProvider

/**
 * Hosts standalone screen previews in the app theme and paints the same root background that
 * [androidx.compose.material3.Scaffold] provides when the screen runs inside the application.
 */
@Composable
internal fun AppPreview(content: @Composable () -> Unit) {
    LocalizationProvider {
        KSafeDemoTheme {
            AppSurface(content = content)
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewAppPreview() {
    AppPreview {
        AppText(
            text = "Preview surface",
            style = AppTextStyle.BODY,
        )
    }
}
