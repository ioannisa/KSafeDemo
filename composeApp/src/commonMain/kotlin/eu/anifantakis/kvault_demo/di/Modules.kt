package eu.anifantakis.kvault_demo.di

import eu.anifantakis.kvault_demo.screens.counters.LibCounterViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val sharedModule = module {
    viewModelOf(::LibCounterViewModel)
}