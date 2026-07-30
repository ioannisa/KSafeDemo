package eu.anifantakis.ksafe_demo.app.navigation

import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
sealed interface AppRoute : NavKey {
    val title: String

    @Serializable
    @SerialName("eu.anifantakis.ksafe_demo.app.navigation.AppRoute.Storage")
    data object Counters : AppRoute {
        override val title = "Counters"
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

    @Serializable
    data object About : AppRoute {
        override val title = "About"
    }

    companion object {
        val bottomNavigationEntries: List<AppRoute> =
            listOf(Counters, Flows, CustomJson, Security)
    }
}
