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
 * The receiver resolves dependencies from the already-created application Koin graph. KSafe
 * cache readiness is GUARANTEED before this lambda runs — the loader owns that barrier — so the
 * lambda may freely use KSafe-backed repositories and contains only application work:
 *
 * ```
 * App(
 *     preload = {
 *         get<RemoteConfigRepository>().preload()
 *         get<CatalogRepository>().warm()
 *     },
 * )
 * ```
 *
 * The whole preload (barrier included) runs inside the startup timeout, and a throw becomes the
 * retryable [AppStartupState.Failed]. Independent work may overlap inside the lambda with
 * `coroutineScope { }` + `async`.
 */
typealias AppPreload = suspend AppPreloadScope.() -> Unit

class AppPreloadScope internal constructor(
    @PublishedApi
    internal val koin: Koin,
) {
    inline fun <reified T : Any> get(qualifier: Qualifier? = null): T =
        koin.get(qualifier = qualifier)
}

/**
 * The startup pipeline, in its guaranteed order:
 *
 * 1. KSafe cache readiness — the loader's OWN first step, never the caller's. A barrier that
 *    the preload lambda must remember to call is a guarantee that dies with one forgotten
 *    line, and the theme/language reads below depend on it regardless of what the lambda does.
 *    (Immediate on Android/Apple/JVM; JS/WasmJS suspend until IndexedDB/WebCrypto hydration.)
 * 2. The caller-owned [AppPreload] lambda.
 * 3. Persisted theme and language reads — safe now on every target.
 */
internal class DefaultAppStartupLoader(
    private val themePreferenceRepository: ThemePreferenceRepository,
    private val appLanguageStore: AppLanguageStore,
    private val preloadScope: AppPreloadScope,
    private val awaitStoresReady: suspend () -> Unit,
) : AppStartupLoader {
    override suspend fun load(preload: AppPreload): AppStartupPreferences {
        awaitStoresReady()
        preloadScope.preload()

        return AppStartupPreferences(
            themeMode = themePreferenceRepository.themeMode.first(),
            language = LocalizationManager.resolveStartup(appLanguageStore.languageCode),
        )
    }
}
