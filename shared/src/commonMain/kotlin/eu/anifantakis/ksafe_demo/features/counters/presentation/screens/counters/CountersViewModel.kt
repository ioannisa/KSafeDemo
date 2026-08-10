package eu.anifantakis.ksafe_demo.features.counters.presentation.screens.counters

import androidx.compose.runtime.State
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import eu.anifantakis.ksafe_demo.core.presentation.global_state.BaseGlobalViewModel
import eu.anifantakis.ksafe_demo.core.presentation.helper.toComposeState
import eu.anifantakis.ksafe_demo.core.presentation.helper.UiText
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.localized
import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.asMutableStateFlow
import eu.anifantakis.lib.ksafe.invoke
import eu.anifantakis.lib.ksafe.compose.mutableStateOf
import eu.anifantakis.ksafe_demo.di.SecurityViolationsHolder
import eu.anifantakis.ksafe_demo.features.counters.presentation.platform.withLockTestExecutionWindow
import eu.anifantakis.ksafe_demo.features.counters.domain.model.AuthInfo
import eu.anifantakis.lib.ksafe.KSafeEncryptedProtection
import eu.anifantakis.lib.ksafe.KSafeKeyInfo
import eu.anifantakis.lib.ksafe.KSafeWriteMode
import eu.anifantakis.lib.ksafe.SecurityViolation
import eu.anifantakis.lib.ksafe.asStateFlow
import eu.anifantakis.lib.ksafe.biometrics.BiometricAuthorizationDuration
import eu.anifantakis.lib.ksafe.biometrics.KSafeBiometrics
import kotlin.coroutines.cancellation.CancellationException
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

data class CountersState(
    val count1: Int = 1000,
    val count2: Int = 2000,
    val count2b: Int = 2300,
    val count2c: Int = 2600,
    val count3: Int = 3000,
    val bioCount: Int = 0,
    val bioAuthRemaining: Int = 0,
    val authInfo: AuthInfo = AuthInfo(),
    val secureToken: String = "",
    val lockTestCountdown: Int = -1,
    val lockTestResult: UiText? = null,
    val isLockTestRunning: Boolean = false,
    val isRotating: Boolean = false,
    val rotationResult: UiText? = null,
)

sealed interface CountersIntent {
    data object Increment : CountersIntent
    data object RefreshCount2 : CountersIntent
    data object Clear : CountersIntent
    data object BiometricIncrement : CountersIntent
    data object GenerateToken : CountersIntent
    data object ClearVault : CountersIntent
    data class StartLockTest(val useHardwareIsolated: Boolean) : CountersIntent
    data object DismissLockTestResult : CountersIntent
    data object RotateKeys : CountersIntent
    data object DismissRotationResult : CountersIntent
}

/** Screen-local effects are intentionally absent; snackbars use the app-wide effect channel. */
sealed interface CountersEffect

