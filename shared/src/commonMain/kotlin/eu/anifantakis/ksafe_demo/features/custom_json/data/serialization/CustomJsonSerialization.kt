package eu.anifantakis.ksafe_demo.features.custom_json.data.serialization

import eu.anifantakis.ksafe_demo.features.custom_json.domain.model.HexColor
import eu.anifantakis.ksafe_demo.features.custom_json.domain.model.Timestamp
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

object TimestampSerializer : KSerializer<Timestamp> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("Timestamp", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Timestamp) {
        encoder.encodeLong(value.epochMillis)
    }

    override fun deserialize(decoder: Decoder): Timestamp =
        Timestamp(decoder.decodeLong())
}

object HexColorSerializer : KSerializer<HexColor> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("HexColor", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: HexColor) {
        encoder.encodeString(value.hex)
    }

    override fun deserialize(decoder: Decoder): HexColor =
        HexColor(decoder.decodeString())
}

val customJsonForKSafe = Json {
    ignoreUnknownKeys = true
    serializersModule = SerializersModule {
        contextual(TimestampSerializer)
        contextual(HexColorSerializer)
    }
}
