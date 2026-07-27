package eu.anifantakis.ksafe_demo.features.preferences.domain.repository

import eu.anifantakis.ksafe_demo.features.preferences.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

interface ThemePreferenceRepository {
    val themeMode: Flow<ThemeMode>

    fun setThemeMode(themeMode: ThemeMode)
}
