package eu.anifantakis.ksafe_demo.screens.customjson

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeWriteMode
import eu.anifantakis.lib.ksafe.compose.mutableStateOf
import kotlin.time.TimeSource
import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

// ── Custom types that have NO built-in serializer ────────────────────
// These represent third-party types you don't own (like UUID, Instant,
// BigDecimal, etc.). You can't add @Serializable to them, so you write
// a small KSerializer for each and register them via SerializersModule.

/** A timestamp wrapper — stands in for types like `kotlinx.datetime.Instant`. */
data class Timestamp(val epochMillis: Long) {
    fun toReadableString(): String {
        val totalSeconds = epochMillis / 1000
        val hours = (totalSeconds / 3600) % 24
        val minutes = (totalSeconds / 60) % 60
        val seconds = totalSeconds % 60
        return "${hours.toString().padStart(2, '0')}:" +
                "${minutes.toString().padStart(2, '0')}:" +
                "${seconds.toString().padStart(2, '0')}"
    }
}

/** A hex color wrapper — stands in for types like `java.awt.Color`. */
data class HexColor(val hex: String) {
    override fun toString(): String = hex
}

// ── Custom serializers ───────────────────────────────────────────────

object TimestampSerializer : KSerializer<Timestamp> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Timestamp", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Timestamp) =
        encoder.encodeLong(value.epochMillis)

    override fun deserialize(decoder: Decoder): Timestamp =
        Timestamp(decoder.decodeLong())
}

object HexColorSerializer : KSerializer<HexColor> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("HexColor", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: HexColor) =
        encoder.encodeString(value.hex)

    override fun deserialize(decoder: Decoder): HexColor =
        HexColor(decoder.decodeString())
}

// ── Data class using @Contextual ─────────────────────────────────────
// Both fields below are third-party types that need custom serializers.
// Without the custom Json instance, KSafe would throw SerializationException.

@Serializable
data class UserProfile(
    val name: String,
    @Contextual val createdAt: Timestamp,
    @Contextual val favoriteColor: HexColor
)

// ── Custom Json — register all serializers in one place ──────────────

val customJsonForKSafe = Json {
    ignoreUnknownKeys = true
    serializersModule = SerializersModule {
        contextual(TimestampSerializer)
        contextual(HexColorSerializer)
        // add as many as you need
    }
}

// ── ViewModel ────────────────────────────────────────────────────────

private val colors = listOf(
    HexColor("#FF5733"), // red-orange
    HexColor("#33B5FF"), // sky blue
    HexColor("#4CAF50"), // green
    HexColor("#9C27B0"), // purple
    HexColor("#FF9800"), // orange
    HexColor("#00BCD4"), // teal
)

@Stable
class CustomJsonViewModel(
    private val ksafe: KSafe
) : ViewModel() {

    // Monotonic mark taken at VM creation — used to generate unique timestamps
    private val startMark = TimeSource.Monotonic.markNow()

    private val defaultProfile = UserProfile(
        name = "guest",
        createdAt = Timestamp(0L),
        favoriteColor = HexColor("#000000")
    )

    // Persisted with encryption, using the custom Json KSafe instance
    var profile by ksafe.mutableStateOf(
        defaultValue = defaultProfile,
        key = "custom_json_profile",
        mode = KSafeWriteMode.Encrypted()
    )
        private set

    // Plain-text variant to show it works in both modes
    var plainProfile by ksafe.mutableStateOf(
        defaultValue = defaultProfile,
        key = "custom_json_plain_profile",
        mode = KSafeWriteMode.Plain
    )
        private set

    var saveCount by mutableStateOf(0)
        private set

    fun updateProfile(name: String) {
        val elapsed = startMark.elapsedNow().inWholeMilliseconds
        val timestamp = Timestamp(elapsed)
        val color = colors[saveCount % colors.size]
        val updated = UserProfile(name, timestamp, color)
        profile = updated
        plainProfile = updated
        saveCount++
    }

    fun clear() {
        profile = defaultProfile
        plainProfile = defaultProfile
        ksafe.deleteDirect("custom_json_profile")
        ksafe.deleteDirect("custom_json_plain_profile")
        saveCount = 0
    }
}
