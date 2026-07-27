package eu.anifantakis.ksafe_demo.util

import eu.anifantakis.lib.ksafe.KSafe

expect suspend fun awaitKSafeCachesReady(
    defaultStore: KSafe,
    customJsonStore: KSafe,
    preferencesStore: KSafe,
)
