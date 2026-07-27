package eu.anifantakis.ksafe_demo.features.custom_json.presentation.screens.custom_json

import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.anifantakis.ksafe_demo.core.presentation.design_system.AppColor
import eu.anifantakis.ksafe_demo.core.presentation.design_system.KSafeDemoTheme
import eu.anifantakis.ksafe_demo.core.presentation.design_system.UIConst
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppButton
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppCard
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppDivider
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppText
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppTextField
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppTextStyle
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

        StepHeader("1. Define custom serializers")
        CodeBlock(
            "object TimestampSerializer : KSerializer<Timestamp> {\n" +
                "  override fun serialize(encoder: Encoder, value: Timestamp) =\n" +
                "    encoder.encodeLong(value.epochMillis)\n" +
                "}",
        )
        StepHeader("2. Register serializers")
        CodeBlock(
            "val customJson = Json {\n" +
                "  serializersModule = SerializersModule {\n" +
                "    contextual(TimestampSerializer)\n" +
                "    contextual(HexColorSerializer)\n" +
                "  }\n" +
                "}",
        )
        StepHeader("3. Pass it through KSafeConfig")
        CodeBlock("val ksafe = KSafe(config = KSafeConfig(json = customJson))")
        StepHeader("4. Use @Contextual fields")
        CodeBlock(
            "@Serializable\n" +
                "data class UserProfile(\n" +
                "  val name: String,\n" +
                "  @Contextual val createdAt: Timestamp,\n" +
                "  @Contextual val favoriteColor: HexColor\n" +
                ")",
        )

        AppDivider()
        StepHeader("Try It")
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
        StepHeader("Stored Values (persisted across restarts)")
        ProfileCard(label = "Encrypted", profile = state.profile)
        ProfileCard(label = "Plain Text", profile = state.plainProfile)
        Spacer(modifier = Modifier.height(UIConst.paddingRegular))
    }
}

@Composable
private fun StepHeader(text: String) {
    AppText(
        text = text,
        style = AppTextStyle.STEP_TITLE,
        modifier = Modifier.fillMaxWidth(),
        color = AppColor.Primary,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
private fun ProfileCard(
    label: String,
    profile: UserProfile,
) {
    AppCard(modifier = Modifier.fillMaxWidth(), bordered = true) {
        Column(
            modifier = Modifier
                .padding(UIConst.paddingSmall)
                .padding(horizontal = UIConst.screenHorizontalPadding),
        ) {
            AppText(label, AppTextStyle.CAPTION)
            AppText("name: ${profile.name}", AppTextStyle.BODY)
            AppText(
                text = "createdAt: ${profile.createdAt.epochMillis} " +
                    "(${profile.createdAt.toReadableString()})",
                style = AppTextStyle.BODY,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(UIConst.paddingSmall),
            ) {
                AppText(
                    text = "favoriteColor: ${profile.favoriteColor.hex}",
                    style = AppTextStyle.BODY,
                )
                Box(
                    modifier = Modifier
                        .size(ColorSwatchSize)
                        .clip(CircleShape)
                        .background(parseHexColor(profile.favoriteColor.hex)),
                )
            }
        }
    }
}

@Composable
private fun CodeBlock(code: String) {
    AppText(
        text = code,
        style = AppTextStyle.CODE,
        color = AppColor.CodeText,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = AppColor.CodeBackground,
                shape = RoundedCornerShape(UIConst.cornerRadius),
            )
            .padding(UIConst.paddingSmall),
    )
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

private val ColorSwatchSize = 14.dp

@Preview
@Composable
private fun PreviewCustomJsonScreen() {
    KSafeDemoTheme {
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
