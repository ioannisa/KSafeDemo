package eu.anifantakis.ksafe_demo.di

import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeConfig
import eu.anifantakis.lib.ksafe.KSafeSecurityPolicy
import eu.anifantakis.lib.ksafe.SecurityAction
import eu.anifantakis.ksafe_demo.screens.customjson.customJsonForKSafe
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<KSafe> {
            KSafe(
                fileName = "jsdata",
                config = KSafeConfig(requireUnlockedDevice = false),
                securityPolicy = KSafeSecurityPolicy.Strict.copy(
                    debuggerAttached = SecurityAction.WARN,
                    debugBuild = SecurityAction.WARN,
                    emulator = SecurityAction.WARN,
                    onViolation = { violation ->
                        SecurityViolationsHolder.addViolation(violation)
                    }
                )
            )
        }

        single<KSafe>(customJsonKSafe) {
            KSafe(
                fileName = "jsdata_customjson",
                config = KSafeConfig(json = customJsonForKSafe)
            )
        }
    }