@Stable
class CountersViewModel(
    private val ksafe: KSafe,
) : BaseGlobalViewModel() {

    // BEFORE YOU COMMENT THIS IS NOT GOOD FOR MVI.....
    // These should all be private totally and expose just universal state via mvi

    // I made them like that for demo purposes as your "eye" would normally track variables with private set

    // just a normal mutableStateOf - no persistence
    var count1 by mutableStateOf(1000)
        private set

    // mutableStateOf via KSafe - with persistence (NO scope — won't see external writes)
    // if key is unspecified, property name becomes the key
    // if encrypted is unspecified, it defaults to protection = KSafeProtection.DEFAULT
    // Note: The Flows tab also writes to "count2". Without scope, we need manual refresh.
    var count2 by ksafe.mutableStateOf(2000)
        private set

    // MutableStateOf? Of course!  Also look at my own helper function "toComposeState()"
    // so you turn it to compose state as if it was a mutableStateOf ;)
    private val _count2b by ksafe.asMutableStateFlow(2300, viewModelScope)
    val count2bState = _count2b.toComposeState(viewModelScope)

    // also for you who are the traditional guy and want "asStateFlow()" so you collectAsState in your composable
    private val _count2c by ksafe.asMutableStateFlow(2600, viewModelScope)
    val count2c = _count2c.asStateFlow()


    val state: State<CountersState> = derivedStateOf {
        CountersState(
            count1 = count1,
            count2 = count2,
            count2b = count2bState.value,
            count2c = count2c.value,
            count3 = count3,
            bioCount = bioCount,
            bioAuthRemaining = bioAuthRemaining,
            authInfo = authInfo,
            secureToken = secureToken,
            lockTestCountdown = lockTestCountdown,
            lockTestResult = lockTestResult,
            isLockTestRunning = isLockTestRunning,
            isRotating = isRotating,
            rotationResult = rotationResult,
        )
    }

    fun onAction(intent: CountersIntent) {
        when (intent) {
            CountersIntent.Increment -> increment()
            CountersIntent.RefreshCount2 -> refreshCount2()
            CountersIntent.Clear -> clear()
            CountersIntent.BiometricIncrement -> bioCounterIncrement()
            CountersIntent.GenerateToken -> generateNewToken()
            CountersIntent.ClearVault -> clearVault()
            is CountersIntent.StartLockTest -> startLockTest(intent.useHardwareIsolated)
            CountersIntent.DismissLockTestResult -> dismissLockTestResult()
            CountersIntent.RotateKeys -> rotateKeys()
            CountersIntent.DismissRotationResult -> dismissRotationResult()
        }
    }

    /** Manual refresh — re-reads "count2" from KSafe cache.
     *  Needed because count2 uses mutableStateOf without scope,
     *  so it won't see writes from the Flows screen automatically.
     *  count2b needs no equivalent — its scope keeps it current. */
    private fun refreshCount2() {
        count2 = ksafe.getDirect("count2", 2000)
    }

    // mutableStateOf via KSafe - with persistence
    // key here is "counter3Key" and protection = KSafeProtection.NONE
    private var count3 by ksafe.mutableStateOf(
        defaultValue = 3000,
        key = "counter3Key",
        mode = KSafeWriteMode.Plain
    )


    // KSafe without compose (regular variables, not states)
    // see console for output
    private var count4 by ksafe(10)
    private var count5 by ksafe(20)

    // encrypted string
    private var count6 by ksafe("30")
    // unencrypted string
    private var count7 by ksafe("40", mode = KSafeWriteMode.Plain)

    private var bioCount by ksafe.mutableStateOf(0)

    // Hardware-secured vault token (StrongBox on Android, Secure Enclave on iOS)
    private var secureToken by ksafe.mutableStateOf("", mode = KSafeWriteMode.Encrypted(protection = KSafeEncryptedProtection.HARDWARE_ISOLATED))

    // initialize the data class as a state so we watch for changes on the screen directly
    private var authInfo by ksafe.mutableStateOf(
        defaultValue = AuthInfo(
            accessToken = "abc",
            refreshToken = "def",
            expiresIn = 3600L
        ),
        key = "authInfo",
        mode = KSafeWriteMode.Encrypted()
    )

    // --- Key Info Tracking ---

    private val trackedKeys = listOf(
        "count2", "count2b", "counter3Key", "count4", "count5",
        "count6", "count7", "bioCount", "secureToken", "authInfo"
    )

    private var keyInfoMap by mutableStateOf<Map<String, KSafeKeyInfo?>>(emptyMap())

    private fun refreshKeyInfo() {
        keyInfoMap = trackedKeys.associateWith { ksafe.getKeyInfo(it) }
    }

    // --- Biometric Auth Window ---
    private val bioAuthDurationSeconds = 6

    private var bioAuthRemaining by mutableStateOf(0)

    private var bioTimerJob: kotlinx.coroutines.Job? = null

    private fun startBioAuthTimer() {
        bioTimerJob?.cancel()
        bioAuthRemaining = bioAuthDurationSeconds
        bioTimerJob = viewModelScope.launch {
            for (i in bioAuthDurationSeconds downTo 1) {
                bioAuthRemaining = i
                delay(1.seconds)
            }
            bioAuthRemaining = 0
        }
    }

    // --- Key Rotation ---

    private var isRotating by mutableStateOf(false)

    private var rotationResult by mutableStateOf<UiText?>(null)

    /**
     * Rotates the WHOLE store — `rotateKeys()` takes no key argument, because the master key
     * it replaces is shared by every entry. Each encrypted entry is decrypted under the old
     * generation and re-encrypted under the new one; values are untouched.
     *
     * Entries a concurrent write races, and strict (`requireUnlockedDevice`) entries on a
     * locked device, come back as `skipped` — still readable under their previous key, and
     * picked up by the next call. That is why this is safe to press twice.
     */
    private fun rotateKeys() {
        if (isRotating) return
        isRotating = true
        viewModelScope.launch {
            try {
                val result = ksafe.rotateKeys()
                val resultKey = if (result.skipped > 0) {
                    StringKey.COUNTERS_ROTATION_RESULT_WITH_SKIPPED
                } else {
                    StringKey.COUNTERS_ROTATION_RESULT
                }
                rotationResult = UiText.res(
                    resultKey,
                    result.rotated,
                    result.skipped,
                    result.failed,
                    result.keyGeneration,
                )
                refreshKeyInfo()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                rotationResult = UiText.res(
                    StringKey.COUNTERS_ROTATION_FAILED,
                    e.message.orEmpty(),
                )
            } finally {
                isRotating = false
            }
        }
    }

    private fun dismissRotationResult() {
        rotationResult = null
    }

    // --- Lock Test Feature ---
    private var lockTestCountdown by mutableStateOf(-1)

    private var lockTestResult by mutableStateOf<UiText?>(null)

    private var isLockTestRunning by mutableStateOf(false)

    init {
        ksafe.deviceKeyStorages.forEach { println("Available key storage: $it") }

        println("count 4 at startup: $count4")
        println("count 5 at startup: $count5")
        println("count 6 at startup: $count6")
        println("count 7 at startup: $count7")

        checkFlows()
        refreshKeyInfo()
    }

    private fun bioCounterIncrement() {
        println("DEBUG: bioCounterIncrement() called")

        // Ask FIRST whether a real prompt can even be shown. On a stock emulator — no
        // enrolled fingerprint/face and no PIN, pattern or password — there is nothing to
        // prompt with, so verifyBiometricDirect just reports success = false and the button
        // looks broken. Probing up front lets us say *why* nothing happened instead.
        //
        // allowDeviceCredentialFallback must match the value passed to verifyBiometricDirect
        // below, or the probe answers a different question than the one we go on to ask.
        KSafeBiometrics.biometricsAvailableDirect(
            allowDeviceCredentialFallback = true
        ) { available ->
            println("DEBUG: biometricsAvailableDirect callback, available=$available")

            if (!available) {
                showSnackbar(StringKey.COUNTERS_BIOMETRICS_UNAVAILABLE)
                return@biometricsAvailableDirect
            }

            // BiometricAuthorizationDuration(6_000L, screenScope) means:
            // - Once authenticated, no new prompt for 6 seconds
            // - Scoped to this screen instance (new ViewModel = re-authenticate)
            KSafeBiometrics.verifyBiometricDirect(
                reason = StringKey.COUNTERS_AUTHENTICATE_TO_SAVE.localized(),
                authorizationDuration = BiometricAuthorizationDuration(
                    duration = bioAuthDurationSeconds * 1000L,
                    scope = viewModelScope.hashCode().toString()
                ),
                allowDeviceCredentialFallback = true // <-- if true (default), you can combine biometrics+fallback (for example fingerprints+pattern) but if its false, it will be biometrics only
            ) { success ->
                println("DEBUG: verifyBiometricDirect callback, success=$success")
                if (success) {
                    bioCount++
                    refreshKeyInfo()
                    // Only start the timer on fresh auth, not cached hits
                    if (bioAuthRemaining == 0) {
                        startBioAuthTimer()
                    }
                } else {
                    // Biometrics exist but the user cancelled or failed to match. Without
                    // this the button is silent here too — the same complaint, different cause.
                    showSnackbar(StringKey.COUNTERS_AUTHENTICATION_FAILED)
                }
            }
        }
    }

    private fun increment() {
        println("count 4 before increment: $count4")
        println("count 5 before increment: $count5")
        println("count 6 before increment: $count6")
        println("count 7 before increment: $count7")

        count1++
        count2++
        // The MutableStateFlow way — .update{} rather than an assignment; the persist and the
        // emission both happen inside the delegate.
        _count2b.update { it + 1 }
        _count2c.update { it + 1 }
        count3++
        count4++
        count5++
        count6 = (count6.toInt() + 1).toString()

        // For count7, we demonstrate an alternative way to update the value without using the delegated property.
        // count7 is declared as String, so we convert it to Int, increment, and convert back to String.
        ksafe.putDirect("count7", (count7.toInt() + 1).toString(), mode = KSafeWriteMode.Plain)

        authInfo = authInfo.copy(
            expiresIn = authInfo.expiresIn + 1,
            accessToken = "abc_${authInfo.expiresIn + 1}",
            refreshToken = "def_${authInfo.expiresIn + 1}"
        )

        refreshKeyInfo()
    }

    @OptIn(ExperimentalUuidApi::class)
    private fun generateNewToken() {
        secureToken = Uuid.random().toString()
        refreshKeyInfo()
    }

    private fun clearVault() {
        secureToken = ""
        refreshKeyInfo()
    }

    private fun clear() {
        // use deleteDirect to delete outside coroutines
        ksafe.deleteDirect("count1") // count 1 is normal mutableStateOf (not ksafe) deleting a non-existent key doesn't break the app
        ksafe.deleteDirect("count2")

        // or use delete for coroutines usage
        viewModelScope.launch {
            ksafe.delete("count2b")
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
        _count2b.update { 2300 }
        _count2c.update { 2600 }
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
        secureToken = ""
        refreshKeyInfo()
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
    private fun startLockTest(useHardwareIsolated: Boolean) {
        if (isLockTestRunning) return

        isLockTestRunning = true
        lockTestResult = null
        lockTestCountdown = 15

        val hasDebugger = SecurityViolationsHolder.violations
            .any { it == SecurityViolation.DebuggerAttached || it == SecurityViolation.DebugBuild }

        viewModelScope.launch {
            // Wrap in a platform background task so iOS doesn't suspend
            // the process while the screen is off
            withLockTestExecutionWindow {
                val testKey = if (useHardwareIsolated) "__lock_test_token_hw__" else "__lock_test_token__"
                val protectionLevel = if (useHardwareIsolated) KSafeEncryptedProtection.HARDWARE_ISOLATED else KSafeEncryptedProtection.DEFAULT

                // Step 1: Pre-store a value while the device is unlocked
                try {
                    ksafe.put(
                        key = testKey,
                        value = "pre-stored-while-unlocked",
                        mode = KSafeWriteMode.Encrypted(
                            protection = protectionLevel,
                            requireUnlockedDevice = true
                        )
                    )
                } catch (e: Exception) {
                    lockTestResult = UiText.res(
                        StringKey.COUNTERS_LOCK_SETUP_FAILED,
                        e.message.orEmpty(),
                    )
                    isLockTestRunning = false
                    return@withLockTestExecutionWindow
                }

                // Step 2: Countdown — user should lock the device now
                for (i in 15 downTo 1) {
                    lockTestCountdown = i
                    delay(1.seconds)
                }
                lockTestCountdown = 0

                // Step 3: Attempt to read the pre-stored encrypted value.
                // The decrypt will hit the hardware Keystore/Keychain because
                // requireUnlockedDevice = true items bypass in-memory caching.

                // This requires fetching the decryption key from the Keychain,
                // which should fail if the device is truly locked.
                try {
                    val readBack: String = ksafe.get(
                        key = testKey,
                        defaultValue = "",
                    )

                    if (readBack == "pre-stored-while-unlocked") {
                        lockTestResult = if (hasDebugger) {
                            UiText.res(StringKey.COUNTERS_LOCK_READ_SUCCEEDED_WITH_DEBUGGER)
                        } else {
                            UiText.res(StringKey.COUNTERS_LOCK_READ_SUCCEEDED)
                        }
                    } else {
                        lockTestResult = UiText.res(
                            StringKey.COUNTERS_LOCK_UNEXPECTED_RESULT,
                            readBack,
                        )
                    }
                } catch (e: Exception) {
                    lockTestResult = UiText.res(
                        StringKey.COUNTERS_LOCK_READ_BLOCKED,
                        e.message.orEmpty(),
                    )
                } finally {
                    // Clean up the test key
                    try { ksafe.delete(testKey) } catch (_: Exception) { }
                    isLockTestRunning = false
                }
            }
        }
    }

    private fun dismissLockTestResult() {
        lockTestResult = null
    }

    private fun checkFlows() {
        val accessTokenKey = "access-token"

        viewModelScope.launch {
            launch {
                ksafe.getFlow<String?>(
                    key = accessTokenKey,
                    defaultValue = null,
                ).collect { value ->
                    println("KSafe flow - Current value: $value")
                }
            }

            // add value to flow
            ksafe.put(
                key = accessTokenKey,
                value = "some-value-1",
                mode = KSafeWriteMode.Encrypted()
            )

            // add another value to flow
            ksafe.put(
                key = accessTokenKey,
                value = "some-value-2",
                mode = KSafeWriteMode.Encrypted()
            )

            // delete value from flow
            ksafe.delete(key = accessTokenKey)
        }
    }
}
