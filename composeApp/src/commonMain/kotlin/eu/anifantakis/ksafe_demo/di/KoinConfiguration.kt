package eu.anifantakis.ksafe_demo.di

import org.koin.dsl.KoinConfiguration

fun createKoinConfiguration(): KoinConfiguration {
    return KoinConfiguration {
        modules(sharedModule, platformModule)
    }
}
