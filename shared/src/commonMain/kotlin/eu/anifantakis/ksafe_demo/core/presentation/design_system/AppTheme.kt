package eu.anifantakis.ksafe_demo.core.presentation.design_system

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import eu.anifantakis.ksafe_demo.features.preferences.domain.model.ThemeMode

data class AppTypography(
    val material: Typography,
    val screenTitle: TextStyle,
    val compactScreenTitle: TextStyle,
    val largeScreenTitle: TextStyle,
    val sectionHeading: TextStyle,
    val sectionTitle: TextStyle,
    val stepTitle: TextStyle,
    val cardTitle: TextStyle,
    val body: TextStyle,
    val caption: TextStyle,
    val eyebrow: TextStyle,
    val small: TextStyle,
    val micro: TextStyle,
    val code: TextStyle,
    val value: TextStyle,
    val action: TextStyle,
    val smallAction: TextStyle,
    val largeAction: TextStyle,
    val navigationLabel: TextStyle,
)

@Immutable
data class AppColors(
    val surface: Color,
    val onSurface: Color,
    val muted: Color,
    val primary: Color,
    val success: Color,
    val warning: Color,
    val error: Color,
    val cardBorder: Color,
    val codeBackground: Color,
    val codeText: Color,
    val warmCodeBackground: Color,
    val warmCodeText: Color,
    val infoBackground: Color,
    val successBackground: Color,
    val warningBackground: Color,
    val errorBackground: Color,
    val hardwareIsolatedBackground: Color,
    val hardwareIsolatedAccent: Color,
)

private val LocalAppTypography = staticCompositionLocalOf<AppTypography> {
    error("No AppTypography provided. Wrap the content in KSafeDemoTheme.")
}

private val LocalAppColors = compositionLocalOf<AppColors> {
    error("No AppColors provided. Wrap the content in KSafeDemoTheme.")
}

object AppTheme {
    val typography: AppTypography
        @Composable get() = LocalAppTypography.current

    val colors: AppColors
        @Composable get() = LocalAppColors.current
}

/**
 * Compatibility palette used by feature screens. Values resolve through [AppTheme], so they
 * remain readable when the theme changes at runtime.
 */
object AppColor {
    val Surface: Color
        @Composable get() = AppTheme.colors.surface
    val OnSurface: Color
        @Composable get() = AppTheme.colors.onSurface
    val Muted: Color
        @Composable get() = AppTheme.colors.muted
    val Primary: Color
        @Composable get() = AppTheme.colors.primary
    val Success: Color
        @Composable get() = AppTheme.colors.success
    val Warning: Color
        @Composable get() = AppTheme.colors.warning
    val Error: Color
        @Composable get() = AppTheme.colors.error
    val CodeBackground: Color
        @Composable get() = AppTheme.colors.codeBackground
    val CodeText: Color
        @Composable get() = AppTheme.colors.codeText
    val WarmCodeBackground: Color
        @Composable get() = AppTheme.colors.warmCodeBackground
    val WarmCodeText: Color
        @Composable get() = AppTheme.colors.warmCodeText
    val InfoBackground: Color
        @Composable get() = AppTheme.colors.infoBackground
    val SuccessBackground: Color
        @Composable get() = AppTheme.colors.successBackground
    val WarningBackground: Color
        @Composable get() = AppTheme.colors.warningBackground
    val ErrorBackground: Color
        @Composable get() = AppTheme.colors.errorBackground
}

private val lightAppColors = AppColors(
    surface = Color.White,
    onSurface = Color(0xFF1A1A1A),
    muted = Color.Gray,
    primary = Color(0xFF1565C0),
    success = Color(0xFF2E7D32),
    warning = Color(0xFFFF9800),
    error = Color(0xFFC62828),
    cardBorder = Color.Black,
    codeBackground = Color(0xFFF0F0F5),
    codeText = Color(0xFF1A1A2E),
    warmCodeBackground = Color(0xFFFFF3E0),
    warmCodeText = Color(0xFF4E342E),
    infoBackground = Color(0xFFE3F2FD),
    successBackground = Color(0xFFE8F5E9),
    warningBackground = Color(0xFFFFF3E0),
    errorBackground = Color(0xFFFFEBEE),
    hardwareIsolatedBackground = Color(0xFFE0F7FA),
    hardwareIsolatedAccent = Color(0xFF00695C),
)

private val darkAppColors = AppColors(
    surface = Color(0xFF211F26),
    onSurface = Color(0xFFE6E1E5),
    muted = Color(0xFFCAC4D0),
    primary = Color(0xFF90CAF9),
    success = Color(0xFF81C784),
    warning = Color(0xFFFFB74D),
    error = Color(0xFFFF8A80),
    cardBorder = Color(0xFF938F99),
    codeBackground = Color(0xFF2B2930),
    codeText = Color(0xFFE6E1E5),
    warmCodeBackground = Color(0xFF3A2E20),
    warmCodeText = Color(0xFFFFCC80),
    infoBackground = Color(0xFF17324A),
    successBackground = Color(0xFF17351F),
    warningBackground = Color(0xFF402D16),
    errorBackground = Color(0xFF481E22),
    hardwareIsolatedBackground = Color(0xFF12383B),
    hardwareIsolatedAccent = Color(0xFF80CBC4),
)

@Composable
fun KSafeDemoTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit,
) {
    val isDark = themeMode.resolveDarkTheme(systemIsDark = isSystemInDarkTheme())
    val materialTypography = Typography()
    val appTypography = AppTypography(
        material = materialTypography,
        screenTitle = TextStyle(fontSize = 20.sp),
        compactScreenTitle = TextStyle(fontSize = 18.sp),
        largeScreenTitle = TextStyle(fontSize = 24.sp),
        sectionHeading = TextStyle(fontSize = 18.sp),
        sectionTitle = TextStyle(fontSize = 15.sp),
        stepTitle = TextStyle(fontSize = 13.sp),
        cardTitle = TextStyle(fontSize = 16.sp),
        body = TextStyle(fontSize = 14.sp),
        caption = TextStyle(fontSize = 12.sp),
        eyebrow = TextStyle(fontSize = 13.sp),
        small = TextStyle(fontSize = 11.sp),
        micro = TextStyle(fontSize = 10.sp),
        code = TextStyle(fontSize = 11.sp),
        value = TextStyle(fontSize = 26.sp),
        // Material components provide more than a color: their slot also provides the
        // expected label weight/letter spacing. Start from those Material roles and only
        // override the explicit sizes used by the original demo UI.
        action = materialTypography.labelLarge.copy(fontSize = 14.sp),
        smallAction = materialTypography.labelLarge.copy(fontSize = 12.sp),
        largeAction = materialTypography.labelLarge.copy(fontSize = 20.sp),
        navigationLabel = materialTypography.labelMedium,
    )

    androidx.compose.runtime.CompositionLocalProvider(
        LocalAppTypography provides appTypography,
        LocalAppColors provides if (isDark) darkAppColors else lightAppColors,
    ) {
        MaterialTheme(
            colorScheme = if (isDark) darkColorScheme() else lightColorScheme(),
            typography = materialTypography,
            content = content,
        )
    }
}

internal fun ThemeMode.resolveDarkTheme(systemIsDark: Boolean): Boolean = when (this) {
    ThemeMode.DAY -> false
    ThemeMode.NIGHT -> true
    ThemeMode.SYSTEM -> systemIsDark
}
