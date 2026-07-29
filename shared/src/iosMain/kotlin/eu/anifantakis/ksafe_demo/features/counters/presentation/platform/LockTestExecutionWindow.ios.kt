package eu.anifantakis.ksafe_demo.features.counters.presentation.platform

import platform.UIKit.UIApplication
import platform.UIKit.UIBackgroundTaskInvalid

internal actual suspend fun <T> withLockTestExecutionWindow(block: suspend () -> T): T {
    val taskId = UIApplication.sharedApplication.beginBackgroundTaskWithName("KSafeLockTest", null)
    return try {
        block()
    } finally {
        if (taskId != UIBackgroundTaskInvalid) {
            UIApplication.sharedApplication.endBackgroundTask(taskId)
        }
    }
}
