package eu.anifantakis.ksafe_demo.features.counters.presentation.platform

internal actual suspend fun <T> withLockTestExecutionWindow(block: suspend () -> T): T = block()
