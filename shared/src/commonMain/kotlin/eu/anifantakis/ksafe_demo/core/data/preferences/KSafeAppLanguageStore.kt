package eu.anifantakis.ksafe_demo.core.data.preferences

import eu.anifantakis.ksafe_demo.core.domain.preferences.AppLanguageStore
import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeWriteMode
import eu.anifantakis.lib.ksafe.invoke

class KSafeAppLanguageStore(
    preferences: KSafe,
) : AppLanguageStore {
    override var languageCode: String by preferences(
        defaultValue = "",
        key = "app_language",
        mode = KSafeWriteMode.Plain,
    )
}
