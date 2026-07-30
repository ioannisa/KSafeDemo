package eu.anifantakis.ksafe_demo.core.presentation.string_resources.lang

import eu.anifantakis.ksafe_demo.core.presentation.string_resources.LanguageStrings
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey

internal class It : LanguageStrings {
    override fun getString(key: StringKey): String = when (key) {
        StringKey.COMMON_PROCESSING -> "Elaborazione"
        StringKey.COMMON_BACK -> "Indietro"
        StringKey.COMMON_OPEN_APPLICATION_MENU -> "Apri menu applicazione"
        StringKey.COMMON_PREFERENCES -> "Preferenze"
        StringKey.COMMON_ABOUT -> "Informazioni"
        StringKey.COMMON_CLEAR -> "Cancella"
        StringKey.COMMON_CANCEL -> "Annulla"
        StringKey.COMMON_OK -> "OK"
        StringKey.COMMON_SAVE -> "Salva"
        StringKey.COMMON_EMAIL -> "E-mail"
        StringKey.APP_PRESENTING_KSAFE -> "Presentazione di KSafe {0}"
        StringKey.APP_STARTUP_LOADING -> "Caricamento di KSafe…"
        StringKey.APP_STARTUP_FAILED -> "Impossibile avviare KSafeDemo."
        StringKey.APP_STARTUP_RETRY -> "Riprova"
        StringKey.NAV_COUNTERS -> "Contatori"
        StringKey.NAV_FLOWS -> "Flussi"
        StringKey.NAV_CUSTOM_JSON -> "JSON personalizzato"
        StringKey.NAV_SECURITY -> "Sicurezza"

        StringKey.PREFERENCES_APPEARANCE -> "Aspetto"
        StringKey.PREFERENCES_APPEARANCE_DESCRIPTION ->
            "Scegli come KSafeDemo seleziona il tema dei colori. La preferenza viene salvata " +
                "localmente e applicata subito su ogni piattaforma."
        StringKey.PREFERENCES_THEME_DAY -> "Giorno"
        StringKey.PREFERENCES_THEME_DAY_DESCRIPTION -> "Usa sempre la palette chiara"
        StringKey.PREFERENCES_THEME_NIGHT -> "Notte"
        StringKey.PREFERENCES_THEME_NIGHT_DESCRIPTION -> "Usa sempre la palette scura"
        StringKey.PREFERENCES_THEME_SYSTEM -> "Sistema"
        StringKey.PREFERENCES_THEME_SYSTEM_DESCRIPTION ->
            "Segui l'aspetto del sistema operativo"
        StringKey.PREFERENCES_LANGUAGE -> "Lingua"
        StringKey.PREFERENCES_LANGUAGE_DESCRIPTION ->
            "Scegli la lingua utilizzata dall'applicazione."
        StringKey.PREFERENCES_SELECT_LANGUAGE -> "Seleziona lingua"

        StringKey.COUNTERS_MUTABLE_STATE_NO_PERSISTENCE ->
            "mutableStateOf (senza persistenza)"
        StringKey.COUNTERS_COUNTER_1 -> "Contatore 1"
        StringKey.COUNTERS_PLAIN_STATE_RESETS ->
            "stato semplice — si azzera al riavvio"
        StringKey.COUNTERS_KSAFE_MUTABLE_STATE_PERSISTED ->
            "ksafe.mutableStateOf (persistente)"
        StringKey.COUNTERS_COUNTER_2 -> "Contatore 2"
        StringKey.COUNTERS_ENCRYPTED_OBSERVED_ON_FLOWS ->
            "crittografato — osservato nella scheda Flussi"
        StringKey.COUNTERS_COUNTER_3 -> "Contatore 3"
        StringKey.COUNTERS_UNENCRYPTED -> "non crittografato"
        StringKey.COUNTERS_COUNTER_2_SYNC_DESCRIPTION ->
            "Il Contatore 2 è osservato anche nella scheda Flussi. Tocca \"+\" qui, poi " +
                "controlla Flussi: il valore sincronizzato si aggiorna in tempo reale."
        StringKey.COUNTERS_COUNTER_2_REFRESH_DESCRIPTION ->
            "Se la scheda Flussi ha modificato il Contatore 2, tocca Aggiorna per vedere il " +
                "valore più recente (senza scope è necessario l'aggiornamento manuale)."
        StringKey.COUNTERS_REFRESH_COUNTER_2 -> "Aggiorna Contatore 2"
        StringKey.COUNTERS_MUTABLE_STATE_FLOW_PERSISTED ->
            "ksafe.asMutableStateFlow (persistente, reattivo)"
        StringKey.COUNTERS_COUNTER_2B -> "Contatore 2b"
        StringKey.COUNTERS_COUNTER_2C -> "Contatore 2c"
        StringKey.COUNTERS_FLOW_NO_REFRESH ->
            "MutableStateFlow — nessun aggiornamento necessario"
        StringKey.COUNTERS_FLOW_EXPLANATION ->
            "Stessa memoria, forma diversa: un MutableStateFlow invece di uno State di Compose. " +
                "Riceve uno scope, si iscrive alla propria chiave e rileva automaticamente le " +
                "scritture esterne, quindi non necessita del pulsante qui sopra."
        StringKey.COUNTERS_DATA_CLASS_PERSISTED ->
            "ksafe.mutableStateOf — data class (persistente, crittografata)"
        StringKey.COUNTERS_BIOMETRIC_COUNT -> "Bio: {0}"
        StringKey.COUNTERS_HARDWARE_SECURED_VAULT ->
            "Cassaforte protetta dall'hardware (StrongBox / Secure Enclave)"
        StringKey.COUNTERS_VAULT_TOKEN -> "Token cassaforte"
        StringKey.COUNTERS_NO_TOKEN_STORED -> "Nessun token salvato"
        StringKey.COUNTERS_GENERATE_TOKEN -> "Genera token"
        StringKey.COUNTERS_CLEAR_VAULT -> "Svuota cassaforte"
        StringKey.COUNTERS_BIO_AUTH_WINDOW ->
            "Finestra biometrica aperta — nessuna richiesta per {0} s"
        StringKey.COUNTERS_KEY_ROTATION -> "Rotazione delle chiavi"
        StringKey.COUNTERS_KEY_ROTATION_DESCRIPTION ->
            "Ricrittografa ogni voce con una nuova generazione di chiavi e rimuove le chiavi " +
                "sostituite. L'operazione riguarda l'intero archivio, ma i valori non cambiano."
        StringKey.COUNTERS_ROTATING -> "Rotazione..."
        StringKey.COUNTERS_ROTATE_KEYS -> "Ruota chiavi"
        StringKey.COUNTERS_LOCK_STATE_POLICY_TEST -> "Test dei criteri di blocco"
        StringKey.COUNTERS_LOCK_TEST_INSTRUCTIONS ->
            "Per risultati accurati, avvia dalla schermata Home e non da Xcode"
        StringKey.COUNTERS_LOCK_DEVICE_COUNTDOWN ->
            "Blocca ora il dispositivo! Lettura tra {0} s..."
        StringKey.COUNTERS_ATTEMPTING_ENCRYPTED_READ ->
            "Tentativo di lettura crittografata..."
        StringKey.COUNTERS_TEST_LOCK_DEFAULT -> "Test blocco (Predefinito)"
        StringKey.COUNTERS_TEST_LOCK_HARDWARE -> "Test blocco (Hardware)"
        StringKey.COUNTERS_LOCK_TEST_RESULT -> "Risultato test blocco"
        StringKey.COUNTERS_CLEAR_ALL_TITLE -> "Cancellare tutti i valori?"
        StringKey.COUNTERS_CLEAR_ALL_DESCRIPTION ->
            "Tutte le chiavi salvate verranno eliminate e i contatori torneranno ai valori iniziali."
        StringKey.COUNTERS_BIOMETRICS_UNAVAILABLE ->
            "Biometria non disponibile: non sono configurati impronta, volto o blocco dispositivo"
        StringKey.COUNTERS_AUTHENTICATE_TO_SAVE -> "Autenticati per salvare"
        StringKey.COUNTERS_AUTHENTICATION_FAILED ->
            "Autenticazione non riuscita o annullata"
        StringKey.COUNTERS_ROTATION_RESULT ->
            "Ricrittografate: {0}\nSaltate: {1}\nNon riuscite: {2}\n" +
                "Generazione chiave attuale: {3}"
        StringKey.COUNTERS_ROTATION_RESULT_WITH_SKIPPED ->
            "Ricrittografate: {0}\nSaltate: {1}\nNon riuscite: {2}\n" +
                "Generazione chiave attuale: {3}\n\nLe voci saltate erano in fase di scrittura " +
                "oppure sono voci rigide su un dispositivo bloccato. Restano leggibili con la " +
                "chiave precedente; esegui di nuovo la rotazione per includerle."
        StringKey.COUNTERS_ROTATION_FAILED -> "Rotazione non riuscita:\n{0}"
        StringKey.COUNTERS_LOCK_SETUP_FAILED ->
            "CONFIGURAZIONE NON RIUSCITA.\n\nImpossibile salvare il valore di test: {0}"
        StringKey.COUNTERS_LOCK_READ_SUCCEEDED ->
            "LETTURA RIUSCITA CON DISPOSITIVO BLOCCATO.\n\nLa lettura crittografata NON è stata " +
                "bloccata.\n\nNota: requireUnlockedDevice è applicato solo su Android e Apple. " +
                "JVM Desktop non ha un concetto di blocco dispositivo e i browser non hanno né " +
                "questo né una decrittazione sincrona, quindi KSafe ignora il flag. Su Web e " +
                "Desktop il test legge sempre il valore: è il comportamento documentato, non " +
                "un errore."
        StringKey.COUNTERS_LOCK_READ_SUCCEEDED_WITH_DEBUGGER ->
            "LETTURA RIUSCITA CON DISPOSITIVO BLOCCATO.\n\nLa lettura crittografata NON è stata " +
                "bloccata.\n\nSu Web e Desktop questo è il comportamento documentato.\n\nÈ stato " +
                "rilevato un debugger o una build di debug. Il debugger di Xcode impedisce " +
                "l'attivazione della protezione dati iOS e il Portachiavi resta sbloccato.\n\n" +
                "Per un test accurato:\n1. Compila e avvia l'app sul dispositivo\n" +
                "2. Premi Stop in Xcode\n3. Avvia l'app dalla schermata Home\n" +
                "4. Esegui nuovamente il test"
        StringKey.COUNTERS_LOCK_UNEXPECTED_RESULT ->
            "RISULTATO IMPREVISTO.\n\nLa lettura ha restituito: \"{0}\""
        StringKey.COUNTERS_LOCK_READ_BLOCKED ->
            "LETTURA BLOCCATA — la funzione opera correttamente!\n\nLa lettura crittografata " +
                "non è riuscita mentre il dispositivo era bloccato:\n{0}\n\nCiò conferma che " +
                "requireUnlockedDevice funziona correttamente."

        StringKey.FLOWS_TITLE -> "Delegati Flow (1.8.0+)"
        StringKey.FLOWS_SUBTITLE -> "MutableStateFlow + crittografia + persistenza"
        StringKey.FLOWS_MUTABLE_STATE_FLOW_SECTION ->
            "asMutableStateFlow — sostituzione diretta di MutableStateFlow"
        StringKey.FLOWS_MOVIES_STATE -> "MoviesListState (persistente + crittografato)"
        StringKey.FLOWS_LOADING_MOVIES -> "Caricamento film..."
        StringKey.FLOWS_ERROR -> "Errore: {0}"
        StringKey.FLOWS_NO_MOVIES_LOADED -> "Nessun film caricato"
        StringKey.FLOWS_LOAD_MOVIES -> "Carica film"
        StringKey.FLOWS_AS_FLOW_SECTION -> "asFlow e binding bidirezionale"
        StringKey.FLOWS_USERNAME -> "Nome utente (asMutableStateFlow)"
        StringKey.FLOWS_TOGGLE_VALUE -> "Attiva/disattiva valore (asFlow)"
        StringKey.FLOWS_DERIVED_VALUE -> "Derivato: {0}"
        StringKey.FLOWS_ON_MODE -> "Modalità attiva"
        StringKey.FLOWS_OFF_MODE -> "Modalità disattiva"
        StringKey.FLOWS_DEFAULT_USERNAME -> "Ospite"
        StringKey.FLOWS_SCOPE_SYNC_SECTION ->
            "mutableStateOf(scope) — sincronizzazione tra schermate"
        StringKey.FLOWS_ISOLATED_NO_SCOPE -> "isolato (senza scope)"
        StringKey.FLOWS_SYNCED_WITH_SCOPE -> "sincronizzato (con scope)"
        StringKey.FLOWS_INCREMENT_FROM_SCREEN -> "+1 da questa schermata"
        StringKey.FLOWS_REFRESH_ISOLATED -> "Aggiorna isolato"
        StringKey.FLOWS_SYNC_EXPLANATION ->
            "Il valore con scope si aggiorna in tempo reale; quello isolato richiede un " +
                "aggiornamento manuale."
        StringKey.FLOWS_CLEAR_ALL -> "Cancella tutte le demo Flow"

        StringKey.CUSTOM_JSON_TITLE -> "Serializzazione JSON personalizzata"
        StringKey.CUSTOM_JSON_SUBTITLE ->
            "Salva data class con campi @Contextual che richiedono serializer personalizzati."
        StringKey.CUSTOM_JSON_DEFINE_SERIALIZERS -> "1. Definisci serializer personalizzati"
        StringKey.CUSTOM_JSON_REGISTER_SERIALIZERS -> "2. Registra i serializer"
        StringKey.CUSTOM_JSON_PASS_THROUGH_CONFIG -> "3. Passali tramite KSafeConfig"
        StringKey.CUSTOM_JSON_USE_CONTEXTUAL_FIELDS -> "4. Usa campi @Contextual"
        StringKey.CUSTOM_JSON_TRY_IT -> "Prova"
        StringKey.CUSTOM_JSON_NAME -> "Nome"
        StringKey.CUSTOM_JSON_SAVED_COUNT ->
            "Salvato {0} volta/e — il colore cambia a ogni salvataggio"
        StringKey.CUSTOM_JSON_STORED_VALUES ->
            "Valori salvati (persistenti tra i riavvii)"
        StringKey.CUSTOM_JSON_ENCRYPTED -> "Crittografato"
        StringKey.CUSTOM_JSON_PLAIN_TEXT -> "Testo in chiaro"
        StringKey.CUSTOM_JSON_PROFILE_NAME -> "nome: {0}"
        StringKey.CUSTOM_JSON_PROFILE_CREATED_AT -> "creato il: {0}"
        StringKey.CUSTOM_JSON_PROFILE_FAVORITE_COLOR -> "colore preferito: {0}"

        StringKey.SECURITY_TITLE -> "Stato di sicurezza"
        StringKey.SECURITY_SUBTITLE -> "Demo dei criteri di sicurezza e custodia chiavi di KSafe"
        StringKey.SECURITY_SECURE_ENVIRONMENT -> "Ambiente sicuro"
        StringKey.SECURITY_WARNINGS_DETECTED -> "{0} avviso/i rilevato/i"
        StringKey.SECURITY_KEY_PROTECTION -> "Protezione delle chiavi"
        StringKey.SECURITY_INTENDED -> "Previsto"
        StringKey.SECURITY_EFFECTIVE -> "Effettivo"
        StringKey.SECURITY_CUSTODY -> "Custodia"
        StringKey.SECURITY_NOTES -> "Note"
        StringKey.SECURITY_DEGRADED_FROM -> "Ridotto da {0}"
        StringKey.SECURITY_REFRESH_STATUS -> "Aggiorna stato di sicurezza"
        StringKey.SECURITY_LIVE_INFO ->
            "Le informazioni di protezione sono live e possono cambiare dopo una rotazione o un recupero."
        StringKey.SECURITY_CHECKS -> "Controlli di sicurezza"
        StringKey.SECURITY_WARNING -> "AVVISO"
        StringKey.SECURITY_OK -> "OK"
        StringKey.SECURITY_CURRENT_POLICY -> "Criterio attuale: {0}"
        StringKey.SECURITY_POLICY_DESCRIPTION ->
            "La demo segnala i problemi di sicurezza senza bloccare le funzionalità. Le " +
                "applicazioni di produzione con dati sensibili devono scegliere un criterio " +
                "adeguato al proprio modello di minaccia."
        StringKey.SECURITY_ROOTED_TITLE -> "Rilevamento root/jailbreak"
        StringKey.SECURITY_ROOTED_DESCRIPTION ->
            "Il dispositivo è rooted o sottoposto a jailbreak e ciò può indebolire la sandbox."
        StringKey.SECURITY_DEBUGGER_TITLE -> "Rilevamento debugger"
        StringKey.SECURITY_DEBUGGER_DESCRIPTION ->
            "Un debugger può ispezionare la memoria a runtime, inclusi i valori decrittografati."
        StringKey.SECURITY_DEBUG_BUILD_TITLE -> "Rilevamento build di debug"
        StringKey.SECURITY_DEBUG_BUILD_DESCRIPTION ->
            "Le build di debug possono esporre più informazioni e usare controlli operativi più deboli."
        StringKey.SECURITY_EMULATOR_TITLE -> "Rilevamento emulatore"
        StringKey.SECURITY_EMULATOR_DESCRIPTION ->
            "Emulatori e simulatori non offrono le stesse garanzie hardware dei dispositivi."

        StringKey.ABOUT_TITLE -> "Informazioni"
        StringKey.ABOUT_KSAFE_VERSION -> "KSafe {0}"
        StringKey.ABOUT_DESCRIPTION ->
            "Una libreria Kotlin Multiplatform per la persistenza sicura e crittografata, " +
                "supportata dal Keystore o Portachiavi della piattaforma."
        StringKey.ABOUT_DEVELOPER -> "Sviluppatore"
        StringKey.ABOUT_PERSONAL_WEBSITE -> "Sito personale"
        StringKey.ABOUT_GITHUB_PROFILE -> "Profilo GitHub"
        StringKey.ABOUT_PROJECTS -> "Progetti"
        StringKey.ABOUT_KSAFE_LIBRARY -> "Libreria KSafe"
        StringKey.ABOUT_OPEN_KSAFE_REPOSITORY -> "Apri repository KSafe"
        StringKey.ABOUT_KSAFE_DEMO_APPLICATION -> "Applicazione KSafeDemo"
        StringKey.ABOUT_OPEN_KSAFE_DEMO_REPOSITORY -> "Apri repository KSafeDemo"

        StringKey.UNMATCHED -> ""
    }
}
