package eu.anifantakis.ksafe_demo.app.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.metadata
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import eu.anifantakis.ksafe_demo.core.presentation.design_system.AppDrawableRepo
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppModalBottomSheet
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppSurface
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppText
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppTextStyle
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppTopAppBar
import eu.anifantakis.ksafe_demo.core.presentation.scaffold.ApplicationScaffold
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.Strings
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.withArgs
import eu.anifantakis.ksafe_demo.features.about.presentation.screens.about.AboutScreenRoot
import eu.anifantakis.ksafe_demo.features.custom_json.presentation.screens.custom_json.CustomJsonScreenRoot
import eu.anifantakis.ksafe_demo.features.flows.presentation.screens.flow_delegates.FlowDelegatesScreenRoot
import eu.anifantakis.ksafe_demo.features.preferences.presentation.screens.preferences.PreferencesScreenRoot
import eu.anifantakis.ksafe_demo.features.security.presentation.screens.security.SecurityScreenRoot
import eu.anifantakis.ksafe_demo.features.counters.presentation.screens.counters.CountersScreenRoot
import eu.anifantakis.lib.ksafe.KSafe
import org.koin.compose.koinInject

@Composable
fun NavigationRoot(
    selectedRoute: AppRoute,
    onRouteSelected: (AppRoute) -> Unit,
    ksafe: KSafe = koinInject(),
) {
    val navigator = remember { Navigator(selectedRoute) }
    val kSafeInfo = remember(ksafe) { ksafe.protectionInfo }
    var showPreferences by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(selectedRoute) {
        if (navigator.current() != selectedRoute) {
            navigator.resetTo(selectedRoute)
        }
    }

    val isAboutScreen = navigator.current() == AppRoute.About

    ApplicationScaffold(
        topBar = {
            AppTopAppBar(
                title = Strings[StringKey.APP_PRESENTING_KSAFE]
                    .withArgs(listOf(kSafeInfo.kSafeVersion)),
                onPreferencesClick = { showPreferences = true },
                onAboutClick = {
                    if (navigator.current() != AppRoute.About) {
                        navigator.navigate(AppRoute.About)
                    }
                },
                onBackClick = if (isAboutScreen) navigator::goBack else null,
            )
        },
        bottomBar = {
            if (!isAboutScreen) {
                NavigationBar {
                    AppRoute.bottomNavigationEntries.forEach { route ->
                        val selected = selectedRoute == route
                        NavigationBarItem(
                            selected = selected,
                            onClick = {
                                onRouteSelected(route)
                                navigator.resetTo(route)
                            },
                            label = {
                                AppText(
                                    text = Strings[route.titleKey],
                                    style = AppTextStyle.NAVIGATION_LABEL,
                                )
                            },
                            icon = {
                                Icon(
                                    imageVector = route.navIcon(selected = selected),
                                    // The visible label right below carries the name.
                                    contentDescription = null,
                                )
                            },
                        )
                    }
                }
            }
        },
    ) { padding ->
        NavDisplay(
            backStack = navigator.backStack,
            onBack = navigator::goBack,
            modifier = Modifier.padding(padding),
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
            ),
            entryProvider = entryProvider<NavKey> {
                entry<AppRoute.Counters>(metadata = TabTransitionMetadata) {
                    AppSurface {
                        CountersScreenRoot()
                    }
                }
                entry<AppRoute.Flows>(metadata = TabTransitionMetadata) {
                    AppSurface {
                        FlowDelegatesScreenRoot()
                    }
                }
                entry<AppRoute.CustomJson>(metadata = TabTransitionMetadata) {
                    AppSurface {
                        CustomJsonScreenRoot()
                    }
                }
                entry<AppRoute.Security>(metadata = TabTransitionMetadata) {
                    AppSurface {
                        SecurityScreenRoot()
                    }
                }
                entry<AppRoute.About> {
                    AppSurface {
                        AboutScreenRoot()
                    }
                }
            },
        )
    }

    if (showPreferences) {
        AppModalBottomSheet(
            title = Strings[StringKey.COMMON_PREFERENCES],
            onDismiss = { showPreferences = false },
        ) {
            PreferencesScreenRoot()
        }
    }
}

private val TabTransitionMetadata = metadata {
    put(NavDisplay.TransitionKey) {
        EnterTransition.None togetherWith ExitTransition.None
    }
    put(NavDisplay.PopTransitionKey) {
        EnterTransition.None togetherWith ExitTransition.None
    }
    put(NavDisplay.PredictivePopTransitionKey) { _: Int ->
        EnterTransition.None togetherWith ExitTransition.None
    }
}

@Composable
private fun AppRoute.navIcon(selected: Boolean): ImageVector = when (this) {
    AppRoute.Counters -> if (selected) AppDrawableRepo.navCountersSelected else AppDrawableRepo.navCounters
    AppRoute.Flows -> if (selected) AppDrawableRepo.navFlowsSelected else AppDrawableRepo.navFlows
    AppRoute.CustomJson -> if (selected) AppDrawableRepo.navCustomJsonSelected else AppDrawableRepo.navCustomJson
    AppRoute.Security -> if (selected) AppDrawableRepo.navSecuritySelected else AppDrawableRepo.navSecurity
    // Not bottom-bar entries; mapped anyway so the when stays exhaustive.
    AppRoute.Preferences -> AppDrawableRepo.preferences
    AppRoute.About -> AppDrawableRepo.about
}
