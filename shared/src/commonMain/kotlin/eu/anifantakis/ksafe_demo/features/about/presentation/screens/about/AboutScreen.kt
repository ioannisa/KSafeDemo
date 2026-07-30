package eu.anifantakis.ksafe_demo.features.about.presentation.screens.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.UIConst
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppButton
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppCard
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppPreview
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppText
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppTextStyle
import eu.anifantakis.ksafe_demo.core.presentation.helper.ObserveEffects
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.Strings
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.withArgs
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AboutScreenRoot(
    viewModel: AboutViewModel = koinViewModel(),
) {
    val uriHandler = LocalUriHandler.current

    ObserveEffects(viewModel.events) { effect ->
        when (effect) {
            is AboutEffect.OpenUri -> uriHandler.openUri(effect.uri)
        }
    }

    AboutScreen(
        state = viewModel.state.value,
        onIntent = viewModel::onAction,
    )
}

@Composable
private fun AboutScreen(
    state: AboutState,
    onIntent: (AboutIntent) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                vertical = UIConst.screenVerticalPadding,
                horizontal = UIConst.screenHorizontalPadding,
            ),
        verticalArrangement = Arrangement.spacedBy(UIConst.paddingRegular),
    ) {
        AppText(
            text = Strings[StringKey.ABOUT_TITLE],
            style = AppTextStyle.SCREEN_TITLE,
            fontWeight = FontWeight.Bold,
        )
        AppText(
            text = Strings[StringKey.ABOUT_KSAFE_VERSION].withArgs(listOf(state.kSafeVersion)),
            style = AppTextStyle.SECTION_HEADING,
            fontWeight = FontWeight.Bold,
        )
        AppText(
            text = Strings[StringKey.ABOUT_DESCRIPTION],
            style = AppTextStyle.BODY,
        )

        AppCard(
            modifier = Modifier.fillMaxWidth(),
            bordered = true,
        ) {
            Column(
                modifier = Modifier.padding(UIConst.paddingRegular),
                verticalArrangement = Arrangement.spacedBy(UIConst.paddingSmall),
            ) {
                AppText(
                    text = Strings[StringKey.ABOUT_DEVELOPER],
                    style = AppTextStyle.SECTION_TITLE,
                    fontWeight = FontWeight.Bold,
                )
                AppText(state.developerName, AppTextStyle.BODY)
                AppText(state.developerEmail, AppTextStyle.CAPTION)
                AppText(state.developerWebsite, AppTextStyle.CAPTION)
                AppButton(
                    label = Strings[StringKey.ABOUT_PERSONAL_WEBSITE],
                    onClick = {
                        onIntent(AboutIntent.OpenLink(state.developerWebsite))
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                AppButton(
                    label = Strings[StringKey.COMMON_EMAIL],
                    onClick = {
                        onIntent(AboutIntent.OpenLink("mailto:${state.developerEmail}"))
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                AppButton(
                    label = Strings[StringKey.ABOUT_GITHUB_PROFILE],
                    onClick = {
                        onIntent(AboutIntent.OpenLink(state.developerGitHub))
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        AppCard(
            modifier = Modifier.fillMaxWidth(),
            bordered = true,
        ) {
            Column(
                modifier = Modifier.padding(UIConst.paddingRegular),
                verticalArrangement = Arrangement.spacedBy(UIConst.paddingSmall),
            ) {
                AppText(
                    text = Strings[StringKey.ABOUT_PROJECTS],
                    style = AppTextStyle.SECTION_TITLE,
                    fontWeight = FontWeight.Bold,
                )
                AppText(
                    text = Strings[StringKey.ABOUT_KSAFE_LIBRARY],
                    style = AppTextStyle.BODY,
                    fontWeight = FontWeight.Bold,
                )
                AppText(state.kSafeRepository, AppTextStyle.CAPTION)
                AppButton(
                    label = Strings[StringKey.ABOUT_OPEN_KSAFE_REPOSITORY],
                    onClick = {
                        onIntent(AboutIntent.OpenLink(state.kSafeRepository))
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                AppText(
                    text = Strings[StringKey.ABOUT_KSAFE_DEMO_APPLICATION],
                    style = AppTextStyle.BODY,
                    fontWeight = FontWeight.Bold,
                )
                AppText(state.kSafeDemoRepository, AppTextStyle.CAPTION)
                AppButton(
                    label = Strings[StringKey.ABOUT_OPEN_KSAFE_DEMO_REPOSITORY],
                    onClick = {
                        onIntent(AboutIntent.OpenLink(state.kSafeDemoRepository))
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun AboutScreenPreview() {
    AppPreview {
        AboutScreen(
            state = AboutState(kSafeVersion = "3.1.0"),
            onIntent = {},
        )
    }
}
