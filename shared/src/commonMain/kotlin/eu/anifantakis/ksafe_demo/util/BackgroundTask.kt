package eu.anifantakis.ksafe_demo.util

/**
 * Wraps a suspend block with platform-specific background task registration.
 * On iOS, this uses `beginBackgroundTaskWithExpirationHandler` to prevent
 * the process from being suspended while the block executes.
 * On other platforms, this is a no-op wrapper.
 */
expect suspend fun <T> withPlatformBackgroundTask(name: String, block: suspend () -> T): T
