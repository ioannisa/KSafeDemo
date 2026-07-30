package eu.anifantakis.ksafe_demo.app.navigation

import androidx.compose.runtime.Immutable
import androidx.navigation3.runtime.NavKey
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Immutable
@Serializable
sealed interface AppRoute : NavKey {
    val titleKey: StringKey

    @Serializable
    @SerialName("eu.anifantakis.ksafe_demo.app.navigation.AppRoute.Storage")
    data object Counters : AppRoute {
        override val titleKey get() = StringKey.NAV_COUNTERS
    }

    @Serializable
    data object Flows : AppRoute {
        override val titleKey get() = StringKey.NAV_FLOWS
    }

    @Serializable
    data object CustomJson : AppRoute {
        override val titleKey get() = StringKey.NAV_CUSTOM_JSON
    }

    @Serializable
    data object Security : AppRoute {
        override val titleKey get() = StringKey.NAV_SECURITY
    }

    @Serializable
    data object Preferences : AppRoute {
        override val titleKey get() = StringKey.COMMON_PREFERENCES
    }

    @Serializable
    data object About : AppRoute {
        override val titleKey get() = StringKey.ABOUT_TITLE
    }

    companion object {
        val bottomNavigationEntries: List<AppRoute> =
            listOf(Counters, Flows, CustomJson, Security)
    }
}
