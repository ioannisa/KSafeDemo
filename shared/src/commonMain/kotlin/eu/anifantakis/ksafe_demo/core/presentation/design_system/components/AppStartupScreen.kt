package eu.anifantakis.ksafe_demo.core.presentation.design_system.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.AppDrawableRepo
import eu.anifantakis.ksafe_demo.core.presentation.design_system.UIConst
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.Strings

@Composable
fun AppStartupScreen(
    failed: Boolean,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppSurface(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = AppDrawableRepo.startupLogo,
                contentDescription = null,
                modifier = Modifier.size(UIConst.startupLogoSize),
            )
            Spacer(modifier = Modifier.height(UIConst.paddingRegular))
            AppText(
                text = "KSafe Demo",
                style = AppTextStyle.SCREEN_TITLE_LARGE,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(UIConst.paddingAverage))

            if (failed) {
                AppText(
                    text = Strings[StringKey.APP_STARTUP_FAILED],
                    style = AppTextStyle.BODY,
                )
                Spacer(modifier = Modifier.height(UIConst.paddingRegular))
                AppButton(
                    label = Strings[StringKey.APP_STARTUP_RETRY],
                    onClick = onRetry,
                )
            } else {
                val loadingDescription = Strings[StringKey.APP_STARTUP_LOADING]
                AppProgressIndicator(
                    modifier = Modifier.semantics {
                        contentDescription = loadingDescription
                    },
                )
                Spacer(modifier = Modifier.height(UIConst.paddingSmall))
                AppText(
                    text = loadingDescription,
                    style = AppTextStyle.CAPTION,
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewAppStartupScreen() {
    AppPreview {
        AppStartupScreen(
            failed = false,
            onRetry = {},
        )
    }
}
