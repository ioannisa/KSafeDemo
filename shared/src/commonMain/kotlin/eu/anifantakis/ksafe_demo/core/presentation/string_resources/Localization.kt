package eu.anifantakis.ksafe_demo.core.presentation.string_resources

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection

val LocalLanguage: ProvidableCompositionLocal<Language?> =
    compositionLocalOf { Language.FALLBACK }

object Strings {
    @Composable
    @ReadOnlyComposable
    operator fun get(key: StringKey): String {
        LocalLanguage.current
        return LocalizationManager.getString(key)
    }
}

fun StringKey.localized(): String = LocalizationManager.getString(this)

@Composable
@ReadOnlyComposable
fun StringKey.localizedCompose(): String {
    LocalLanguage.current
    return LocalizationManager.getString(this)
}

fun String.localized(): String {
    val localized = LocalizationManager.getString(StringKey.fromJson(this))
    return localized.ifEmpty { this }
}

fun String.withArgs(args: List<Any>): String =
    args.foldIndexed(this) { index, current, argument ->
        current.replace("{$index}", argument.toString())
    }

@Composable
fun LocalizationProvider(content: @Composable () -> Unit) {
    val language by LocalizationManager.currentLanguage.collectAsState()
    val layoutDirection =
        if (language.isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr

    CompositionLocalProvider(
        LocalLanguage provides language,
        LocalLayoutDirection provides layoutDirection,
        content = content,
    )
}

@Composable
fun rememberCurrentLanguage(): State<Language> =
    LocalizationManager.currentLanguage.collectAsState()
