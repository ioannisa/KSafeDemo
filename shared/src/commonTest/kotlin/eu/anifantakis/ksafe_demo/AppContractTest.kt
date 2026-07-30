package eu.anifantakis.ksafe_demo

import eu.anifantakis.ksafe_demo.app.navigation.AppRoute
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey
import eu.anifantakis.ksafe_demo.core.presentation.design_system.resolveDarkTheme
import eu.anifantakis.ksafe_demo.features.custom_json.data.serialization.customJsonForKSafe
import eu.anifantakis.ksafe_demo.features.custom_json.domain.model.HexColor
import eu.anifantakis.ksafe_demo.features.custom_json.domain.model.Timestamp
import eu.anifantakis.ksafe_demo.features.custom_json.domain.model.UserProfile
import eu.anifantakis.ksafe_demo.features.preferences.domain.model.ThemeMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class AppContractTest {
    @Test
    fun routesHaveStableUniqueTitleKeys() {
        assertEquals(
            expected = listOf(
                StringKey.NAV_COUNTERS,
                StringKey.NAV_FLOWS,
                StringKey.NAV_CUSTOM_JSON,
                StringKey.NAV_SECURITY,
            ),
            actual = AppRoute.bottomNavigationEntries.map(AppRoute::titleKey),
        )
        assertEquals(
            expected = AppRoute.bottomNavigationEntries.size,
            actual = AppRoute.bottomNavigationEntries.distinct().size,
        )
        assertFalse(AppRoute.Preferences in AppRoute.bottomNavigationEntries)
        assertFalse(AppRoute.About in AppRoute.bottomNavigationEntries)
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
