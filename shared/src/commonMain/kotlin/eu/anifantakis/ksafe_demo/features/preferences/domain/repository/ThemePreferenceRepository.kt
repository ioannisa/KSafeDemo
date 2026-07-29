package eu.anifantakis.ksafe_demo.features.preferences.domain.repository

import androidx.compose.runtime.Immutable
import eu.anifantakis.ksafe_demo.features.preferences.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

@Immutable
interface ThemePreferenceRepository {
    val themeMode: Flow<ThemeMode>

    fun setThemeMode(themeMode: ThemeMode)
}
