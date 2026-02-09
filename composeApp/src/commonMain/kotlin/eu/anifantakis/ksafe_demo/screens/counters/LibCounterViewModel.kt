package eu.anifantakis.ksafe_demo.screens.counters

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.anifantakis.lib.ksafe.BiometricAuthorizationDuration
import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.invoke
import eu.anifantakis.lib.ksafe.compose.mutableStateOf
import eu.anifantakis.ksafe_demo.di.SecurityViolationsHolder
import eu.anifantakis.ksafe_demo.util.withPlatformBackgroundTask
import eu.anifantakis.lib.ksafe.SecurityViolation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class AuthInfo(
    val accessToken: String = "",
    val refreshToken: String = "",
    val expiresIn: Long = 0L
)

@Stable
class LibCounterViewModel(
    val ksafe: KSafe
) : ViewModel() {

    // just a normal mutableStateOf - no persistence
    var count1 by mutableStateOf(1000)
        private set

    // mutableStateOf via KSafe - with persistence
    // if key is unspecified, property name becomes the key
    // if encrypted is unspecified, it defaults to true
    var count2 by ksafe.mutableStateOf(2000)
        private set

    // mutableStateOf via KSafe - with persistence
    // key here is "counter3Key" and encrypted is false
    var count3 by ksafe.mutableStateOf(
        defaultValue = 3000,
        key = "counter3Key",
        encrypted = false
    )
        private set


    // KSafe without compose (regular variables, not states)
    // see console for output
    var count4 by ksafe(10)
    var count5 by ksafe(20)

    // encrypted string
    var count6 by ksafe("30")
    // unencrypted string
    var count7 by ksafe("40", encrypted = false)

    var bioCount by ksafe.mutableStateOf(0)

    // initialize the data class as a state so we watch for changes on the screen directly
    var authInfo by ksafe.mutableStateOf(
        defaultValue = AuthInfo(
            accessToken = "abc",
            refreshToken = "def",
            expiresIn = 3600L
        ),
        key = "authInfo",
        encrypted = true
    )

    // --- Biometric Auth Window ---
    private val bioAuthDurationSeconds = 6

    var bioAuthRemaining by mutableStateOf(0)
        private set

    private var bioTimerJob: kotlinx.coroutines.Job? = null

    private fun startBioAuthTimer() {
        bioTimerJob?.cancel()
        bioAuthRemaining = bioAuthDurationSeconds
        bioTimerJob = viewModelScope.launch {
            for (i in bioAuthDurationSeconds downTo 1) {
                bioAuthRemaining = i
                delay(1000)
            }
            bioAuthRemaining = 0
        }
    }

    // --- Lock Test Feature ---
    var lockTestCountdown by mutableStateOf(-1)
        private set

    var lockTestResult by mutableStateOf<String?>(null)
        private set

    var isLockTestRunning by mutableStateOf(false)
        private set

    init {
        println("count 4 at startup: $count4")
        println("count 5 at startup: $count5")
        println("count 6 at startup: $count6")
        println("count 7 at startup: $count7")

        checkFlows()
    }

    fun bioCounterIncrement() {
        // BiometricAuthorizationDuration(6_000L, screenScope) means:
        // - Once authenticated, no new prompt for 6 seconds
        // - Scoped to this screen instance (new ViewModel = re-authenticate)
        ksafe.verifyBiometricDirect(
            reason = "Authenticate to save",
            authorizationDuration = BiometricAuthorizationDuration(
                duration = bioAuthDurationSeconds * 1000L,
                scope = viewModelScope.hashCode().toString()
            )
        ) { success ->
            if (success) {
                bioCount++
                // Only start the timer on fresh auth, not cached hits
                if (bioAuthRemaining == 0) {
                    startBioAuthTimer()
                }
            }
        }
    }

    fun increment() {
        println("count 4 before increment: $count4")
        println("count 5 before increment: $count5")
        println("count 6 before increment: $count6")
        println("count 7 before increment: $count7")

        count1++
        count2++
        count3++
        count4++
        count5++
        count6 = (count6.toInt() + 1).toString()
        ksafe.putDirect("count7", count7.toInt() + 1)

        authInfo = authInfo.copy(
            expiresIn = authInfo.expiresIn + 1,
            accessToken = "abc_${authInfo.expiresIn + 1}",
            refreshToken = "def_${authInfo.expiresIn + 1}"
        )
    }

    fun clear() {
        // use deleteDirect to delete outside coroutines
        ksafe.deleteDirect("count1") // count 1 is normal mutableStateOf (not ksafe) deleting an non-existent key doesn't break the app
        ksafe.deleteDirect("count2")

        // or use delete for coroutines usage
        viewModelScope.launch {
            ksafe.delete("counter3Key")
            ksafe.delete("count4")
            ksafe.delete("count5")
            ksafe.delete("count6")
            ksafe.delete("count7")
            ksafe.delete("authInfo")
        }

        // Reset in-memory state to defaults so the UI updates immediately
        count1 = 1000
        count2 = 2000
        count3 = 3000
        count4 = 10
        count5 = 20
        count6 = "30"
        count7 = "40"
        bioCount = 0
        authInfo = AuthInfo(
            accessToken = "abc",
            refreshToken = "def",
            expiresIn = 3600L
        )
    }

    /**
     * Tests the requireUnlockedDevice feature:
     * 1. Pre-stores an encrypted value while the device is unlocked
     * 2. Counts down 15 seconds (user should lock the device)
     * 3. Attempts to read the encrypted value (requires Keychain access)
     *
     * If the device is truly locked, the Keychain read fails with
     * errSecInteractionNotAllowed, confirming the feature works.
     *
     * NOTE: Xcode's debugger prevents iOS data protection from engaging.
     * For accurate results, launch the app from the Home Screen (not Xcode).
     */
    fun startLockTest() {
        if (isLockTestRunning) return

        isLockTestRunning = true
        lockTestResult = null
        lockTestCountdown = 15

        val hasDebugger = SecurityViolationsHolder.violations
            .any { it == SecurityViolation.DebuggerAttached || it == SecurityViolation.DebugBuild }

        viewModelScope.launch {
            // Wrap in a platform background task so iOS doesn't suspend
            // the process while the screen is off
            withPlatformBackgroundTask("KSafeLockTest") {
                val testKey = "__lock_test_token__"

                // Step 1: Pre-store a value while the device is unlocked
                try {
                    ksafe.put(
                        key = testKey,
                        value = "pre-stored-while-unlocked",
                        encrypted = true,
                    )
                } catch (e: Exception) {
                    lockTestResult = "SETUP FAILED.\n\nCould not store test value: ${e.message}"
                    isLockTestRunning = false
                    return@withPlatformBackgroundTask
                }

                // Step 2: Countdown — user should lock the device now
                for (i in 15 downTo 1) {
                    lockTestCountdown = i
                    delay(1000)
                }
                lockTestCountdown = 0

                // Step 3: Attempt to read the pre-stored encrypted value.
                // This requires fetching the decryption key from the Keychain,
                // which should fail if the device is truly locked.
                try {
                    val readBack: String = ksafe.get(
                        key = testKey,
                        defaultValue = "",
                        encrypted = true,
                    )

                    if (readBack == "pre-stored-while-unlocked") {
                        val debuggerNote = if (hasDebugger) {
                            "\n\nA debugger / debug build was detected. " +
                            "Xcode's debugger prevents iOS data protection from engaging — " +
                            "the Keychain stays unlocked while the debugger is connected.\n\n" +
                            "To test accurately:\n" +
                            "1. Build & run the app on your device\n" +
                            "2. Press Stop in Xcode\n" +
                            "3. Launch the app from the Home Screen\n" +
                            "4. Run this test again"
                        } else {
                            ""
                        }
                        lockTestResult = "READ SUCCEEDED while locked.\n\n" +
                                "The encrypted read was NOT blocked." +
                                debuggerNote
                    } else {
                        lockTestResult = "UNEXPECTED RESULT.\n\n" +
                                "Read returned: \"$readBack\""
                    }
                } catch (e: Exception) {
                    lockTestResult = "READ BLOCKED — feature works!\n\n" +
                            "The encrypted read failed while the device was locked:\n${e.message}\n\n" +
                            "This confirms requireUnlockedDevice is working correctly."
                } finally {
                    // Clean up the test key
                    try { ksafe.delete(testKey) } catch (_: Exception) { }
                    isLockTestRunning = false
                }
            }
        }
    }

    fun dismissLockTestResult() {
        lockTestResult = null
    }

    private fun checkFlows() {
        val accessTokenKey = "access-token"

        viewModelScope.launch {
            launch {
                ksafe.getFlow<String?>(
                    key = accessTokenKey,
                    defaultValue = null,
                    encrypted = true,
                ).collect { value ->
                    println("KSafe flow - Current value: $value")
                }
            }

            // add value to flow
            ksafe.put(
                key = accessTokenKey,
                value = "some-value-1",
                encrypted = true,
            )

            // add another value to flow
            ksafe.put(
                key = accessTokenKey,
                value = "some-value-2",
                encrypted = true,
            )

            // delete value from flow
            ksafe.delete(key = accessTokenKey)
        }
    }
}
