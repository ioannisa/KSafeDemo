package eu.anifantakis.ksafe_demo.di

import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeConfig
import eu.anifantakis.lib.ksafe.KSafeSecurityPolicy
import eu.anifantakis.lib.ksafe.SecurityAction
import eu.anifantakis.ksafe_demo.screens.customjson.customJsonForKSafe
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<KSafe> {
            KSafe(
                context = androidApplication(),
                config = KSafeConfig(requireUnlockedDevice = false),
                securityPolicy = KSafeSecurityPolicy.Strict.copy( // If in rooted device will crash (use WarnOnly to run in rooted phones)
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
        // another KSafe instance demo that uses Custom JSON serialization via KSafeConfig(json = ...)
        // which was introduced in KSafe version 1.7.1
        // Separate fileName ensures this instance has its own storage namespace
        single<KSafe>(customJsonKSafe) {
            KSafe(
                context = androidApplication(),
                fileName = "androiddata_customjson",
                config = KSafeConfig(json = customJsonForKSafe)
            )
        }
    }