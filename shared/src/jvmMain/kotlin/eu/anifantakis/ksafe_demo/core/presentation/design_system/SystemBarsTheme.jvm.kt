package eu.anifantakis.ksafe_demo.core.presentation.design_system

import androidx.compose.runtime.Composable

/** Desktop window chrome belongs to the OS; there are no in-window system bars to restyle. */
@Composable
internal actual fun SyncSystemBarsWithTheme(isDark: Boolean) = Unit
