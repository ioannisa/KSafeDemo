package eu.anifantakis.ksafe_demo.features.preferences.presentation.screens.preferences

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.AppDrawableRepo
import eu.anifantakis.ksafe_demo.core.presentation.design_system.UIConst
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppPreview
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppRadioPreference
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppText
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppTextStyle
import eu.anifantakis.ksafe_demo.features.preferences.domain.model.ThemeMode
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PreferencesScreenRoot(
    viewModel: PreferencesViewModel = koinViewModel(),
) {
    PreferencesScreen(
        state = viewModel.state.value,
        onIntent = viewModel::onAction,
    )
}

@Composable
private fun PreferencesScreen(
    state: PreferencesState,
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
            text = "Appearance",
            style = AppTextStyle.SECTION_HEADING,
            modifier = Modifier.fillMaxWidth(),
            fontWeight = FontWeight.Bold,
        )
        AppText(
            text = "Choose how KSafeDemo selects its color theme. The preference is stored " +
                "locally and applied immediately on every platform.",
            style = AppTextStyle.BODY,
            modifier = Modifier.fillMaxWidth(),
        )

        ThemeMode.entries.forEach { themeMode ->
            AppRadioPreference(
                title = themeMode.title(),
                description = themeMode.description(),
                icon = themeMode.icon(),
                selected = state.themeMode == themeMode,
                onClick = {
                    onIntent(PreferencesIntent.ThemeSelected(themeMode))
                },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

private fun ThemeMode.title(): String = when (this) {
    ThemeMode.DAY -> "Day"
    ThemeMode.NIGHT -> "Night"
    ThemeMode.SYSTEM -> "System"
}

private fun ThemeMode.description(): String = when (this) {
    ThemeMode.DAY -> "Always use the light color palette"
    ThemeMode.NIGHT -> "Always use the dark color palette"
    ThemeMode.SYSTEM -> "Follow the operating system appearance"
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
            onIntent = {},
        )
    }
}
