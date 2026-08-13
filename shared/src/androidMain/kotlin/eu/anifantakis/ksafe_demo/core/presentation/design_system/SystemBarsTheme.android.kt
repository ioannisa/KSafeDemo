package eu.anifantakis.ksafe_demo.core.presentation.design_system

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.view.Window
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.window.DialogWindowProvider
import androidx.core.view.WindowCompat

@Composable
internal actual fun SyncSystemBarsWithTheme(isDark: Boolean) {
    val view = LocalView.current
    if (view.isInEditMode) return
    SideEffect {
        // Inside a dialog/sheet the FOCUSED window is the dialog's own, and that is the
        // window whose appearance the system bars follow — prefer it over the Activity's.
        val window = view.dialogWindow() ?: view.context.findActivity()?.window
            ?: return@SideEffect
        val controller = WindowCompat.getInsetsController(window, view)
        // "Light" here means light-colored BARS wanting dark icons — the inverse of a dark theme.
        controller.isAppearanceLightStatusBars = !isDark
        controller.isAppearanceLightNavigationBars = !isDark
    }
}

/** The window of the nearest enclosing dialog-backed compose host, if this view is in one. */
private fun View.dialogWindow(): Window? =
    generateSequence(parent) { (it as? View)?.parent }
        .filterIsInstance<DialogWindowProvider>()
        .firstOrNull()
        ?.window

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
