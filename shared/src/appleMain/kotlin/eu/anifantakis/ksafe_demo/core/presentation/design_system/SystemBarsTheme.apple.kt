package eu.anifantakis.ksafe_demo.core.presentation.design_system

import androidx.compose.runtime.Composable

/** iOS/macOS status-bar styling is owned by UIKit/AppKit (Info.plist), not the window content. */
@Composable
internal actual fun SyncSystemBarsWithTheme(isDark: Boolean) = Unit
