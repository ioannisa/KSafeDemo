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
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.Strings
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.withArgs
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
            text = Strings[StringKey.CUSTOM_JSON_TITLE],
            style = AppTextStyle.SCREEN_TITLE_COMPACT,
            fontWeight = FontWeight.Bold,
        )
        AppText(
            text = Strings[StringKey.CUSTOM_JSON_SUBTITLE],
            style = AppTextStyle.CAPTION,
        )
        AppDivider()

        AppSectionHeader(
            text = Strings[StringKey.CUSTOM_JSON_DEFINE_SERIALIZERS],
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
            text = Strings[StringKey.CUSTOM_JSON_REGISTER_SERIALIZERS],
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
            text = Strings[StringKey.CUSTOM_JSON_PASS_THROUGH_CONFIG],
            modifier = Modifier.fillMaxWidth(),
            textStyle = AppTextStyle.STEP_TITLE,
        )
        AppCodeBlock("val ksafe = KSafe(config = KSafeConfig(json = customJson))")
        AppSectionHeader(
            text = Strings[StringKey.CUSTOM_JSON_USE_CONTEXTUAL_FIELDS],
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
            text = Strings[StringKey.CUSTOM_JSON_TRY_IT],
            modifier = Modifier.fillMaxWidth(),
            textStyle = AppTextStyle.STEP_TITLE,
        )
        AppTextField(
            value = state.nameInput,
            onValueChange = { onIntent(CustomJsonIntent.NameChanged(it)) },
            label = Strings[StringKey.CUSTOM_JSON_NAME],
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(UIConst.paddingSmall),
        ) {
            AppButton(
                label = Strings[StringKey.COMMON_SAVE],
                onClick = {
                    keyboardController?.hide()
                    onIntent(CustomJsonIntent.Save)
                },
                modifier = Modifier.weight(1f),
            )
            AppButton(
                label = Strings[StringKey.COMMON_CLEAR],
                onClick = { onIntent(CustomJsonIntent.Clear) },
                modifier = Modifier.weight(1f),
            )
        }
        if (state.saveCount > 0) {
            AppText(
                text = Strings[StringKey.CUSTOM_JSON_SAVED_COUNT]
                    .withArgs(listOf(state.saveCount)),
                style = AppTextStyle.CAPTION,
            )
        }

        AppDivider()
        AppSectionHeader(
            text = Strings[StringKey.CUSTOM_JSON_STORED_VALUES],
            modifier = Modifier.fillMaxWidth(),
            textStyle = AppTextStyle.STEP_TITLE,
        )
        AppProfileCard(
            label = Strings[StringKey.CUSTOM_JSON_ENCRYPTED],
            name = state.profile.name,
            createdAt = "${state.profile.createdAt.epochMillis} " +
                "(${state.profile.createdAt.toReadableString()})",
            favoriteColor = state.profile.favoriteColor.hex,
            favoriteColorSwatch = parseHexColor(state.profile.favoriteColor.hex),
            modifier = Modifier.fillMaxWidth(),
        )
        AppProfileCard(
            label = Strings[StringKey.CUSTOM_JSON_PLAIN_TEXT],
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
