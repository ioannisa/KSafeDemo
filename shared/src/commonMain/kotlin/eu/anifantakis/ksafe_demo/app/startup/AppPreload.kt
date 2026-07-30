package eu.anifantakis.ksafe_demo.app.startup

import org.koin.core.Koin
import org.koin.core.qualifier.Qualifier

/**
 * Application-specific work that must finish before the first usable frame.
 *
 * This is the ONE seam an app customizes at startup. It reaches [AppStartupCoordinator] through
 * `initialize(preload = …)` and runs as step 2 of the coordinator's pipeline — AFTER the KSafe
 * cache-readiness barrier (guaranteed, never the lambda's job) and BEFORE the theme/language
 * reads. So the lambda may freely use KSafe-backed repositories and contains only application
 * work:
 *
 * ```
 * App(
 *     preload = {
 *         // illustrative names — these stand for your app's own classes
 *         get<RemoteConfigRepository>().preload()
 *         get<CatalogRepository>().warm()
 *     },
 * )
 * ```
 *
 * The whole pipeline (barrier included) runs inside the startup timeout, and a throw becomes the
 * retryable [AppStartupState.Failed]. Independent work may overlap inside the lambda with
 * `coroutineScope { }` + `async`.
 */
typealias AppPreload = suspend AppPreloadScope.() -> Unit

/**
 * Deliberately exposes ONLY dependency resolution from the application Koin graph. Storage
 * readiness is not offered because it is not the lambda's job — the coordinator's pipeline has
 * already awaited it by the time the lambda runs.
 */
class AppPreloadScope internal constructor(
    @PublishedApi
    internal val koin: Koin,
) {
    inline fun <reified T : Any> get(qualifier: Qualifier? = null): T =
        koin.get(qualifier = qualifier)
}
