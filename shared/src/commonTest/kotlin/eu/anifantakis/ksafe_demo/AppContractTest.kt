package eu.anifantakis.ksafe_demo

import eu.anifantakis.ksafe_demo.app.navigation.AppRoute
import eu.anifantakis.ksafe_demo.core.presentation.design_system.resolveDarkTheme
import eu.anifantakis.ksafe_demo.features.custom_json.data.serialization.customJsonForKSafe
import eu.anifantakis.ksafe_demo.features.custom_json.domain.model.HexColor
import eu.anifantakis.ksafe_demo.features.custom_json.domain.model.Timestamp
import eu.anifantakis.ksafe_demo.features.custom_json.domain.model.UserProfile
import eu.anifantakis.ksafe_demo.features.preferences.domain.model.ThemeMode
import kotlin.test.Test
import kotlin.test.assertEquals

class AppContractTest {
    @Test
    fun routesHaveStableUniqueTitles() {
        assertEquals(
            expected = listOf("Storage", "Flows", "Custom JSON", "Security", "Preferences"),
            actual = AppRoute.entries.map(AppRoute::title),
        )
        assertEquals(
            expected = AppRoute.entries.size,
            actual = AppRoute.entries.distinct().size,
        )
    }

    @Test
    fun customJsonRoundTripsContextualTypes() {
        val profile = UserProfile(
            name = "Ada",
            createdAt = Timestamp(123_456L),
            favoriteColor = HexColor("#33B5FF"),
        )

        val encoded = customJsonForKSafe.encodeToString(profile)
        val decoded = customJsonForKSafe.decodeFromString<UserProfile>(encoded)

        assertEquals(profile, decoded)
    }

    @Test
    fun themeModeResolvesDayNightAndSystem() {
        assertEquals(false, ThemeMode.DAY.resolveDarkTheme(systemIsDark = true))
        assertEquals(true, ThemeMode.NIGHT.resolveDarkTheme(systemIsDark = false))
        assertEquals(false, ThemeMode.SYSTEM.resolveDarkTheme(systemIsDark = false))
        assertEquals(true, ThemeMode.SYSTEM.resolveDarkTheme(systemIsDark = true))
    }
}
