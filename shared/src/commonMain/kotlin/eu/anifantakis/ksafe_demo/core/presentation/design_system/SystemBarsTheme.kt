package eu.anifantakis.ksafe_demo.core.presentation.design_system

import androidx.compose.runtime.Composable

/**
 * Keeps the OS-drawn system bars (status-bar clock / battery / network icons, gesture bar)
 * legible when the in-app theme differs from the system one. Edge-to-edge defaults follow
 * the SYSTEM dark-mode flag, so picking NIGHT in-app on a light-mode device would leave
 * dark icons on the now-dark background. Android flips the icon appearance to match
 * [isDark]; the other platforms draw no in-window system bars, so they are no-ops.
 *
 * System-bar appearance is per WINDOW, and the focused window wins. The call in
 * [KSafeDemoTheme] styles only the Activity window; content shown in its own window
 * (dialogs, modal bottom sheets) must call this again from INSIDE that content, where
 * [androidx.compose.ui.platform.LocalView] belongs to the dialog window — material3 1.4
 * styles the sheet window once at creation and never follows a theme change.
 */
@Composable
internal expect fun SyncSystemBarsWithTheme(isDark: Boolean)
