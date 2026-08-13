package eu.anifantakis.ksafe_demo.core.presentation.design_system.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.AppTheme
import eu.anifantakis.ksafe_demo.core.presentation.design_system.SyncSystemBarsWithTheme
import eu.anifantakis.ksafe_demo.core.presentation.design_system.UIConst

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppModalBottomSheet(
    title: String,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        // The sheet lives in its own (focused) window, which material3 styles only once at
        // creation — re-sync it here so a theme switched WHILE the sheet is open still flips
        // the status-bar icons. LocalView inside this lambda belongs to the sheet's window.
        SyncSystemBarsWithTheme(AppTheme.isDark)
        Column(modifier = Modifier.fillMaxWidth()) {
            AppText(
                text = title,
                style = AppTextStyle.SCREEN_TITLE_LARGE,
                modifier = Modifier.padding(horizontal = UIConst.screenHorizontalPadding),
            )
            content()
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewAppModalBottomSheet() {
    AppPreview {
        AppModalBottomSheet(
            title = "Preferences",
            onDismiss = {},
        ) {
            AppText(
                text = "Bottom sheet content",
                style = AppTextStyle.BODY,
                modifier = Modifier.padding(UIConst.paddingRegular),
            )
        }
    }
}
