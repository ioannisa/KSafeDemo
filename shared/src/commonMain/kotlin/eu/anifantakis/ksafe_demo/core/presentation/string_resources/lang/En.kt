package eu.anifantakis.ksafe_demo.core.presentation.string_resources.lang

import eu.anifantakis.ksafe_demo.core.presentation.string_resources.LanguageStrings
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey

internal class En : LanguageStrings {
    override fun getString(key: StringKey): String = when (key) {
        StringKey.COMMON_PROCESSING -> "Processing"
        StringKey.COMMON_BACK -> "Back"
        StringKey.COMMON_OPEN_APPLICATION_MENU -> "Open application menu"
        StringKey.COMMON_PREFERENCES -> "Preferences"
        StringKey.COMMON_ABOUT -> "About"
        StringKey.COMMON_CLEAR -> "Clear"
        StringKey.COMMON_CANCEL -> "Cancel"
        StringKey.COMMON_OK -> "OK"
        StringKey.COMMON_SAVE -> "Save"
        StringKey.COMMON_EMAIL -> "Email"
        StringKey.APP_PRESENTING_KSAFE -> "Presenting KSafe {0}"
        StringKey.APP_STARTUP_LOADING -> "Loading KSafe…"
        StringKey.APP_STARTUP_FAILED -> "KSafeDemo could not be started."
        StringKey.APP_STARTUP_RETRY -> "Retry"
        StringKey.NAV_COUNTERS -> "Counters"
        StringKey.NAV_FLOWS -> "Flows"
        StringKey.NAV_CUSTOM_JSON -> "Custom JSON"
        StringKey.NAV_SECURITY -> "Security"

        StringKey.PREFERENCES_APPEARANCE -> "Appearance"
        StringKey.PREFERENCES_APPEARANCE_DESCRIPTION ->
            "Choose how KSafeDemo selects its color theme. The preference is stored locally " +
                "and applied immediately on every platform."
        StringKey.PREFERENCES_THEME_DAY -> "Day"
        StringKey.PREFERENCES_THEME_DAY_DESCRIPTION -> "Always use the light color palette"
        StringKey.PREFERENCES_THEME_NIGHT -> "Night"
        StringKey.PREFERENCES_THEME_NIGHT_DESCRIPTION -> "Always use the dark color palette"
        StringKey.PREFERENCES_THEME_SYSTEM -> "System"
        StringKey.PREFERENCES_THEME_SYSTEM_DESCRIPTION ->
            "Follow the operating system appearance"
        StringKey.PREFERENCES_LANGUAGE -> "Language"
        StringKey.PREFERENCES_LANGUAGE_DESCRIPTION ->
            "Choose the language used by the application."
        StringKey.PREFERENCES_SELECT_LANGUAGE -> "Select language"

        StringKey.COUNTERS_MUTABLE_STATE_NO_PERSISTENCE ->
            "mutableStateOf (no persistence)"
        StringKey.COUNTERS_COUNTER_1 -> "Counter 1"
        StringKey.COUNTERS_PLAIN_STATE_RESETS -> "plain state — resets on restart"
        StringKey.COUNTERS_KSAFE_MUTABLE_STATE_PERSISTED ->
            "ksafe.mutableStateOf (persisted)"
        StringKey.COUNTERS_COUNTER_2 -> "Counter 2"
        StringKey.COUNTERS_ENCRYPTED_OBSERVED_ON_FLOWS ->
            "encrypted — observed on Flows tab"
        StringKey.COUNTERS_COUNTER_3 -> "Counter 3"
        StringKey.COUNTERS_UNENCRYPTED -> "unencrypted"
        StringKey.COUNTERS_COUNTER_2_SYNC_DESCRIPTION ->
            "Counter 2 is also observed on the Flows tab. Tap \"+\" here, then check Flows — " +
                "the synced value updates in real time."
        StringKey.COUNTERS_COUNTER_2_REFRESH_DESCRIPTION ->
            "If the Flows tab wrote to Counter 2, tap Refresh to see the latest value " +
                "(no scope = manual refresh needed)."
        StringKey.COUNTERS_REFRESH_COUNTER_2 -> "Refresh Counter 2"
        StringKey.COUNTERS_MUTABLE_STATE_FLOW_PERSISTED ->
            "ksafe.asMutableStateFlow (persisted, reactive)"
        StringKey.COUNTERS_COUNTER_2B -> "Counter 2b"
        StringKey.COUNTERS_COUNTER_2C -> "Counter 2c"
        StringKey.COUNTERS_FLOW_NO_REFRESH -> "MutableStateFlow — no refresh needed"
        StringKey.COUNTERS_FLOW_EXPLANATION ->
            "Same storage, different shape: a MutableStateFlow instead of a Compose State. " +
                "It takes a scope, so it subscribes to its key and picks up outside writes " +
                "by itself — that is the button above that it does not need."
        StringKey.COUNTERS_DATA_CLASS_PERSISTED ->
            "ksafe.mutableStateOf — data class (persisted, encrypted)"
        StringKey.COUNTERS_BIOMETRIC_COUNT -> "Bio: {0}"
        StringKey.COUNTERS_HARDWARE_SECURED_VAULT ->
            "Hardware-Secured Vault (StrongBox / Secure Enclave)"
        StringKey.COUNTERS_VAULT_TOKEN -> "Vault Token"
        StringKey.COUNTERS_NO_TOKEN_STORED -> "No token stored"
        StringKey.COUNTERS_GENERATE_TOKEN -> "Generate Token"
        StringKey.COUNTERS_CLEAR_VAULT -> "Clear Vault"
        StringKey.COUNTERS_BIO_AUTH_WINDOW ->
            "Bio auth window open — no prompt for {0}s"
        StringKey.COUNTERS_KEY_ROTATION -> "Key Rotation"
        StringKey.COUNTERS_KEY_ROTATION_DESCRIPTION ->
            "Re-encrypts every encrypted entry under a fresh key generation and drops the " +
                "superseded keys. Whole-store — the values themselves never change, so the " +
                "counters above stay exactly as they are."
        StringKey.COUNTERS_ROTATING -> "Rotating..."
        StringKey.COUNTERS_ROTATE_KEYS -> "Rotate Keys"
        StringKey.COUNTERS_LOCK_STATE_POLICY_TEST -> "Lock-State Policy Test"
        StringKey.COUNTERS_LOCK_TEST_INSTRUCTIONS ->
            "Run from Home Screen, not Xcode, for accurate results"
        StringKey.COUNTERS_LOCK_DEVICE_COUNTDOWN ->
            "Lock your device now! Reading in {0}s..."
        StringKey.COUNTERS_ATTEMPTING_ENCRYPTED_READ ->
            "Attempting encrypted read..."
        StringKey.COUNTERS_TEST_LOCK_DEFAULT -> "Default"
        StringKey.COUNTERS_TEST_LOCK_HARDWARE -> "Isolated"
        StringKey.COUNTERS_LOCK_TEST_RESULT -> "Lock Test Result"
        StringKey.COUNTERS_CLEAR_ALL_TITLE -> "Clear all values?"
        StringKey.COUNTERS_CLEAR_ALL_DESCRIPTION ->
            "This will delete all stored keys and reset counters to their defaults."
        StringKey.COUNTERS_BIOMETRICS_UNAVAILABLE ->
            "Biometrics unavailable — no fingerprint, face, or device lock is set up"
        StringKey.COUNTERS_AUTHENTICATE_TO_SAVE -> "Authenticate to save"
        StringKey.COUNTERS_AUTHENTICATION_FAILED ->
            "Authentication failed or was cancelled"
        StringKey.COUNTERS_ROTATION_RESULT ->
            "Re-encrypted: {0}\nSkipped: {1}\nFailed: {2}\nKey generation is now {3}"
        StringKey.COUNTERS_ROTATION_RESULT_WITH_SKIPPED ->
            "Re-encrypted: {0}\nSkipped: {1}\nFailed: {2}\nKey generation is now {3}\n\n" +
                "Skipped entries were being written to, or are strict entries on a locked " +
                "device. They stay readable under their previous key — rotate again to pick " +
                "them up."
        StringKey.COUNTERS_ROTATION_FAILED -> "Rotation failed:\n{0}"
        StringKey.COUNTERS_LOCK_SETUP_FAILED ->
            "SETUP FAILED.\n\nCould not store test value: {0}"
        StringKey.COUNTERS_LOCK_READ_SUCCEEDED ->
            "READ SUCCEEDED while locked.\n\nThe encrypted read was NOT blocked.\n\n" +
                "Note: requireUnlockedDevice is enforced on Android and Apple only. JVM " +
                "Desktop has no device-lock concept to key against, and browsers have neither " +
                "that nor a synchronous decrypt, so KSafe drops the flag there rather than " +
                "leave the value write-only. On Web and Desktop this test therefore always " +
                "reads back — that is the documented behaviour, not a failure."
        StringKey.COUNTERS_LOCK_READ_SUCCEEDED_WITH_DEBUGGER ->
            "READ SUCCEEDED while locked.\n\nThe encrypted read was NOT blocked.\n\n" +
                "Note: requireUnlockedDevice is enforced on Android and Apple only. JVM " +
                "Desktop has no device-lock concept to key against, and browsers have neither " +
                "that nor a synchronous decrypt, so KSafe drops the flag there rather than " +
                "leave the value write-only. On Web and Desktop this test therefore always " +
                "reads back — that is the documented behaviour, not a failure.\n\n" +
                "A debugger or debug build was detected. Xcode's debugger prevents iOS data " +
                "protection from engaging — the Keychain stays unlocked while the debugger " +
                "is connected.\n\nTo test accurately:\n1. Build and run the app on your device\n" +
                "2. Press Stop in Xcode\n3. Launch the app from the Home Screen\n" +
                "4. Run this test again"
        StringKey.COUNTERS_LOCK_UNEXPECTED_RESULT ->
            "UNEXPECTED RESULT.\n\nRead returned: \"{0}\""
        StringKey.COUNTERS_LOCK_READ_BLOCKED ->
            "READ BLOCKED — feature works!\n\nThe encrypted read failed while the device was " +
                "locked:\n{0}\n\nThis confirms requireUnlockedDevice is working correctly."

        StringKey.FLOWS_TITLE -> "Flow Delegates (1.8.0+)"
        StringKey.FLOWS_SUBTITLE -> "MutableStateFlow + encryption + persistence"
        StringKey.FLOWS_MUTABLE_STATE_FLOW_SECTION ->
            "asMutableStateFlow — drop-in MutableStateFlow"
        StringKey.FLOWS_MOVIES_STATE -> "MoviesListState (persisted + encrypted)"
        StringKey.FLOWS_LOADING_MOVIES -> "Loading movies..."
        StringKey.FLOWS_ERROR -> "Error: {0}"
        StringKey.FLOWS_NO_MOVIES_LOADED -> "No movies loaded"
        StringKey.FLOWS_LOAD_MOVIES -> "Load Movies"
        StringKey.FLOWS_AS_FLOW_SECTION -> "asFlow & two-way binding"
        StringKey.FLOWS_USERNAME -> "Username (asMutableStateFlow)"
        StringKey.FLOWS_TOGGLE_VALUE -> "Toggle Some Value (asFlow)"
        StringKey.FLOWS_DERIVED_VALUE -> "Derived: {0}"
        StringKey.FLOWS_ON_MODE -> "On Mode"
        StringKey.FLOWS_OFF_MODE -> "Off Mode"
        StringKey.FLOWS_DEFAULT_USERNAME -> "Guest"
        StringKey.FLOWS_SCOPE_SYNC_SECTION ->
            "mutableStateOf(scope) — cross-screen sync"
        StringKey.FLOWS_ISOLATED_NO_SCOPE -> "isolated (no scope)"
        StringKey.FLOWS_SYNCED_WITH_SCOPE -> "synced (with scope)"
        StringKey.FLOWS_INCREMENT_FROM_SCREEN -> "+1 from this screen"
        StringKey.FLOWS_REFRESH_ISOLATED -> "Refresh isolated"
        StringKey.FLOWS_SYNC_EXPLANATION ->
            "The scoped value updates in real time; the isolated value needs a manual refresh."
        StringKey.FLOWS_CLEAR_ALL -> "Clear All Flow Demos"

        StringKey.CUSTOM_JSON_TITLE -> "Custom JSON Serialization"
        StringKey.CUSTOM_JSON_SUBTITLE ->
            "Store data classes with @Contextual fields that need custom serializers."
        StringKey.CUSTOM_JSON_DEFINE_SERIALIZERS -> "1. Define custom serializers"
        StringKey.CUSTOM_JSON_REGISTER_SERIALIZERS -> "2. Register serializers"
        StringKey.CUSTOM_JSON_PASS_THROUGH_CONFIG -> "3. Pass it through KSafeConfig"
        StringKey.CUSTOM_JSON_USE_CONTEXTUAL_FIELDS -> "4. Use @Contextual fields"
        StringKey.CUSTOM_JSON_TRY_IT -> "Try It"
        StringKey.CUSTOM_JSON_NAME -> "Name"
        StringKey.CUSTOM_JSON_SAVED_COUNT ->
            "Saved {0} time(s) — color cycles on each save"
        StringKey.CUSTOM_JSON_STORED_VALUES ->
            "Stored Values (persisted across restarts)"
        StringKey.CUSTOM_JSON_ENCRYPTED -> "Encrypted"
        StringKey.CUSTOM_JSON_PLAIN_TEXT -> "Plain Text"
        StringKey.CUSTOM_JSON_PROFILE_NAME -> "name: {0}"
        StringKey.CUSTOM_JSON_PROFILE_CREATED_AT -> "createdAt: {0}"
        StringKey.CUSTOM_JSON_PROFILE_FAVORITE_COLOR -> "favoriteColor: {0}"

        StringKey.SECURITY_TITLE -> "Security Status"
        StringKey.SECURITY_SUBTITLE -> "KSafe security policy and key-custody demo"
        StringKey.SECURITY_SECURE_ENVIRONMENT -> "Secure Environment"
        StringKey.SECURITY_WARNINGS_DETECTED -> "{0} Warning(s) Detected"
        StringKey.SECURITY_KEY_PROTECTION -> "Key Protection"
        StringKey.SECURITY_INTENDED -> "Intended"
        StringKey.SECURITY_EFFECTIVE -> "Effective"
        StringKey.SECURITY_CUSTODY -> "Custody"
        StringKey.SECURITY_NOTES -> "Notes"
        StringKey.SECURITY_DEGRADED_FROM -> "Degraded from {0}"
        StringKey.SECURITY_REFRESH_STATUS -> "Refresh Security Status"
        StringKey.SECURITY_LIVE_INFO ->
            "Protection info is live and can change after key rotation or recovery."
        StringKey.SECURITY_CHECKS -> "Security Checks"
        StringKey.SECURITY_WARNING -> "WARNING"
        StringKey.SECURITY_OK -> "OK"
        StringKey.SECURITY_CURRENT_POLICY -> "Current Policy: {0}"
        StringKey.SECURITY_POLICY_DESCRIPTION ->
            "The demo reports security issues without blocking functionality. Production apps " +
                "handling sensitive data should choose a policy based on their threat model."
        StringKey.SECURITY_ROOTED_TITLE -> "Root/Jailbreak Detection"
        StringKey.SECURITY_ROOTED_DESCRIPTION ->
            "The device is rooted or jailbroken, which can weaken application sandboxing."
        StringKey.SECURITY_DEBUGGER_TITLE -> "Debugger Detection"
        StringKey.SECURITY_DEBUGGER_DESCRIPTION ->
            "A debugger can inspect runtime memory, including values while they are decrypted."
        StringKey.SECURITY_DEBUG_BUILD_TITLE -> "Debug Build Detection"
        StringKey.SECURITY_DEBUG_BUILD_DESCRIPTION ->
            "Debug builds may expose more information and use weaker operational controls."
        StringKey.SECURITY_EMULATOR_TITLE -> "Emulator Detection"
        StringKey.SECURITY_EMULATOR_DESCRIPTION ->
            "Emulators and simulators do not provide the same hardware-backed guarantees as devices."

        StringKey.ABOUT_TITLE -> "About"
        StringKey.ABOUT_KSAFE_VERSION -> "KSafe {0}"
        StringKey.ABOUT_DESCRIPTION ->
            "A Kotlin Multiplatform library for secure, encrypted persistence backed by the " +
                "platform Keystore or Keychain."
        StringKey.ABOUT_DEVELOPER -> "Developer"
        StringKey.ABOUT_PERSONAL_WEBSITE -> "Personal website"
        StringKey.ABOUT_GITHUB_PROFILE -> "GitHub profile"
        StringKey.ABOUT_PROJECTS -> "Projects"
        StringKey.ABOUT_KSAFE_LIBRARY -> "KSafe library"
        StringKey.ABOUT_OPEN_KSAFE_REPOSITORY -> "Open KSafe repository"
        StringKey.ABOUT_KSAFE_DEMO_APPLICATION -> "KSafeDemo application"
        StringKey.ABOUT_OPEN_KSAFE_DEMO_REPOSITORY -> "Open KSafeDemo repository"

        StringKey.UNMATCHED -> ""
    }
}
