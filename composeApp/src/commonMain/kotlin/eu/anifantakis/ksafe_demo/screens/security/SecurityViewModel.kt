package eu.anifantakis.ksafe_demo.screens.security

import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import eu.anifantakis.ksafe_demo.di.SecurityViolationsHolder
import eu.anifantakis.lib.ksafe.SecurityViolation
import eu.anifantakis.lib.ksafe.compose.UiSecurityViolation

/**
 * ViewModel for the Security screen.
 * Displays security violations detected during KSafe initialization.
 */
@Stable
class SecurityViewModel : ViewModel() {

    /**
     * List of security violations detected.
     * Populated from SecurityViolationsHolder which receives callbacks during KSafe init.
     */
    val violations = mutableStateListOf<UiSecurityViolation>()

    init {
        // Load violations that were detected during KSafe initialization
        SecurityViolationsHolder.violations.forEach { violation ->
            // Wrap the enum in the UI wrapper
            val uiViolation = UiSecurityViolation(violation)
            if (uiViolation !in violations) {
                violations.add(uiViolation)
            }
        }
    }

    /**
     * Get a description for each violation type.
     */
    fun getViolationDescription(violation: SecurityViolation): String {
        return when (violation) {
            SecurityViolation.RootedDevice ->
                "The device is rooted (Android) or jailbroken (iOS). " +
                "This allows apps to bypass sandboxing and potentially access encrypted data."
            SecurityViolation.DebuggerAttached ->
                "A debugger is attached to the process. " +
                "This allows inspection of memory and runtime values including decrypted secrets."
            SecurityViolation.DebugBuild ->
                "The app is running in debug mode. " +
                "Debug builds may have weaker security settings and expose more information."
            SecurityViolation.Emulator ->
                "The app is running on an emulator/simulator. " +
                "Emulators don't have hardware-backed security like real devices."
        }
    }
}

