package eu.anifantakis.ksafe_demo.core.presentation.string_resources

import androidx.compose.ui.text.intl.Locale
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.lang.De
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.lang.El
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.lang.En
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.lang.Fr
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.lang.He
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.lang.It
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object LocalizationManager {
    private val providers: Map<Language, LanguageStrings> = mapOf(
        Language.EN to En(),
        Language.DE to De(),
        Language.IT to It(),
        Language.HE to He(),
        Language.FR to Fr(),
        Language.EL to El(),
    )

    private val _currentLanguage = MutableStateFlow(Language.FALLBACK)
    val currentLanguage: StateFlow<Language> = _currentLanguage.asStateFlow()
    val current: Language get() = _currentLanguage.value

    fun availableLanguages(): List<Language> = providers.keys.toList()

    fun setLanguage(language: Language) {
        _currentLanguage.value = language
    }

    fun resolveStartup(savedCode: String): Language =
        Language.fromCode(savedCode.ifEmpty { null })
            ?: Language.fromCode(Locale.current.language)
            ?: Language.FALLBACK

    fun getString(key: StringKey): String =
        (providers[_currentLanguage.value] ?: providers.getValue(Language.FALLBACK)).getString(key)
}
