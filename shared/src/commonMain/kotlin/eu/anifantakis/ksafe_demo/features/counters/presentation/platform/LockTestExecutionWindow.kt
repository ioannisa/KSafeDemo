package eu.anifantakis.ksafe_demo.features.counters.presentation.platform

/**
 * Runs [block] inside the platform execution allowance required by the counters feature's
 * interactive `requireUnlockedDevice` test.
 *
 * The iOS actual requests a finite UIKit background-task window so the countdown and encrypted
 * read can continue after the user locks the screen, then releases that window even when the
 * block fails or is cancelled. The allowance is best-effort and time-limited: it does not move
 * the block to another thread, keep the app alive indefinitely, or bypass Keychain lock policy.
 *
 * Other targets invoke [block] directly because they do not need the same UIKit lifecycle bridge
 * for this test.
 *
 * @param block the complete lock-test operation to execute.
 * @return the value returned by [block].
 */
internal expect suspend fun <T> withLockTestExecutionWindow(block: suspend () -> T): T
