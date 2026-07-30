package eu.anifantakis.ksafe_demo.app.startup

import eu.anifantakis.ksafe_demo.core.domain.preferences.AppLanguageStore
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.Language
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.LocalizationManager
import eu.anifantakis.ksafe_demo.features.preferences.domain.model.ThemeMode
import eu.anifantakis.ksafe_demo.features.preferences.domain.repository.ThemePreferenceRepository
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class AppPreloadTest {
    private val application = koinApplication {
        modules(
            module {
                single { PreloadDependency(value = "ready") }
            },
        )
    }

    @AfterTest
    fun tearDown() {
        application.close()
        LocalizationManager.setLanguage(Language.FALLBACK)
    }

    @Test
    fun preloadScopeResolvesDependenciesFromTheApplicationGraph() {
        val scope = AppPreloadScope(koin = application.koin)

        assertEquals("ready", scope.get<PreloadDependency>().value)
    }

    /**
     * The contract the pipeline exists for: readiness is the COORDINATOR's own first step, so an
     * empty (or barrier-oblivious) preload lambda still gets safe preference reads.
     */
    @Test
    fun barrierRunsEvenWhenThePreloadLambdaIgnoresIt() = runTest {
        var kSafeReady = false
        val coordinator = AppStartupCoordinator(
            themePreferenceRepository =
                object : ThemePreferenceRepository {
                    override val themeMode =
                        flow {
                            assertTrue(kSafeReady, "theme read before KSafe readiness")
                            emit(ThemeMode.NIGHT)
                        }

                    override fun setThemeMode(themeMode: ThemeMode) = Unit
                },
            appLanguageStore =
                object : AppLanguageStore {
                    override var languageCode: String
                        get() {
                            assertTrue(kSafeReady, "language read before KSafe readiness")
                            return "en"
                        }
                        set(_) = Unit
                },
            preloadScope = AppPreloadScope(koin = application.koin),
            awaitStoresReady = { kSafeReady = true },
        )

        coordinator.initialize(preload = { /* no barrier call — and none needed */ })

        assertEquals(AppStartupState.Ready(ThemeMode.NIGHT), coordinator.state.value)
        assertEquals(Language.EN, LocalizationManager.current)
    }

    /** A failing barrier surfaces as the retryable [AppStartupState.Failed], never as a read past it. */
    @Test
    fun barrierFailurePropagatesBeforeAnyPreferenceRead() = runTest {
        var readsHappened = false
        val coordinator = AppStartupCoordinator(
            themePreferenceRepository =
                object : ThemePreferenceRepository {
                    override val themeMode = flow {
                        readsHappened = true
                        emit(ThemeMode.DAY)
                    }

                    override fun setThemeMode(themeMode: ThemeMode) = Unit
                },
            appLanguageStore =
                object : AppLanguageStore {
                    override var languageCode: String = "en"
                },
            preloadScope = AppPreloadScope(koin = application.koin),
            awaitStoresReady = { error("hydration failed") },
        )

        coordinator.initialize(preload = {})

        assertEquals(AppStartupState.Failed, coordinator.state.value)
        assertFalse(readsHappened, "no preference read may precede the barrier")
    }

    private data class PreloadDependency(
        val value: String,
    )
}
