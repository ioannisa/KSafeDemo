package eu.anifantakis.ksafe_demo.features.counters.presentation.platform

/**
 * Locking a Mac does not suspend an AppKit process the way iOS backgrounds UIKit apps.
 */
internal actual suspend fun <T> withLockTestExecutionWindow(block: suspend () -> T): T = block()
