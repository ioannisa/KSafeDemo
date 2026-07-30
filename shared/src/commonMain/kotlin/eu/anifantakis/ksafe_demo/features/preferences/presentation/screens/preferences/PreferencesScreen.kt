package eu.anifantakis.ksafe_demo.features.preferences.presentation.screens.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.AppDrawableRepo
import eu.anifantakis.ksafe_demo.core.presentation.design_system.UIConst
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppPreview
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppDivider
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppDropdown
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppRadioPreference
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppText
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppTextStyle
import eu.anifantakis.ksafe_demo.features.preferences.domain.model.ThemeMode
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.Language
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.LocalizationManager
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.Strings
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.rememberCurrentLanguage
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PreferencesScreenRoot(
    viewModel: PreferencesViewModel = koinViewModel(),
) {
    val currentLanguage by rememberCurrentLanguage()

    PreferencesScreen(
        state = viewModel.state.value,
        currentLanguage = currentLanguage,
        onIntent = viewModel::onAction,
    )
}

@Composable
private fun PreferencesScreen(
    state: PreferencesState,
    currentLanguage: Language,
    onIntent: (PreferencesIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(
                vertical = UIConst.screenVerticalPadding,
                horizontal = UIConst.screenHorizontalPadding,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(UIConst.paddingSmall),
    ) {
        AppText(
            text = Strings[StringKey.PREFERENCES_APPEARANCE],
            style = AppTextStyle.SECTION_HEADING,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold,
        )
        AppText(
            text = Strings[StringKey.PREFERENCES_APPEARANCE_DESCRIPTION],
            style = AppTextStyle.BODY,
            modifier = Modifier.fillMaxWidth(),
        )

        ThemeMode.entries.forEach { themeMode ->
            AppRadioPreference(
                title = Strings[themeMode.titleKey()],
                description = Strings[themeMode.descriptionKey()],
                icon = themeMode.icon(),
                selected = state.themeMode == themeMode,
                onClick = {
                    onIntent(PreferencesIntent.ThemeSelected(themeMode))
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        AppDivider(
            modifier = Modifier.padding(vertical = UIConst.paddingSmall),
        )
        AppText(
            text = Strings[StringKey.PREFERENCES_LANGUAGE],
            style = AppTextStyle.SECTION_HEADING,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold,
        )
        AppText(
            text = Strings[StringKey.PREFERENCES_LANGUAGE_DESCRIPTION],
            style = AppTextStyle.BODY,
            modifier = Modifier.fillMaxWidth(),
        )
        AppDropdown(
            selectedItem = currentLanguage,
            items = LocalizationManager.availableLanguages(),
            itemLabel = Language::displayName,
            label = Strings[StringKey.PREFERENCES_SELECT_LANGUAGE],
            onItemSelected = { language ->
                onIntent(PreferencesIntent.LanguageSelected(language))
            },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

private fun ThemeMode.titleKey(): StringKey = when (this) {
    ThemeMode.DAY -> StringKey.PREFERENCES_THEME_DAY
    ThemeMode.NIGHT -> StringKey.PREFERENCES_THEME_NIGHT
    ThemeMode.SYSTEM -> StringKey.PREFERENCES_THEME_SYSTEM
}

private fun ThemeMode.descriptionKey(): StringKey = when (this) {
    ThemeMode.DAY -> StringKey.PREFERENCES_THEME_DAY_DESCRIPTION
    ThemeMode.NIGHT -> StringKey.PREFERENCES_THEME_NIGHT_DESCRIPTION
    ThemeMode.SYSTEM -> StringKey.PREFERENCES_THEME_SYSTEM_DESCRIPTION
}

@Composable
private fun ThemeMode.icon(): ImageVector = when (this) {
    ThemeMode.DAY -> AppDrawableRepo.themeDay
    ThemeMode.NIGHT -> AppDrawableRepo.themeNight
    ThemeMode.SYSTEM -> AppDrawableRepo.themeSystem
}

@PreviewLightDark
@Composable
private fun PreferencesScreenPreview() {
    AppPreview {
        PreferencesScreen(
            state = PreferencesState(themeMode = ThemeMode.SYSTEM),
            currentLanguage = Language.EN,
            onIntent = {},
        )
    }
}
