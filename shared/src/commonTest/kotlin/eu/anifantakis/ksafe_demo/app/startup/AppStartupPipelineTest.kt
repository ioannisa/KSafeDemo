package eu.anifantakis.ksafe_demo.app.startup

import eu.anifantakis.ksafe_demo.core.domain.preferences.AppLanguageStore
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.Language
import eu.anifantakis.ksafe_demo.features.preferences.domain.model.ThemeMode
import eu.anifantakis.ksafe_demo.features.preferences.domain.repository.ThemePreferenceRepository
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest

class AppStartupPipelineTest {
    @Test
    fun prerequisitesFinishBeforeCriticalTasksStart() = runTest {
        var prerequisiteFinished = false
        val pipeline = AppStartupPipeline(
            tasks = listOf(
                TestStartupTask(
                    id = "storage",
                    phase = AppStartupPhase.PREREQUISITE,
                ) {
                    prerequisiteFinished = true
                },
                TestStartupTask(
                    id = "content",
                    phase = AppStartupPhase.CRITICAL,
                ) {
                    assertTrue(prerequisiteFinished)
                },
            ),
        )

        pipeline.preload()
    }

    @Test
    fun tasksInTheSamePhaseRunConcurrently() = runTest {
        val firstStarted = CompletableDeferred<Unit>()
        val secondStarted = CompletableDeferred<Unit>()
        val releaseTasks = CompletableDeferred<Unit>()
        val pipeline = AppStartupPipeline(
            tasks = listOf(
                TestStartupTask(
                    id = "first",
                    phase = AppStartupPhase.CRITICAL,
                ) {
                    firstStarted.complete(Unit)
                    secondStarted.await()
                    releaseTasks.await()
                },
                TestStartupTask(
                    id = "second",
                    phase = AppStartupPhase.CRITICAL,
                ) {
                    secondStarted.complete(Unit)
                    firstStarted.await()
                    releaseTasks.await()
                },
            ),
        )

        val preload = launch {
            pipeline.preload()
        }
        firstStarted.await()
        secondStarted.await()
        assertFalse(preload.isCompleted)

        releaseTasks.complete(Unit)
        preload.join()
        assertTrue(preload.isCompleted)
    }

    @Test
    fun failureIdentifiesTheTaskThatFailed() = runTest {
        val pipeline = AppStartupPipeline(
            tasks = listOf(
                TestStartupTask(
                    id = "remote_config",
                    phase = AppStartupPhase.CRITICAL,
                ) {
                    error("unavailable")
                },
            ),
        )

        val error =
            try {
                pipeline.preload()
                error("Expected startup pipeline to fail")
            } catch (error: AppStartupTaskException) {
                error
            }

        assertEquals("remote_config", error.taskId)
        assertEquals("unavailable", error.cause?.message)
    }

    @Test
    fun duplicateTaskIdsAreRejected() {
        val task =
            TestStartupTask(
                id = "duplicate",
                phase = AppStartupPhase.CRITICAL,
                preload = {},
            )

        val error = assertFailsWith<IllegalArgumentException> {
            AppStartupPipeline(tasks = listOf(task, task))
        }

        assertTrue(error.message.orEmpty().contains("duplicate"))
    }

    @Test
    fun startupPreferencesAreLoadedByACriticalTask() = runTest {
        val task = StartupPreferencesTask(
            themePreferenceRepository =
                FakeThemePreferenceRepository(
                    initialThemeMode = ThemeMode.NIGHT,
                ),
            appLanguageStore =
                FakeAppLanguageStore(
                    initialLanguageCode = Language.EL.code,
                ),
        )

        task.preload()

        assertEquals(AppStartupPhase.CRITICAL, task.phase)
        assertEquals(
            AppStartupPreferences(
                themeMode = ThemeMode.NIGHT,
                language = Language.EL,
            ),
            task.requireLoadedPreferences(),
        )
    }

    private class TestStartupTask(
        override val id: String,
        override val phase: AppStartupPhase,
        private val preload: suspend () -> Unit,
    ) : AppStartupTask {
        override suspend fun preload() {
            preload.invoke()
        }
    }

    private class FakeThemePreferenceRepository(
        initialThemeMode: ThemeMode,
    ) : ThemePreferenceRepository {
        override val themeMode: Flow<ThemeMode> = flowOf(initialThemeMode)

        override fun setThemeMode(themeMode: ThemeMode) = Unit
    }

    private class FakeAppLanguageStore(
        initialLanguageCode: String,
    ) : AppLanguageStore {
        override var languageCode: String = initialLanguageCode
    }
}
