package eu.anifantakis.ksafe_demo.app.startup

import eu.anifantakis.ksafe_demo.core.domain.preferences.AppLanguageStore
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.Language
import eu.anifantakis.ksafe_demo.features.preferences.domain.model.ThemeMode
import eu.anifantakis.ksafe_demo.features.preferences.domain.repository.ThemePreferenceRepository
import kotlin.test.Test
import kotlin.test.assertEquals
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
            val scope = AppPreloadScope(
                koin = application.koin,
                kSafeReady = {},
            )

            assertEquals("ready", scope.get<PreloadDependency>().value)
        } finally {
            application.close()
        }
    }

    @Test
    fun awaitKSafeReadyRunsTheConfiguredReadinessBarrier() = runTest {
        val application = koinApplication()
        var readinessCalls = 0
        val scope = AppPreloadScope(
            koin = application.koin,
            kSafeReady = {
                readinessCalls++
            },
        )

        try {
            scope.awaitKSafeReady()

            assertEquals(1, readinessCalls)
        } finally {
            application.close()
        }
    }

    @Test
    fun loaderRunsTheCallerPreloadBeforeReadingStartupPreferences() = runTest {
        val application = koinApplication()
        var kSafeReady = false
        val scope = AppPreloadScope(
            koin = application.koin,
            kSafeReady = {
                kSafeReady = true
            },
        )
        val loader = DefaultAppStartupLoader(
            themePreferenceRepository =
                object : ThemePreferenceRepository {
                    override val themeMode =
                        flow {
                            assertTrue(kSafeReady)
                            emit(ThemeMode.NIGHT)
                        }

                    override fun setThemeMode(themeMode: ThemeMode) = Unit
                },
            appLanguageStore =
                object : AppLanguageStore {
                    override var languageCode: String
                        get() {
                            assertTrue(kSafeReady)
                            return "en"
                        }
                        set(_) = Unit
                },
            preloadScope = scope,
        )

        try {
            val preferences = loader.load {
                awaitKSafeReady()
            }

            assertEquals(ThemeMode.NIGHT, preferences.themeMode)
            assertEquals(Language.EN, preferences.language)
        } finally {
            application.close()
        }
    }

    private data class PreloadDependency(
        val value: String,
    )
}
