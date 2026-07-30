package eu.anifantakis.ksafe_demo.app.startup

import eu.anifantakis.ksafe_demo.core.data.persistence.awaitKSafeCachesReady
import eu.anifantakis.ksafe_demo.core.domain.preferences.AppLanguageStore
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.LocalizationManager
import eu.anifantakis.ksafe_demo.features.preferences.domain.repository.ThemePreferenceRepository
import eu.anifantakis.lib.ksafe.KSafe
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.first

/**
 * Defines when a startup task runs.
 *
 * All [PREREQUISITE] tasks finish before any [CRITICAL] task starts. Tasks within the same phase
 * run concurrently.
 */
internal enum class AppStartupPhase {
    PREREQUISITE,
    CRITICAL,
}

/**
 * One idempotent unit of work required before the first usable application frame.
 *
 * Tasks may be retried after a startup failure. A task must therefore be safe to execute more
 * than once and should suspend instead of blocking the UI thread.
 */
internal interface AppStartupTask {
    val id: String
    val phase: AppStartupPhase

    suspend fun preload()
}

internal class AppStartupTaskException(
    val taskId: String,
    cause: Throwable,
) : Exception("Startup task '$taskId' failed", cause)

/**
 * Runs startup phases in order and the independent tasks of each phase concurrently.
 */
internal class AppStartupPipeline(
    tasks: List<AppStartupTask>,
) {
    private val tasksByPhase = tasks.groupBy(AppStartupTask::phase)

    init {
        val duplicateIds =
            tasks
                .groupingBy(AppStartupTask::id)
                .eachCount()
                .filterValues { count -> count > 1 }
                .keys
                .sorted()

        require(duplicateIds.isEmpty()) {
            "Duplicate startup task ids: ${duplicateIds.joinToString()}"
        }
    }

    suspend fun preload() {
        AppStartupPhase.entries.forEach { phase ->
            coroutineScope {
                tasksByPhase[phase]
                    .orEmpty()
                    .map { task ->
                        async {
                            task.preloadSafely()
                        }
                    }
                    .awaitAll()
            }
        }
    }

    private suspend fun AppStartupTask.preloadSafely() {
        try {
            preload()
        } catch (error: CancellationException) {
            currentCoroutineContext().ensureActive()
            throw AppStartupTaskException(
                taskId = id,
                cause = error,
            )
        } catch (error: Exception) {
            throw AppStartupTaskException(
                taskId = id,
                cause = error,
            )
        }
    }
}

/**
 * Hydrates every app-lifetime KSafe cache before later tasks perform synchronous reads.
 *
 * This is real asynchronous work on JS/WasmJS and an immediate no-op barrier on Android, Apple,
 * and JVM, where KSafe is already ready after construction.
 */
internal class KSafeCachesStartupTask(
    private val defaultStore: KSafe,
    private val customJsonStore: KSafe,
    private val preferencesStore: KSafe,
) : AppStartupTask {
    override val id: String = "ksafe_caches"
    override val phase: AppStartupPhase = AppStartupPhase.PREREQUISITE

    override suspend fun preload() {
        awaitKSafeCachesReady(
            defaultStore = defaultStore,
            customJsonStore = customJsonStore,
            preferencesStore = preferencesStore,
        )
    }
}

/**
 * Resolves the persisted values required by the first rendered application frame.
 */
internal class StartupPreferencesTask(
    private val themePreferenceRepository: ThemePreferenceRepository,
    private val appLanguageStore: AppLanguageStore,
) : AppStartupTask {
    override val id: String = "startup_preferences"
    override val phase: AppStartupPhase = AppStartupPhase.CRITICAL

    private var loadedPreferences: AppStartupPreferences? = null

    override suspend fun preload() {
        loadedPreferences = AppStartupPreferences(
            themeMode = themePreferenceRepository.themeMode.first(),
            language = LocalizationManager.resolveStartup(appLanguageStore.languageCode),
        )
    }

    fun requireLoadedPreferences(): AppStartupPreferences =
        checkNotNull(loadedPreferences) {
            "Startup preferences were requested before their preload task completed"
        }
}

/**
 * Adapts the task pipeline to the coordinator's startup loader contract.
 */
internal class PipelineAppStartupLoader(
    private val pipeline: AppStartupPipeline,
    private val startupPreferencesTask: StartupPreferencesTask,
) : AppStartupLoader {
    override suspend fun load(): AppStartupPreferences {
        pipeline.preload()
        return startupPreferencesTask.requireLoadedPreferences()
    }
}
