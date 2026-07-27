package eu.anifantakis.ksafe_demo.features.custom_json.domain.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

/** Timestamp wrapper used to demonstrate contextual serialization. */
data class Timestamp(val epochMillis: Long) {
    fun toReadableString(): String {
        val totalSeconds = epochMillis / 1000
        val hours = (totalSeconds / 3600) % 24
        val minutes = (totalSeconds / 60) % 60
        val seconds = totalSeconds % 60
        return "${hours.toString().padStart(2, '0')}:" +
            "${minutes.toString().padStart(2, '0')}:" +
            seconds.toString().padStart(2, '0')
    }
}

/** Hex color wrapper used to demonstrate contextual serialization. */
data class HexColor(val hex: String) {
    override fun toString(): String = hex
}

@Serializable
data class UserProfile(
    val name: String,
    @Contextual val createdAt: Timestamp,
    @Contextual val favoriteColor: HexColor,
)
