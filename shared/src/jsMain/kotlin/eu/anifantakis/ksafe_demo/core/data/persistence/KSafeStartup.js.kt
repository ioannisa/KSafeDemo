package eu.anifantakis.ksafe_demo.core.data.persistence

import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.awaitCacheReady

actual suspend fun awaitKSafeCachesReady(
    defaultStore: KSafe,
    customJsonStore: KSafe,
    preferencesStore: KSafe,
) {
    defaultStore.awaitCacheReady()
    customJsonStore.awaitCacheReady()
    preferencesStore.awaitCacheReady()
}
