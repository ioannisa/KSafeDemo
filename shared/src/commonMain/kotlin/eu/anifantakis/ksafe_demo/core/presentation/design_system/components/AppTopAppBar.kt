package eu.anifantakis.ksafe_demo.core.presentation.design_system.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.AppDrawableRepo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopAppBar(
    title: String,
    onPreferencesClick: () -> Unit,
    onAboutClick: () -> Unit,
    onBackClick: (() -> Unit)? = null,
) {
    var isMenuExpanded by rememberSaveable { mutableStateOf(false) }

    TopAppBar(
        title = {
            AppText(
                text = title,
                style = AppTextStyle.SCREEN_TITLE_COMPACT,
            )
        },
        navigationIcon = {
            onBackClick?.let { onBack ->
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = AppDrawableRepo.back,
                        contentDescription = "Back",
                    )
                }
            }
        },
        actions = {
            Box {
                IconButton(onClick = { isMenuExpanded = true }) {
                    Icon(
                        imageVector = AppDrawableRepo.appMenu,
                        contentDescription = "Open application menu",
                    )
                }
                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false },
                ) {
                    DropdownMenuItem(
                        text = {
                            AppText(
                                text = "Preferences",
                                style = AppTextStyle.BODY,
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = AppDrawableRepo.preferences,
                                contentDescription = null,
                            )
                        },
                        onClick = {
                            isMenuExpanded = false
                            onPreferencesClick()
                        },
                    )
                    DropdownMenuItem(
                        text = {
                            AppText(
                                text = "About",
                                style = AppTextStyle.BODY,
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = AppDrawableRepo.about,
                                contentDescription = null,
                            )
                        },
                        onClick = {
                            isMenuExpanded = false
                            onAboutClick()
                        },
                    )
                }
            }
        },
    )
}

@PreviewLightDark
@Composable
private fun PreviewAppTopAppBar() {
    AppPreview {
        AppTopAppBar(
            title = "Presenting KSafe 3.1.0",
            onPreferencesClick = {},
            onAboutClick = {},
        )
    }
}
