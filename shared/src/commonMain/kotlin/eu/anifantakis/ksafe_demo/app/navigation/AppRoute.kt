package eu.anifantakis.ksafe_demo.app.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoute : NavKey {
    val title: String

    @Serializable
    data object Storage : AppRoute {
        override val title = "Storage"
    }

    @Serializable
    data object Flows : AppRoute {
        override val title = "Flows"
    }

    @Serializable
    data object CustomJson : AppRoute {
        override val title = "Custom JSON"
    }

    @Serializable
    data object Security : AppRoute {
        override val title = "Security"
    }

    @Serializable
    data object Preferences : AppRoute {
        override val title = "Preferences"
    }

    companion object {
        val entries: List<AppRoute> = listOf(Storage, Flows, CustomJson, Security, Preferences)
    }
}
