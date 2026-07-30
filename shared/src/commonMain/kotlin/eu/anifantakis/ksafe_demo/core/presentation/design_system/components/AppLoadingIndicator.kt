package eu.anifantakis.ksafe_demo.core.presentation.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.Strings

@Composable
fun AppLoadingIndicator(
    isLoading: Boolean,
    modifier: Modifier = Modifier,
) {
    if (!isLoading) return
    val processingDescription = Strings[StringKey.COMMON_PROCESSING]

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.24f))
            .pointerInput(Unit) { detectTapGestures { } }
            .semantics { contentDescription = processingDescription },
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@PreviewLightDark
@Composable
private fun PreviewAppLoadingIndicator() {
    AppPreview {
        AppLoadingIndicator(isLoading = true)
    }
}
