package eu.anifantakis.ksafe_demo.di

import eu.anifantakis.ksafe_demo.core.presentation.global_state.GlobalStateContainer
import eu.anifantakis.ksafe_demo.features.about.presentation.screens.about.AboutViewModel
import eu.anifantakis.ksafe_demo.features.custom_json.presentation.screens.custom_json.CustomJsonViewModel
import eu.anifantakis.ksafe_demo.features.flows.presentation.screens.flow_delegates.FlowDelegatesViewModel
import eu.anifantakis.ksafe_demo.features.preferences.data.repository.ThemePreferenceRepositoryImpl
import eu.anifantakis.ksafe_demo.features.preferences.domain.repository.ThemePreferenceRepository
import eu.anifantakis.ksafe_demo.features.preferences.presentation.screens.preferences.PreferencesViewModel
import eu.anifantakis.ksafe_demo.features.security.presentation.screens.security.SecurityViewModel
import eu.anifantakis.ksafe_demo.features.counters.presentation.screens.counters.CountersViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

expect val platformModule: Module

/** Qualifier for the KSafe instance configured with custom Json. */
val customJsonKSafe = named("customJsonKSafe")

/** Qualifier for the plain KSafe store used by non-sensitive app preferences. */
val preferencesKSafe = named("preferencesKSafe")

val sharedModule = module {
    single { GlobalStateContainer() }
    single<ThemePreferenceRepository> {
        ThemePreferenceRepositoryImpl(get(preferencesKSafe))
    }

    viewModelOf(::CountersViewModel)
    viewModelOf(::FlowDelegatesViewModel)
    viewModelOf(::PreferencesViewModel)
    viewModelOf(::AboutViewModel)

    // SecurityViewModel - initialized with violations from the holder
    viewModelOf(::SecurityViewModel)

    // CustomJsonViewModel - uses the named KSafe instance with custom Json
    viewModel { CustomJsonViewModel(get(customJsonKSafe)) }
}
