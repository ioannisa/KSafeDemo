package eu.anifantakis.ksafe_demo.core.domain.preferences

/**
 * Persists the user's chosen UI language as an ISO-639 code.
 *
 * An empty value means that no explicit choice has been made yet, so startup falls back to the
 * supported system language and then to English.
 */
interface AppLanguageStore {
    var languageCode: String
}
