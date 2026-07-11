package eu.anifantakis.ksafe_demo.di

import eu.anifantakis.ksafe_demo.screens.counters.LibCounterViewModel
import eu.anifantakis.ksafe_demo.screens.customjson.CustomJsonViewModel
import eu.anifantakis.ksafe_demo.screens.flows.FlowDelegatesViewModel
import eu.anifantakis.ksafe_demo.screens.security.SecurityViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

expect val platformModule: Module

/** Qualifier for the KSafe instance configured with custom Json. */
val customJsonKSafe = named("customJsonKSafe")

val sharedModule = module {
    viewModelOf(::LibCounterViewModel)
    viewModelOf(::FlowDelegatesViewModel)

    // SecurityViewModel - initialized with violations from the holder
    viewModelOf(::SecurityViewModel)

    // CustomJsonViewModel - uses the named KSafe instance with custom Json
    viewModel { CustomJsonViewModel(get(customJsonKSafe)) }
}