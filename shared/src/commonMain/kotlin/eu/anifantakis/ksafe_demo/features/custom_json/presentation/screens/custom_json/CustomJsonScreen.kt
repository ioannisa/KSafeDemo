package eu.anifantakis.ksafe_demo.features.custom_json.presentation.screens.custom_json

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.UIConst
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppButton
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppDivider
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppPreview
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppText
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppTextField
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppTextStyle
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content.AppCodeBlock
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content.AppProfileCard
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content.AppSectionHeader
import eu.anifantakis.ksafe_demo.features.custom_json.domain.model.HexColor
import eu.anifantakis.ksafe_demo.features.custom_json.domain.model.Timestamp
import eu.anifantakis.ksafe_demo.features.custom_json.domain.model.UserProfile
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CustomJsonScreenRoot(
    viewModel: CustomJsonViewModel = koinViewModel(),
) {
    CustomJsonScreen(
        state = viewModel.state.value,
        onIntent = viewModel::onAction,
    )
}

@Composable
private fun CustomJsonScreen(
    state: CustomJsonState,
    onIntent: (CustomJsonIntent) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                vertical = UIConst.screenVerticalPadding,
                horizontal = UIConst.screenHorizontalPadding,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(UIConst.paddingSmall),
    ) {
        AppText(
            text = "Custom JSON Serialization",
            style = AppTextStyle.SCREEN_TITLE_COMPACT,
            fontWeight = FontWeight.Bold,
        )
        AppText(
            text = "Store data classes with @Contextual fields that need custom serializers.",
            style = AppTextStyle.CAPTION,
        )
        AppDivider()

        AppSectionHeader(
            text = "1. Define custom serializers",
            modifier = Modifier.fillMaxWidth(),
            textStyle = AppTextStyle.STEP_TITLE,
        )
        AppCodeBlock(
            "object TimestampSerializer : KSerializer<Timestamp> {\n" +
                "  override fun serialize(encoder: Encoder, value: Timestamp) =\n" +
                "    encoder.encodeLong(value.epochMillis)\n" +
                "}",
        )
        AppSectionHeader(
            text = "2. Register serializers",
            modifier = Modifier.fillMaxWidth(),
            textStyle = AppTextStyle.STEP_TITLE,
        )
        AppCodeBlock(
            "val customJson = Json {\n" +
                "  serializersModule = SerializersModule {\n" +
                "    contextual(TimestampSerializer)\n" +
                "    contextual(HexColorSerializer)\n" +
                "  }\n" +
                "}",
        )
        AppSectionHeader(
            text = "3. Pass it through KSafeConfig",
            modifier = Modifier.fillMaxWidth(),
            textStyle = AppTextStyle.STEP_TITLE,
        )
        AppCodeBlock("val ksafe = KSafe(config = KSafeConfig(json = customJson))")
        AppSectionHeader(
            text = "4. Use @Contextual fields",
            modifier = Modifier.fillMaxWidth(),
            textStyle = AppTextStyle.STEP_TITLE,
        )
        AppCodeBlock(
            "@Serializable\n" +
                "data class UserProfile(\n" +
                "  val name: String,\n" +
                "  @Contextual val createdAt: Timestamp,\n" +
                "  @Contextual val favoriteColor: HexColor\n" +
                ")",
        )

        AppDivider()
        AppSectionHeader(
            text = "Try It",
            modifier = Modifier.fillMaxWidth(),
            textStyle = AppTextStyle.STEP_TITLE,
        )
        AppTextField(
            value = state.nameInput,
            onValueChange = { onIntent(CustomJsonIntent.NameChanged(it)) },
            label = "Name",
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(UIConst.paddingSmall)) {
            AppButton(
                label = "Save",
                onClick = {
                    keyboardController?.hide()
                    onIntent(CustomJsonIntent.Save)
                },
            )
            AppButton(
                label = "Clear",
                onClick = { onIntent(CustomJsonIntent.Clear) },
            )
        }
        if (state.saveCount > 0) {
            AppText(
                text = "Saved ${state.saveCount} time(s) — color cycles on each save",
                style = AppTextStyle.CAPTION,
            )
        }

        AppDivider()
        AppSectionHeader(
            text = "Stored Values (persisted across restarts)",
            modifier = Modifier.fillMaxWidth(),
            textStyle = AppTextStyle.STEP_TITLE,
        )
        AppProfileCard(
            label = "Encrypted",
            name = state.profile.name,
            createdAt = "${state.profile.createdAt.epochMillis} " +
                "(${state.profile.createdAt.toReadableString()})",
            favoriteColor = state.profile.favoriteColor.hex,
            favoriteColorSwatch = parseHexColor(state.profile.favoriteColor.hex),
            modifier = Modifier.fillMaxWidth(),
        )
        AppProfileCard(
            label = "Plain Text",
            name = state.plainProfile.name,
            createdAt = "${state.plainProfile.createdAt.epochMillis} " +
                "(${state.plainProfile.createdAt.toReadableString()})",
            favoriteColor = state.plainProfile.favoriteColor.hex,
            favoriteColorSwatch = parseHexColor(state.plainProfile.favoriteColor.hex),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(UIConst.paddingRegular))
    }
}

private fun parseHexColor(hex: String): Color {
    val cleaned = hex.removePrefix("#")
    if (cleaned.length != 6) return Color.Gray
    return try {
        Color(
            red = cleaned.substring(0, 2).toInt(16),
            green = cleaned.substring(2, 4).toInt(16),
            blue = cleaned.substring(4, 6).toInt(16),
        )
    } catch (_: Exception) {
        Color.Gray
    }
}

@PreviewLightDark
@Composable
private fun PreviewCustomJsonScreen() {
    AppPreview {
        CustomJsonScreen(
            state = CustomJsonState(
                profile = UserProfile(
                    name = "Ada",
                    createdAt = Timestamp(123_456),
                    favoriteColor = HexColor("#33B5FF"),
                ),
                saveCount = 1,
            ),
            onIntent = {},
        )
    }
}
