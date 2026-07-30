package eu.anifantakis.ksafe_demo

import eu.anifantakis.ksafe_demo.core.presentation.helper.UiText
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.Language
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.LocalizationManager
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.localized
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.withArgs
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LocalizationContractTest {
    @AfterTest
    fun restoreFallbackLanguage() {
        LocalizationManager.setLanguage(Language.FALLBACK)
    }

    @Test
    fun appShipsExactlyTheRequestedLanguages() {
        assertEquals(
            expected = listOf("en", "de", "it", "he", "fr", "el"),
            actual = Language.entries.map(Language::code),
        )
        assertEquals(
            expected = Language.entries.toSet(),
            actual = LocalizationManager.availableLanguages().toSet(),
        )
    }

    @Test
    fun onlyHebrewUsesRightToLeftLayout() {
        assertTrue(Language.HE.isRtl)
        Language.entries
            .filterNot { it == Language.HE }
            .forEach { language -> assertFalse(language.isRtl, language.code) }
    }

    @Test
    fun everyLanguageResolvesEveryUserFacingKey() {
        Language.entries.forEach { language ->
            LocalizationManager.setLanguage(language)
            StringKey.entries
                .filterNot { it == StringKey.UNMATCHED }
                .forEach { key ->
                    assertTrue(
                        key.localized().isNotBlank(),
                        "Missing ${language.code} translation for $key",
                    )
                }
        }
    }

    @Test
    fun savedLanguageWinsAndUnknownCodesFallBackSafely() {
        assertEquals(Language.DE, LocalizationManager.resolveStartup("de-DE"))
        assertEquals(Language.HE, Language.fromCode("he_IL"))
        assertEquals(null, Language.fromCode("ja"))
    }

    @Test
    fun positionalArgumentsWorkForDirectAndUiTextResolution() {
        LocalizationManager.setLanguage(Language.EN)

        assertEquals(
            "Presenting KSafe 3.1.0",
            StringKey.APP_PRESENTING_KSAFE.localized().withArgs(listOf("3.1.0")),
        )
        assertEquals(
            "Bio: 7",
            UiText.res(StringKey.COUNTERS_BIOMETRIC_COUNT, 7).resolve(),
        )
    }
}
