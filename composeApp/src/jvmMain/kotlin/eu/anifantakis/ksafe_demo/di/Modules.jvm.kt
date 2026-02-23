package eu.anifantakis.ksafe_demo.di

import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeConfig
import eu.anifantakis.lib.ksafe.KSafeSecurityPolicy
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
    }