package eu.anifantakis.ksafe_demo.app.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.navigation3.runtime.NavKey

class Navigator(start: NavKey) {
    val backStack = mutableStateListOf<NavKey>(start)

    fun navigate(route: NavKey) {
        backStack.add(route)
    }

    fun goBack() {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }

    fun resetTo(route: NavKey) {
        backStack.clear()
        backStack.add(route)
    }

    fun current(): NavKey? = backStack.lastOrNull()
}

