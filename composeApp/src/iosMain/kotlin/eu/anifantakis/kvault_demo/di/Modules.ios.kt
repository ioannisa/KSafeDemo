package eu.anifantakis.kvault_demo.di

import eu.anifantakis.lib.kvault.KVault
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<KVault> {
            KVault()
        }
    }