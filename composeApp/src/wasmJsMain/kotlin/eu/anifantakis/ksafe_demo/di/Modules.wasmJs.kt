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
                fileName = "wasmdata",
                config = KSafeConfig(requireUnlockedDevice = false),
                securityPolicy = KSafeSecurityPolicy.Strict.copy( // If in jailbroken device will crash (use WarnOnly to run in jailbroken iPhones)
                    debuggerAttached = SecurityAction.WARN,   // Change to BLOCK and if debugger attached, app will crash
                    debugBuild = SecurityAction.WARN, // Change to BLOCK in debug and app will crash
                    emulator = SecurityAction.WARN,   // Change to BLOCK and if you run from emulator, app will crash
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
                fileName = "wasmdata_customjson",
                config = KSafeConfig(json = customJsonForKSafe)
            )
        }
    }
