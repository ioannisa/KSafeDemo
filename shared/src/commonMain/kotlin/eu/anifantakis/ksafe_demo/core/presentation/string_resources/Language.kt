package eu.anifantakis.ksafe_demo.core.presentation.string_resources

enum class Language(
    val code: String,
    val displayName: String,
    val isRtl: Boolean = false,
) {
    EN("en", "English"),
    DE("de", "Deutsch"),
    IT("it", "Italiano"),
    HE("he", "עברית", isRtl = true),
    FR("fr", "Français"),
    EL("el", "Ελληνικά");

    companion object {
        val FALLBACK: Language = EN

        fun fromCode(code: String?): Language? =
            code
                ?.lowercase()
                ?.take(2)
                ?.let { normalizedCode ->
                    entries.find { language -> language.code == normalizedCode }
                }
    }
}
