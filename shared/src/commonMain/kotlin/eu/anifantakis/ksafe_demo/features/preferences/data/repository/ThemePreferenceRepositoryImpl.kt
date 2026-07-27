package eu.anifantakis.ksafe_demo.features.preferences.data.repository

import eu.anifantakis.ksafe_demo.features.preferences.domain.model.ThemeMode
import eu.anifantakis.ksafe_demo.features.preferences.domain.repository.ThemePreferenceRepository
import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeWriteMode
import eu.anifantakis.lib.ksafe.WritableKSafeFlow
import eu.anifantakis.lib.ksafe.asWritableFlow
import kotlinx.coroutines.flow.Flow

class ThemePreferenceRepositoryImpl(
    preferences: KSafe,
) : ThemePreferenceRepository {
    private val persistedThemeMode: WritableKSafeFlow<ThemeMode> by preferences.asWritableFlow(
        defaultValue = ThemeMode.SYSTEM,
        key = "theme_mode",
        mode = KSafeWriteMode.Plain,
    )

    override val themeMode: Flow<ThemeMode>
        get() = persistedThemeMode

    override fun setThemeMode(themeMode: ThemeMode) {
        persistedThemeMode.set(themeMode)
    }
}
