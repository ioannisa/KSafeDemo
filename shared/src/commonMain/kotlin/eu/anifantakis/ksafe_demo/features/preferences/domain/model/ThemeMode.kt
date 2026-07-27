package eu.anifantakis.ksafe_demo.features.preferences.domain.model

import kotlinx.serialization.Serializable

@Serializable
enum class ThemeMode {
    DAY,
    NIGHT,
    SYSTEM,
}
