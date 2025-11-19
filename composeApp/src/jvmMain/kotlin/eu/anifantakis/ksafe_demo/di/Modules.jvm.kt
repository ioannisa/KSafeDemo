package eu.anifantakis.ksafe_demo.di

import eu.anifantakis.lib.ksafe.KSafe
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<KSafe> {
            KSafe(fileName = "desktopdata")
        }
    }