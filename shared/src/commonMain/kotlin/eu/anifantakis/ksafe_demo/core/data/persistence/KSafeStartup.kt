package eu.anifantakis.ksafe_demo.core.data.persistence

import eu.anifantakis.lib.ksafe.KSafe

/**
 * Suspends app startup until every app-lifetime [KSafe] store can safely serve synchronous
 * reads from its in-memory cache.
 *
 * JS and WasmJS hydrate encrypted values asynchronously through IndexedDB and WebCrypto, so their
 * actual implementations await each store's cache before Compose reads persisted navigation,
 * preferences, or demo data. Android, Apple, and JVM require no explicit readiness barrier, and
 * their actual implementations return immediately.
 *
 * Call this after dependency injection has created the stores and before rendering content that
 * reads from KSafe. It coordinates existing instances; it does not construct stores, create
 * encryption keys, or perform application migrations.
 *
 * @param defaultStore the main KSafe store used by the demo.
 * @param customJsonStore the isolated store configured with the demo's custom JSON serializer.
 * @param preferencesStore the isolated store containing non-sensitive application preferences.
 */
expect suspend fun awaitKSafeCachesReady(
    defaultStore: KSafe,
    customJsonStore: KSafe,
    preferencesStore: KSafe,
)
