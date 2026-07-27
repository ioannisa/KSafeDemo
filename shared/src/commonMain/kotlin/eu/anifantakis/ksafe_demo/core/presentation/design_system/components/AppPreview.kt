package eu.anifantakis.ksafe_demo.core.presentation.design_system.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import eu.anifantakis.ksafe_demo.core.presentation.design_system.KSafeDemoTheme

/**
 * Hosts standalone screen previews in the app theme and paints the same root background that
 * [androidx.compose.material3.Scaffold] provides when the screen runs inside the application.
 */
@Composable
internal fun AppPreview(content: @Composable () -> Unit) {
    KSafeDemoTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background,
            content = content,
        )
    }
}
