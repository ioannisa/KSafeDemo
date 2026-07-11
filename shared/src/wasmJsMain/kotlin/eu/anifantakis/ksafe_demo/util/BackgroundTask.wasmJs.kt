package eu.anifantakis.ksafe_demo.util

actual suspend fun <T> withPlatformBackgroundTask(name: String, block: suspend () -> T): T = block()
