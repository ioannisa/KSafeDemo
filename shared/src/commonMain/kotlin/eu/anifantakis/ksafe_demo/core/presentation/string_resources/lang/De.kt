package eu.anifantakis.ksafe_demo.core.presentation.string_resources.lang

import eu.anifantakis.ksafe_demo.core.presentation.string_resources.LanguageStrings
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey

internal class De : LanguageStrings {
    override fun getString(key: StringKey): String = when (key) {
        StringKey.COMMON_PROCESSING -> "Wird verarbeitet"
        StringKey.COMMON_BACK -> "Zurück"
        StringKey.COMMON_OPEN_APPLICATION_MENU -> "Anwendungsmenü öffnen"
        StringKey.COMMON_PREFERENCES -> "Einstellungen"
        StringKey.COMMON_ABOUT -> "Über"
        StringKey.COMMON_CLEAR -> "Löschen"
        StringKey.COMMON_CANCEL -> "Abbrechen"
        StringKey.COMMON_OK -> "OK"
        StringKey.COMMON_SAVE -> "Speichern"
        StringKey.COMMON_EMAIL -> "E-Mail"
        StringKey.APP_PRESENTING_KSAFE -> "KSafe {0} vorgestellt"
        StringKey.APP_STARTUP_LOADING -> "KSafe wird geladen…"
        StringKey.APP_STARTUP_FAILED -> "KSafeDemo konnte nicht gestartet werden."
        StringKey.APP_STARTUP_RETRY -> "Erneut versuchen"
        StringKey.NAV_COUNTERS -> "Zähler"
        StringKey.NAV_FLOWS -> "Flows"
        StringKey.NAV_CUSTOM_JSON -> "Eigenes JSON"
        StringKey.NAV_SECURITY -> "Sicherheit"

        StringKey.PREFERENCES_APPEARANCE -> "Darstellung"
        StringKey.PREFERENCES_APPEARANCE_DESCRIPTION ->
            "Wählen Sie, wie KSafeDemo das Farbschema bestimmt. Die Einstellung wird lokal " +
                "gespeichert und auf jeder Plattform sofort angewendet."
        StringKey.PREFERENCES_THEME_DAY -> "Hell"
        StringKey.PREFERENCES_THEME_DAY_DESCRIPTION -> "Immer die helle Farbpalette verwenden"
        StringKey.PREFERENCES_THEME_NIGHT -> "Dunkel"
        StringKey.PREFERENCES_THEME_NIGHT_DESCRIPTION -> "Immer die dunkle Farbpalette verwenden"
        StringKey.PREFERENCES_THEME_SYSTEM -> "System"
        StringKey.PREFERENCES_THEME_SYSTEM_DESCRIPTION ->
            "Der Darstellung des Betriebssystems folgen"
        StringKey.PREFERENCES_LANGUAGE -> "Sprache"
        StringKey.PREFERENCES_LANGUAGE_DESCRIPTION ->
            "Wählen Sie die Sprache der Anwendung."
        StringKey.PREFERENCES_SELECT_LANGUAGE -> "Sprache auswählen"

        StringKey.COUNTERS_MUTABLE_STATE_NO_PERSISTENCE ->
            "mutableStateOf (ohne Persistenz)"
        StringKey.COUNTERS_COUNTER_1 -> "Zähler 1"
        StringKey.COUNTERS_PLAIN_STATE_RESETS ->
            "einfacher Zustand — wird beim Neustart zurückgesetzt"
        StringKey.COUNTERS_KSAFE_MUTABLE_STATE_PERSISTED ->
            "ksafe.mutableStateOf (persistent)"
        StringKey.COUNTERS_COUNTER_2 -> "Zähler 2"
        StringKey.COUNTERS_ENCRYPTED_OBSERVED_ON_FLOWS ->
            "verschlüsselt — im Tab Flows beobachtet"
        StringKey.COUNTERS_COUNTER_3 -> "Zähler 3"
        StringKey.COUNTERS_UNENCRYPTED -> "unverschlüsselt"
        StringKey.COUNTERS_COUNTER_2_SYNC_DESCRIPTION ->
            "Zähler 2 wird auch im Tab Flows beobachtet. Tippen Sie hier auf \"+\" und prüfen " +
                "Sie anschließend Flows — der synchronisierte Wert wird in Echtzeit aktualisiert."
        StringKey.COUNTERS_COUNTER_2_REFRESH_DESCRIPTION ->
            "Wenn der Tab Flows Zähler 2 geändert hat, tippen Sie auf Aktualisieren, um den " +
                "neuesten Wert zu sehen (ohne Scope ist eine manuelle Aktualisierung nötig)."
        StringKey.COUNTERS_REFRESH_COUNTER_2 -> "Zähler 2 aktualisieren"
        StringKey.COUNTERS_MUTABLE_STATE_FLOW_PERSISTED ->
            "ksafe.asMutableStateFlow (persistent, reaktiv)"
        StringKey.COUNTERS_COUNTER_2B -> "Zähler 2b"
        StringKey.COUNTERS_COUNTER_2C -> "Zähler 2c"
        StringKey.COUNTERS_FLOW_NO_REFRESH ->
            "MutableStateFlow — keine Aktualisierung nötig"
        StringKey.COUNTERS_FLOW_EXPLANATION ->
            "Gleicher Speicher, andere Form: ein MutableStateFlow statt eines Compose State. " +
                "Er erhält einen Scope, abonniert dadurch seinen Schlüssel und übernimmt externe " +
                "Änderungen selbstständig — die Schaltfläche darüber wird nicht benötigt."
        StringKey.COUNTERS_DATA_CLASS_PERSISTED ->
            "ksafe.mutableStateOf — Datenklasse (persistent, verschlüsselt)"
        StringKey.COUNTERS_BIOMETRIC_COUNT -> "Bio: {0}"
        StringKey.COUNTERS_HARDWARE_SECURED_VAULT ->
            "Hardware-gesicherter Tresor (StrongBox / Secure Enclave)"
        StringKey.COUNTERS_VAULT_TOKEN -> "Tresor-Token"
        StringKey.COUNTERS_NO_TOKEN_STORED -> "Kein Token gespeichert"
        StringKey.COUNTERS_GENERATE_TOKEN -> "Token erzeugen"
        StringKey.COUNTERS_CLEAR_VAULT -> "Tresor leeren"
        StringKey.COUNTERS_BIO_AUTH_WINDOW ->
            "Biometrisches Authentifizierungsfenster offen — {0} s lang keine Abfrage"
        StringKey.COUNTERS_KEY_ROTATION -> "Schlüsselrotation"
        StringKey.COUNTERS_KEY_ROTATION_DESCRIPTION ->
            "Verschlüsselt alle verschlüsselten Einträge mit einer neuen Schlüsselgeneration " +
                "und entfernt die ersetzten Schlüssel. Der gesamte Speicher wird verarbeitet, " +
                "die Werte selbst ändern sich jedoch nicht."
        StringKey.COUNTERS_ROTATING -> "Rotation läuft..."
        StringKey.COUNTERS_ROTATE_KEYS -> "Schlüssel rotieren"
        StringKey.COUNTERS_LOCK_STATE_POLICY_TEST -> "Sperrstatus-Richtlinientest"
        StringKey.COUNTERS_LOCK_TEST_INSTRUCTIONS ->
            "Für genaue Ergebnisse vom Home-Bildschirm statt aus Xcode starten"
        StringKey.COUNTERS_LOCK_DEVICE_COUNTDOWN ->
            "Sperren Sie jetzt Ihr Gerät! Lesen in {0} s..."
        StringKey.COUNTERS_ATTEMPTING_ENCRYPTED_READ ->
            "Verschlüsselter Lesevorgang wird versucht..."
        StringKey.COUNTERS_TEST_LOCK_DEFAULT -> "Sperre testen (Standard)"
        StringKey.COUNTERS_TEST_LOCK_HARDWARE -> "Sperre testen (Hardware)"
        StringKey.COUNTERS_LOCK_TEST_RESULT -> "Ergebnis des Sperrtests"
        StringKey.COUNTERS_CLEAR_ALL_TITLE -> "Alle Werte löschen?"
        StringKey.COUNTERS_CLEAR_ALL_DESCRIPTION ->
            "Dadurch werden alle gespeicherten Schlüssel gelöscht und die Zähler zurückgesetzt."
        StringKey.COUNTERS_BIOMETRICS_UNAVAILABLE ->
            "Biometrie nicht verfügbar — Fingerabdruck, Gesichtserkennung oder Gerätesperre " +
                "sind nicht eingerichtet"
        StringKey.COUNTERS_AUTHENTICATE_TO_SAVE -> "Zum Speichern authentifizieren"
        StringKey.COUNTERS_AUTHENTICATION_FAILED ->
            "Authentifizierung fehlgeschlagen oder abgebrochen"
        StringKey.COUNTERS_ROTATION_RESULT ->
            "Neu verschlüsselt: {0}\nÜbersprungen: {1}\nFehlgeschlagen: {2}\n" +
                "Aktuelle Schlüsselgeneration: {3}"
        StringKey.COUNTERS_ROTATION_RESULT_WITH_SKIPPED ->
            "Neu verschlüsselt: {0}\nÜbersprungen: {1}\nFehlgeschlagen: {2}\n" +
                "Aktuelle Schlüsselgeneration: {3}\n\nÜbersprungene Einträge wurden gerade " +
                "geschrieben oder sind strikte Einträge auf einem gesperrten Gerät. Sie bleiben " +
                "mit dem vorherigen Schlüssel lesbar — rotieren Sie erneut, um sie zu übernehmen."
        StringKey.COUNTERS_ROTATION_FAILED -> "Rotation fehlgeschlagen:\n{0}"
        StringKey.COUNTERS_LOCK_SETUP_FAILED ->
            "EINRICHTUNG FEHLGESCHLAGEN.\n\nTestwert konnte nicht gespeichert werden: {0}"
        StringKey.COUNTERS_LOCK_READ_SUCCEEDED ->
            "LESEN IM GESPERRTEN ZUSTAND ERFOLGREICH.\n\nDer verschlüsselte Lesevorgang wurde " +
                "NICHT blockiert.\n\nHinweis: requireUnlockedDevice wird nur unter Android und " +
                "Apple erzwungen. JVM Desktop besitzt kein Gerätesperrkonzept und Browser weder " +
                "dieses noch synchrone Entschlüsselung. Deshalb ignoriert KSafe dort das Flag. " +
                "Unter Web und Desktop liest dieser Test immer erfolgreich — das ist das " +
                "dokumentierte Verhalten und kein Fehler."
        StringKey.COUNTERS_LOCK_READ_SUCCEEDED_WITH_DEBUGGER ->
            "LESEN IM GESPERRTEN ZUSTAND ERFOLGREICH.\n\nDer verschlüsselte Lesevorgang wurde " +
                "NICHT blockiert.\n\nHinweis: requireUnlockedDevice wird nur unter Android und " +
                "Apple erzwungen. Unter Web und Desktop liest dieser Test immer erfolgreich — " +
                "das ist das dokumentierte Verhalten und kein Fehler.\n\nEin Debugger oder " +
                "Debug-Build wurde erkannt. Der Xcode-Debugger verhindert den iOS-Datenschutz; " +
                "der Schlüsselbund bleibt während der Verbindung entsperrt.\n\nFür einen genauen " +
                "Test:\n1. App auf dem Gerät erstellen und starten\n2. In Xcode Stop drücken\n" +
                "3. App vom Home-Bildschirm starten\n4. Test erneut ausführen"
        StringKey.COUNTERS_LOCK_UNEXPECTED_RESULT ->
            "UNERWARTETES ERGEBNIS.\n\nLesevorgang lieferte: \"{0}\""
        StringKey.COUNTERS_LOCK_READ_BLOCKED ->
            "LESEN BLOCKIERT — Funktion arbeitet!\n\nDer verschlüsselte Lesevorgang ist bei " +
                "gesperrtem Gerät fehlgeschlagen:\n{0}\n\nDamit ist bestätigt, dass " +
                "requireUnlockedDevice korrekt funktioniert."

        StringKey.FLOWS_TITLE -> "Flow-Delegates (1.8.0+)"
        StringKey.FLOWS_SUBTITLE -> "MutableStateFlow + Verschlüsselung + Persistenz"
        StringKey.FLOWS_MUTABLE_STATE_FLOW_SECTION ->
            "asMutableStateFlow — direkter MutableStateFlow-Ersatz"
        StringKey.FLOWS_MOVIES_STATE -> "MoviesListState (persistent + verschlüsselt)"
        StringKey.FLOWS_LOADING_MOVIES -> "Filme werden geladen..."
        StringKey.FLOWS_ERROR -> "Fehler: {0}"
        StringKey.FLOWS_NO_MOVIES_LOADED -> "Keine Filme geladen"
        StringKey.FLOWS_LOAD_MOVIES -> "Filme laden"
        StringKey.FLOWS_AS_FLOW_SECTION -> "asFlow und bidirektionale Bindung"
        StringKey.FLOWS_USERNAME -> "Benutzername (asMutableStateFlow)"
        StringKey.FLOWS_TOGGLE_VALUE -> "Wert umschalten (asFlow)"
        StringKey.FLOWS_DERIVED_VALUE -> "Abgeleitet: {0}"
        StringKey.FLOWS_ON_MODE -> "Ein-Modus"
        StringKey.FLOWS_OFF_MODE -> "Aus-Modus"
        StringKey.FLOWS_DEFAULT_USERNAME -> "Gast"
        StringKey.FLOWS_SCOPE_SYNC_SECTION ->
            "mutableStateOf(scope) — bildschirmübergreifende Synchronisierung"
        StringKey.FLOWS_ISOLATED_NO_SCOPE -> "isoliert (ohne Scope)"
        StringKey.FLOWS_SYNCED_WITH_SCOPE -> "synchronisiert (mit Scope)"
        StringKey.FLOWS_INCREMENT_FROM_SCREEN -> "+1 von diesem Bildschirm"
        StringKey.FLOWS_REFRESH_ISOLATED -> "Isolierten Wert aktualisieren"
        StringKey.FLOWS_SYNC_EXPLANATION ->
            "Der Wert mit Scope wird in Echtzeit aktualisiert; der isolierte Wert muss manuell " +
                "aktualisiert werden."
        StringKey.FLOWS_CLEAR_ALL -> "Alle Flow-Demos löschen"

        StringKey.CUSTOM_JSON_TITLE -> "Benutzerdefinierte JSON-Serialisierung"
        StringKey.CUSTOM_JSON_SUBTITLE ->
            "Datenklassen mit @Contextual-Feldern und eigenen Serialisierern speichern."
        StringKey.CUSTOM_JSON_DEFINE_SERIALIZERS -> "1. Eigene Serialisierer definieren"
        StringKey.CUSTOM_JSON_REGISTER_SERIALIZERS -> "2. Serialisierer registrieren"
        StringKey.CUSTOM_JSON_PASS_THROUGH_CONFIG -> "3. Über KSafeConfig übergeben"
        StringKey.CUSTOM_JSON_USE_CONTEXTUAL_FIELDS -> "4. @Contextual-Felder verwenden"
        StringKey.CUSTOM_JSON_TRY_IT -> "Ausprobieren"
        StringKey.CUSTOM_JSON_NAME -> "Name"
        StringKey.CUSTOM_JSON_SAVED_COUNT ->
            "{0}-mal gespeichert — die Farbe wechselt bei jedem Speichern"
        StringKey.CUSTOM_JSON_STORED_VALUES ->
            "Gespeicherte Werte (bleiben nach Neustarts erhalten)"
        StringKey.CUSTOM_JSON_ENCRYPTED -> "Verschlüsselt"
        StringKey.CUSTOM_JSON_PLAIN_TEXT -> "Klartext"
        StringKey.CUSTOM_JSON_PROFILE_NAME -> "Name: {0}"
        StringKey.CUSTOM_JSON_PROFILE_CREATED_AT -> "Erstellt am: {0}"
        StringKey.CUSTOM_JSON_PROFILE_FAVORITE_COLOR -> "Lieblingsfarbe: {0}"

        StringKey.SECURITY_TITLE -> "Sicherheitsstatus"
        StringKey.SECURITY_SUBTITLE -> "Demo der KSafe-Sicherheitsrichtlinie und Schlüsselverwahrung"
        StringKey.SECURITY_SECURE_ENVIRONMENT -> "Sichere Umgebung"
        StringKey.SECURITY_WARNINGS_DETECTED -> "{0} Warnung(en) erkannt"
        StringKey.SECURITY_KEY_PROTECTION -> "Schlüsselschutz"
        StringKey.SECURITY_INTENDED -> "Vorgesehen"
        StringKey.SECURITY_EFFECTIVE -> "Wirksam"
        StringKey.SECURITY_CUSTODY -> "Verwahrung"
        StringKey.SECURITY_NOTES -> "Hinweise"
        StringKey.SECURITY_DEGRADED_FROM -> "Herabgestuft von {0}"
        StringKey.SECURITY_REFRESH_STATUS -> "Sicherheitsstatus aktualisieren"
        StringKey.SECURITY_LIVE_INFO ->
            "Die Schutzinformationen sind live und können sich nach Rotation oder " +
                "Wiederherstellung ändern."
        StringKey.SECURITY_CHECKS -> "Sicherheitsprüfungen"
        StringKey.SECURITY_WARNING -> "WARNUNG"
        StringKey.SECURITY_OK -> "OK"
        StringKey.SECURITY_CURRENT_POLICY -> "Aktuelle Richtlinie: {0}"
        StringKey.SECURITY_POLICY_DESCRIPTION ->
            "Die Demo meldet Sicherheitsprobleme, ohne Funktionen zu blockieren. " +
                "Produktionsanwendungen mit sensiblen Daten sollten eine zu ihrem " +
                "Bedrohungsmodell passende Richtlinie wählen."
        StringKey.SECURITY_ROOTED_TITLE -> "Root-/Jailbreak-Erkennung"
        StringKey.SECURITY_ROOTED_DESCRIPTION ->
            "Das Gerät ist gerootet oder hat einen Jailbreak, wodurch die Anwendungs-Sandbox " +
                "geschwächt werden kann."
        StringKey.SECURITY_DEBUGGER_TITLE -> "Debugger-Erkennung"
        StringKey.SECURITY_DEBUGGER_DESCRIPTION ->
            "Ein Debugger kann Laufzeitspeicher untersuchen, einschließlich entschlüsselter Werte."
        StringKey.SECURITY_DEBUG_BUILD_TITLE -> "Debug-Build-Erkennung"
        StringKey.SECURITY_DEBUG_BUILD_DESCRIPTION ->
            "Debug-Builds können mehr Informationen offenlegen und schwächere Kontrollen verwenden."
        StringKey.SECURITY_EMULATOR_TITLE -> "Emulator-Erkennung"
        StringKey.SECURITY_EMULATOR_DESCRIPTION ->
            "Emulatoren und Simulatoren bieten nicht dieselben hardwaregestützten Garantien wie Geräte."

        StringKey.ABOUT_TITLE -> "Über"
        StringKey.ABOUT_KSAFE_VERSION -> "KSafe {0}"
        StringKey.ABOUT_DESCRIPTION ->
            "Eine Kotlin-Multiplatform-Bibliothek für sichere, verschlüsselte Persistenz auf " +
                "Basis des Keystore oder Schlüsselbunds der Plattform."
        StringKey.ABOUT_DEVELOPER -> "Entwickler"
        StringKey.ABOUT_PERSONAL_WEBSITE -> "Persönliche Website"
        StringKey.ABOUT_GITHUB_PROFILE -> "GitHub-Profil"
        StringKey.ABOUT_PROJECTS -> "Projekte"
        StringKey.ABOUT_KSAFE_LIBRARY -> "KSafe-Bibliothek"
        StringKey.ABOUT_OPEN_KSAFE_REPOSITORY -> "KSafe-Repository öffnen"
        StringKey.ABOUT_KSAFE_DEMO_APPLICATION -> "KSafeDemo-Anwendung"
        StringKey.ABOUT_OPEN_KSAFE_DEMO_REPOSITORY -> "KSafeDemo-Repository öffnen"

        StringKey.UNMATCHED -> ""
    }
}
