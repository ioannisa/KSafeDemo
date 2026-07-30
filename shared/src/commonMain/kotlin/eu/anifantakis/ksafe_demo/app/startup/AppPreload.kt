package eu.anifantakis.ksafe_demo.app.startup

import eu.anifantakis.ksafe_demo.core.domain.preferences.AppLanguageStore
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.LocalizationManager
import eu.anifantakis.ksafe_demo.features.preferences.domain.repository.ThemePreferenceRepository
import kotlinx.coroutines.flow.first
import org.koin.core.Koin
import org.koin.core.qualifier.Qualifier

/**
 * Application-specific work that must finish before the first usable frame.
 *
 * The receiver resolves dependencies from the already-created application Koin graph.
 */
typealias AppPreload = suspend AppPreloadScope.() -> Unit

class AppPreloadScope internal constructor(
    @PublishedApi
    internal val koin: Koin,
    private val kSafeReady: suspend AppPreloadScope.() -> Unit,
) {
    inline fun <reified T : Any> get(qualifier: Qualifier? = null): T =
        koin.get(qualifier = qualifier)

    /**
     * Resolves every app-lifetime KSafe store and waits until its cache is ready.
     *
     * This is an immediate readiness barrier on Android, Apple, and JVM. JS and WasmJS suspend
     * until IndexedDB/WebCrypto hydration has completed.
     */
    suspend fun awaitKSafeReady() {
        kSafeReady()
    }
}

/**
 * Executes the caller-owned preload before resolving first-frame preferences.
 */
internal class DefaultAppStartupLoader(
    private val themePreferenceRepository: ThemePreferenceRepository,
    private val appLanguageStore: AppLanguageStore,
    private val preloadScope: AppPreloadScope,
) : AppStartupLoader {
    override suspend fun load(preload: AppPreload): AppStartupPreferences {
        preloadScope.preload()

        return AppStartupPreferences(
            themeMode = themePreferenceRepository.themeMode.first(),
            language = LocalizationManager.resolveStartup(appLanguageStore.languageCode),
        )
    }
}
