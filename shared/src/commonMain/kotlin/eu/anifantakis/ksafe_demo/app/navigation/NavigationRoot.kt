package eu.anifantakis.ksafe_demo.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppText
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppTextStyle
import eu.anifantakis.ksafe_demo.core.presentation.scaffold.ApplicationScaffold
import eu.anifantakis.ksafe_demo.features.custom_json.presentation.screens.custom_json.CustomJsonScreenRoot
import eu.anifantakis.ksafe_demo.features.flows.presentation.screens.flow_delegates.FlowDelegatesScreenRoot
import eu.anifantakis.ksafe_demo.features.preferences.presentation.screens.preferences.PreferencesScreenRoot
import eu.anifantakis.ksafe_demo.features.security.presentation.screens.security.SecurityScreenRoot
import eu.anifantakis.ksafe_demo.features.storage.presentation.screens.storage.StorageScreenRoot

@Composable
fun NavigationRoot(
    selectedRoute: AppRoute,
    onRouteSelected: (AppRoute) -> Unit,
) {
    val navigator = remember { Navigator(selectedRoute) }

    LaunchedEffect(selectedRoute) {
        if (navigator.current() != selectedRoute) {
            navigator.resetTo(selectedRoute)
        }
    }

    ApplicationScaffold(
        bottomBar = {
            NavigationBar {
                AppRoute.entries.forEach { route ->
                    NavigationBarItem(
                        selected = selectedRoute == route,
                        onClick = {
                            onRouteSelected(route)
                            navigator.resetTo(route)
                        },
                        label = {
                            AppText(route.title, AppTextStyle.NAVIGATION_LABEL)
                        },
                        icon = { },
                    )
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
                entry<AppRoute.Storage> {
                    StorageScreenRoot()
                }
                entry<AppRoute.Flows> {
                    FlowDelegatesScreenRoot()
                }
                entry<AppRoute.CustomJson> {
                    CustomJsonScreenRoot()
                }
                entry<AppRoute.Security> {
                    SecurityScreenRoot()
                }
                entry<AppRoute.Preferences> {
                    PreferencesScreenRoot()
                }
            },
        )
    }
}
