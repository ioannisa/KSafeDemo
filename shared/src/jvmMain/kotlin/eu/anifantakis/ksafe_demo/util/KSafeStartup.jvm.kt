package eu.anifantakis.ksafe_demo.util

import eu.anifantakis.lib.ksafe.KSafe

actual suspend fun awaitKSafeCachesReady(
    defaultStore: KSafe,
    customJsonStore: KSafe,
    preferencesStore: KSafe,
) = Unit
