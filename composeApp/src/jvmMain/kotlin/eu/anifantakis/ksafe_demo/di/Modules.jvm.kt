package eu.anifantakis.ksafe_demo.di

import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeConfig
import eu.anifantakis.lib.ksafe.KSafeSecurityPolicy
import eu.anifantakis.ksafe_demo.screens.customjson.customJsonForKSafe
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<KSafe> {
            KSafe(
                fileName = "desktopdata",
                config = KSafeConfig(requireUnlockedDevice = false),
                securityPolicy = KSafeSecurityPolicy.WarnOnly.copy(
                    onViolation = { violation ->
                        SecurityViolationsHolder.addViolation(violation)
                    }
                )
            )
        }

        // another KSafe instance demo that uses Custom JSON serialization via KSafeConfig(json = ...)
        // which was introduced in KSafe version 1.7.1
        single<KSafe>(customJsonKSafe) {
            KSafe(
                fileName = "desktopdata_customjson",
                config = KSafeConfig(json = customJsonForKSafe)
            )
        }
    }