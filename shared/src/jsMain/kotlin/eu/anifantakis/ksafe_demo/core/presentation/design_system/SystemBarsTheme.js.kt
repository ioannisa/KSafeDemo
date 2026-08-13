package eu.anifantakis.ksafe_demo.core.presentation.design_system

import androidx.compose.runtime.Composable

/** Browser chrome belongs to the browser; there are no in-window system bars to restyle. */
@Composable
internal actual fun SyncSystemBarsWithTheme(isDark: Boolean) = Unit
