package eu.anifantakis.ksafe_demo.core.presentation.design_system.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.AppDrawableRepo
import eu.anifantakis.ksafe_demo.core.presentation.design_system.UIConst
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.Strings
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.withArgs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopAppBar(
    title: String,
    onPreferencesClick: () -> Unit,
    onAboutClick: () -> Unit,
    onBackClick: (() -> Unit)? = null,
    isAboutEnabled: Boolean = true,
) {
    var isMenuExpanded by rememberSaveable { mutableStateOf(false) }

    TopAppBar(
        // A tinted container plus a real drop shadow: M3's default bar colour IS the
        // content background, so without both the bar dissolves into the screen.
        modifier = Modifier.shadow(elevation = UIConst.barElevation),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        ),
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
                        contentDescription = Strings[StringKey.COMMON_BACK],
                    )
                }
            }
        },
        actions = {
            Box {
                IconButton(onClick = { isMenuExpanded = true }) {
                    Icon(
                        imageVector = AppDrawableRepo.appMenu,
                        contentDescription = Strings[StringKey.COMMON_OPEN_APPLICATION_MENU],
                    )
                }
                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false },
                ) {
                    DropdownMenuItem(
                        text = {
                            AppText(
                                text = Strings[StringKey.COMMON_PREFERENCES],
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
                                text = Strings[StringKey.COMMON_ABOUT],
                                style = AppTextStyle.BODY,
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = AppDrawableRepo.about,
                                contentDescription = null,
                            )
                        },
                        // Greys out when the About screen is already open: the entry led
                        // nowhere there, and a dead-but-lit item reads as a broken tap.
                        enabled = isAboutEnabled,
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
            title = Strings[StringKey.APP_PRESENTING_KSAFE].withArgs(listOf("3.1.0")),
            onPreferencesClick = {},
            onAboutClick = {},
        )
    }
}
