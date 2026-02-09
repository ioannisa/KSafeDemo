package eu.anifantakis.ksafe_demo.di

import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeConfig
import eu.anifantakis.lib.ksafe.KSafeSecurityPolicy
import eu.anifantakis.lib.ksafe.SecurityAction
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<KSafe> {
            KSafe(
                config = KSafeConfig(requireUnlockedDevice = true),
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
    }