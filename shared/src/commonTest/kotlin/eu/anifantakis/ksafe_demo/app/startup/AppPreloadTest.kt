package eu.anifantakis.ksafe_demo.app.startup

import eu.anifantakis.ksafe_demo.core.domain.preferences.AppLanguageStore
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.Language
import eu.anifantakis.ksafe_demo.features.preferences.domain.model.ThemeMode
import eu.anifantakis.ksafe_demo.features.preferences.domain.repository.ThemePreferenceRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class AppPreloadTest {
    @Test
    fun preloadScopeResolvesDependenciesFromTheApplicationGraph() {
        val application = koinApplication {
            modules(
                module {
                    single { PreloadDependency(value = "ready") }
                },
            )
        }

        try {
            val scope = AppPreloadScope(koin = application.koin)

            assertEquals("ready", scope.get<PreloadDependency>().value)
        } finally {
            application.close()
        }
    }

    /**
     * The contract the pipeline exists for: readiness is the LOADER's own first step, so an
     * empty (or barrier-oblivious) preload lambda still gets safe preference reads.
     */
    @Test
    fun barrierRunsEvenWhenThePreloadLambdaIgnoresIt() = runTest {
        val application = koinApplication()
        var kSafeReady = false
        val loader = DefaultAppStartupLoader(
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

        try {
            val preferences = loader.load(preload = { /* no barrier call — and none needed */ })

            assertEquals(ThemeMode.NIGHT, preferences.themeMode)
            assertEquals(Language.EN, preferences.language)
        } finally {
            application.close()
        }
    }

    /** A hung or failing barrier surfaces as a loader failure (→ Failed + retry), not a hang past the reads. */
    @Test
    fun barrierFailurePropagatesBeforeAnyPreferenceRead() = runTest {
        val application = koinApplication()
        var readsHappened = false
        val loader = DefaultAppStartupLoader(
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

        try {
            assertFailsWith<IllegalStateException> { loader.load(preload = {}) }
            assertEquals(false, readsHappened, "no preference read may precede the barrier")
        } finally {
            application.close()
        }
    }

    private data class PreloadDependency(
        val value: String,
    )
}
