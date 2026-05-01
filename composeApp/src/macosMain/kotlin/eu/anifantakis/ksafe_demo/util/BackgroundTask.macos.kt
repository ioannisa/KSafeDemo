package eu.anifantakis.ksafe_demo.util

/**
 * macOS doesn't have UIKit's `beginBackgroundTaskWithName` — apps can't be
 * suspended mid-execution the way iOS apps can, so wrapping the block adds no
 * extra survival guarantees. NSProcessInfo's `beginActivity` is the closest
 * macOS equivalent (it disables App Nap / sudden termination during the
 * window of the activity), but it's overkill for the kind of "buy a few
 * extra seconds while finishing a write" pattern the iOS implementation
 * exists for. Match the JVM behaviour and run the block directly.
 */
actual suspend fun <T> withPlatformBackgroundTask(name: String, block: suspend () -> T): T = block()
