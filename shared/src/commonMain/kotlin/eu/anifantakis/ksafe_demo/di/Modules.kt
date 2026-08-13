package eu.anifantakis.ksafe_demo.di

import eu.anifantakis.ksafe_demo.app.startup.AppStartupCoordinator
import eu.anifantakis.ksafe_demo.app.startup.AppPreloadScope
import eu.anifantakis.ksafe_demo.core.data.persistence.awaitKSafeCachesReady
import eu.anifantakis.ksafe_demo.core.data.preferences.KSafeAppLanguageStore
import eu.anifantakis.ksafe_demo.core.domain.preferences.AppLanguageStore
import eu.anifantakis.ksafe_demo.core.presentation.global_state.GlobalStateContainer
import eu.anifantakis.ksafe_demo.features.about.presentation.AboutViewModel
import eu.anifantakis.ksafe_demo.features.custom_json.presentation.CustomJsonViewModel
import eu.anifantakis.ksafe_demo.features.flows.presentation.FlowDelegatesViewModel
import eu.anifantakis.ksafe_demo.features.helpers.presentation.HelpersViewModel
import eu.anifantakis.ksafe_demo.features.preferences.data.repository.ThemePreferenceRepositoryImpl
import eu.anifantakis.ksafe_demo.features.preferences.domain.repository.ThemePreferenceRepository
import eu.anifantakis.ksafe_demo.features.preferences.presentation.PreferencesViewModel
import eu.anifantakis.ksafe_demo.features.security.presentation.SecurityViewModel
import eu.anifantakis.ksafe_demo.features.counters.presentation.CountersViewModel
import eu.anifantakis.lib.ksafe.KSafeEncrypted
import eu.anifantakis.lib.ksafe.KSafeHardwareIsolated
import eu.anifantakis.lib.ksafe.KSafePlain
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
    single<AppLanguageStore> {
        KSafeAppLanguageStore(get(preferencesKSafe))
    }
    single<ThemePreferenceRepository> {
        ThemePreferenceRepositoryImpl(get(preferencesKSafe))
    }
    // The whole "what does the splash wait for" in one place: the coordinator runs
    // awaitStoresReady → the App(preload = …) lambda → the theme/language reads below.
    single {
        AppStartupCoordinator(
            themePreferenceRepository = get(),
            appLanguageStore = get(),
            preloadScope = AppPreloadScope(koin = getKoin()),
            awaitStoresReady = {
                awaitKSafeCachesReady(
                    defaultStore = get(),
                    customJsonStore = get(customJsonKSafe),
                    preferencesStore = get(preferencesKSafe),
                )
            },
        )
    }

    // 3.1.0 mode-typed views over the default KSafe store: the handle IS the write mode.
    single { KSafePlain(get()) }
    single { KSafeEncrypted(get()) }
    single { KSafeHardwareIsolated(get()) }

    viewModelOf(::CountersViewModel)
    viewModelOf(::FlowDelegatesViewModel)
    viewModelOf(::HelpersViewModel)
    viewModelOf(::PreferencesViewModel)
    viewModelOf(::AboutViewModel)

    // SecurityViewModel - initialized with violations from the holder
    viewModelOf(::SecurityViewModel)

    // CustomJsonViewModel - uses the named KSafe instance with custom Json
    viewModel { CustomJsonViewModel(get(customJsonKSafe)) }
}
