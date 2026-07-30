package eu.anifantakis.ksafe_demo.core.presentation.design_system

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import ksafedemo.shared.generated.resources.Res
import ksafedemo.shared.generated.resources.compose_multiplatform
import org.jetbrains.compose.resources.painterResource

object AppDrawableRepo {
    val startupLogo: Painter
        @Composable get() = painterResource(Res.drawable.compose_multiplatform)

    val themeSystem: ImageVector
        @Composable get() = Icons.Outlined.Computer

    val themeDay: ImageVector
        @Composable get() = Icons.Outlined.LightMode

    val themeNight: ImageVector
        @Composable get() = Icons.Outlined.DarkMode

    val appMenu: ImageVector
        @Composable get() = Icons.Filled.MoreVert

    val preferences: ImageVector
        @Composable get() = Icons.Filled.Settings

    val about: ImageVector
        @Composable get() = Icons.Filled.Info

    val back: ImageVector
        @Composable get() = Icons.AutoMirrored.Filled.ArrowBack
}
