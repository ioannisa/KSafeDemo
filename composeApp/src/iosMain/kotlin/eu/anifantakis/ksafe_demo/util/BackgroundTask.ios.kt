package eu.anifantakis.ksafe_demo.util

import platform.UIKit.UIApplication
import platform.UIKit.UIBackgroundTaskInvalid

actual suspend fun <T> withPlatformBackgroundTask(name: String, block: suspend () -> T): T {
    val taskId = UIApplication.sharedApplication.beginBackgroundTaskWithName(name, null)
    return try {
        block()
    } finally {
        if (taskId != UIBackgroundTaskInvalid) {
            UIApplication.sharedApplication.endBackgroundTask(taskId)
        }
    }
}
