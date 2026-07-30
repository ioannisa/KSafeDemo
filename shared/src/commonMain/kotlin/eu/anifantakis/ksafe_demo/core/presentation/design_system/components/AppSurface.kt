package eu.anifantakis.ksafe_demo.core.presentation.design_system.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark

/**
 * Paints an opaque, full-size app background behind destination content.
 */
@Composable
fun AppSurface(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
        content = content,
    )
}

@PreviewLightDark
@Composable
private fun PreviewAppSurface() {
    AppPreview {
        AppSurface {
            AppText(
                text = "App surface",
                style = AppTextStyle.BODY,
            )
        }
    }
}
