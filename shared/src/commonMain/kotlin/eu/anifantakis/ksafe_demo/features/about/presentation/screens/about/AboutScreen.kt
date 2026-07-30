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
            text = "About",
            style = AppTextStyle.SCREEN_TITLE,
            fontWeight = FontWeight.Bold,
        )
        AppText(
            text = "KSafe ${state.kSafeVersion}",
            style = AppTextStyle.SECTION_HEADING,
            fontWeight = FontWeight.Bold,
        )
        AppText(
            text = "A Kotlin Multiplatform library for secure, encrypted persistence backed " +
                "by the platform Keystore or Keychain.",
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
                    text = "Developer",
                    style = AppTextStyle.SECTION_TITLE,
                    fontWeight = FontWeight.Bold,
                )
                AppText(state.developerName, AppTextStyle.BODY)
                AppText(state.developerEmail, AppTextStyle.CAPTION)
                AppText(state.developerWebsite, AppTextStyle.CAPTION)
                AppButton(
                    label = "Personal website",
                    onClick = {
                        onIntent(AboutIntent.OpenLink(state.developerWebsite))
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                AppButton(
                    label = "Email",
                    onClick = {
                        onIntent(AboutIntent.OpenLink("mailto:${state.developerEmail}"))
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                AppButton(
                    label = "GitHub profile",
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
                    text = "Projects",
                    style = AppTextStyle.SECTION_TITLE,
                    fontWeight = FontWeight.Bold,
                )
                AppText(
                    text = "KSafe library",
                    style = AppTextStyle.BODY,
                    fontWeight = FontWeight.Bold,
                )
                AppText(state.kSafeRepository, AppTextStyle.CAPTION)
                AppButton(
                    label = "Open KSafe repository",
                    onClick = {
                        onIntent(AboutIntent.OpenLink(state.kSafeRepository))
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                AppText(
                    text = "KSafeDemo application",
                    style = AppTextStyle.BODY,
                    fontWeight = FontWeight.Bold,
                )
                AppText(state.kSafeDemoRepository, AppTextStyle.CAPTION)
                AppButton(
                    label = "Open KSafeDemo repository",
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
