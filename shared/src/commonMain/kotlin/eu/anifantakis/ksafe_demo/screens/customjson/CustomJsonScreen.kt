package eu.anifantakis.ksafe_demo.screens.customjson

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CustomJsonScreen(
    viewModel: CustomJsonViewModel = koinViewModel()
) {
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current

    var nameInput by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(vertical = 10.dp, horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Custom JSON Serialization",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Store data classes with @Contextual fields — types you don't own " +
                    "(like UUID, Instant, Color) that need custom serializers.",
            fontSize = 12.sp,
            color = Color.Gray
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

        // --- Step 1: Define custom serializers ---
        StepHeader("1. Define custom serializers")
        CodeBlock(
            """
            |object TimestampSerializer : KSerializer<Timestamp> {
            |    override val descriptor = PrimitiveSerialDescriptor(
            |        "Timestamp", PrimitiveKind.LONG)
            |    override fun serialize(encoder: Encoder, value: Timestamp) =
            |        encoder.encodeLong(value.epochMillis)
            |    override fun deserialize(decoder: Decoder) =
            |        Timestamp(decoder.decodeLong())
            |}
            |
            |object HexColorSerializer : KSerializer<HexColor> {
            |    override val descriptor = PrimitiveSerialDescriptor(
            |        "HexColor", PrimitiveKind.STRING)
            |    override fun serialize(encoder: Encoder, value: HexColor) =
            |        encoder.encodeString(value.hex)
            |    override fun deserialize(decoder: Decoder) =
            |        HexColor(decoder.decodeString())
            |}
            """.trimMargin()
        )

        // --- Step 2: Register in one place ---
        StepHeader("2. Register all serializers in one place")
        CodeBlock(
            """
            |val customJson = Json {
            |    ignoreUnknownKeys = true
            |    serializersModule = SerializersModule {
            |        contextual(TimestampSerializer)
            |        contextual(HexColorSerializer)
            |        // add as many as you need
            |    }
            |}
            """.trimMargin()
        )

        // --- Step 3: Pass via KSafeConfig ---
        StepHeader("3. Pass it via KSafeConfig")
        CodeBlock(
            """
            |val ksafe = KSafe(
            |    config = KSafeConfig(json = customJson)
            |)
            """.trimMargin()
        )

        // --- Step 4: Use @Contextual directly ---
        StepHeader("4. Use @Contextual types directly")
        CodeBlock(
            """
            |@Serializable
            |data class UserProfile(
            |    val name: String,
            |    @Contextual val createdAt: Timestamp,
            |    @Contextual val favoriteColor: HexColor
            |)
            |
            |// Works with all KSafe APIs — no extra work
            |var profile by ksafe.mutableStateOf(
            |    defaultValue = defaultProfile,
            |    mode = KSafeWriteMode.Encrypted()
            |)
            """.trimMargin()
        )

        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

        // --- Try it ---
        Text(
            text = "Try It",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = nameInput,
            onValueChange = { nameInput = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() })
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    keyboardController?.hide()
                    viewModel.updateProfile(
                        name = nameInput.ifBlank { "user_${viewModel.saveCount + 1}" }
                    )
                }
            ) {
                Text("Save")
            }
            Button(onClick = {
                viewModel.clear()
                nameInput = ""
            }) {
                Text("Clear")
            }
        }

        if (viewModel.saveCount > 0) {
            Text(
                text = "Saved ${viewModel.saveCount} time(s) — color cycles on each save",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

        // --- Display stored values ---
        Text(
            text = "Stored Values (persisted across restarts)",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )

        ProfileCard(label = "Encrypted", profile = viewModel.profile)
        ProfileCard(label = "Plain Text", profile = viewModel.plainProfile)

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun StepHeader(text: String) {
    Text(
        text = text,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF1565C0),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun ProfileCard(
    label: String,
    profile: UserProfile
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .padding(horizontal = 12.dp)
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 2.dp)
        )
        Text(text = "name: ${profile.name}", fontSize = 14.sp)
        Text(
            text = "createdAt: ${profile.createdAt.epochMillis} " +
                    "(${profile.createdAt.toReadableString()})",
            fontSize = 14.sp
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = "favoriteColor: ${profile.favoriteColor.hex}", fontSize = 14.sp)
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(parseHexColor(profile.favoriteColor.hex))
            )
        }
    }
}

@Composable
private fun CodeBlock(code: String) {
    Text(
        text = code,
        fontSize = 11.sp,
        fontFamily = FontFamily.Monospace,
        color = Color(0xFF1A1A2E),
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF0F0F5), RoundedCornerShape(6.dp))
            .padding(8.dp)
    )
}

private fun parseHexColor(hex: String): Color {
    val cleaned = hex.removePrefix("#")
    if (cleaned.length != 6) return Color.Gray
    return try {
        val r = cleaned.substring(0, 2).toInt(16)
        val g = cleaned.substring(2, 4).toInt(16)
        val b = cleaned.substring(4, 6).toInt(16)
        Color(r, g, b)
    } catch (_: Exception) {
        Color.Gray
    }
}
