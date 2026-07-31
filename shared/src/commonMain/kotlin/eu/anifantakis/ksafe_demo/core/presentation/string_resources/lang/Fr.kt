package eu.anifantakis.ksafe_demo.core.presentation.string_resources.lang

import eu.anifantakis.ksafe_demo.core.presentation.string_resources.LanguageStrings
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey

internal class Fr : LanguageStrings {
    override fun getString(key: StringKey): String = when (key) {
        StringKey.COMMON_PROCESSING -> "Traitement en cours"
        StringKey.COMMON_BACK -> "Retour"
        StringKey.COMMON_OPEN_APPLICATION_MENU -> "Ouvrir le menu de l’application"
        StringKey.COMMON_PREFERENCES -> "Préférences"
        StringKey.COMMON_ABOUT -> "À propos"
        StringKey.COMMON_CLEAR -> "Effacer"
        StringKey.COMMON_CANCEL -> "Annuler"
        StringKey.COMMON_OK -> "OK"
        StringKey.COMMON_SAVE -> "Enregistrer"
        StringKey.COMMON_EMAIL -> "E-mail"
        StringKey.APP_PRESENTING_KSAFE -> "Présentation de KSafe {0}"
        StringKey.APP_STARTUP_LOADING -> "Chargement de KSafe…"
        StringKey.APP_STARTUP_FAILED -> "Impossible de démarrer KSafeDemo."
        StringKey.APP_STARTUP_RETRY -> "Réessayer"
        StringKey.NAV_COUNTERS -> "Compteurs"
        StringKey.NAV_FLOWS -> "Flux"
        StringKey.NAV_CUSTOM_JSON -> "JSON personnalisé"
        StringKey.NAV_SECURITY -> "Sécurité"

        StringKey.PREFERENCES_APPEARANCE -> "Apparence"
        StringKey.PREFERENCES_APPEARANCE_DESCRIPTION ->
            "Choisissez comment KSafeDemo sélectionne son thème de couleurs. La préférence " +
                "est enregistrée localement et appliquée immédiatement sur chaque plateforme."
        StringKey.PREFERENCES_THEME_DAY -> "Jour"
        StringKey.PREFERENCES_THEME_DAY_DESCRIPTION ->
            "Toujours utiliser la palette de couleurs claires"
        StringKey.PREFERENCES_THEME_NIGHT -> "Nuit"
        StringKey.PREFERENCES_THEME_NIGHT_DESCRIPTION ->
            "Toujours utiliser la palette de couleurs sombres"
        StringKey.PREFERENCES_THEME_SYSTEM -> "Système"
        StringKey.PREFERENCES_THEME_SYSTEM_DESCRIPTION ->
            "Suivre l’apparence du système d’exploitation"
        StringKey.PREFERENCES_LANGUAGE -> "Langue"
        StringKey.PREFERENCES_LANGUAGE_DESCRIPTION ->
            "Choisissez la langue utilisée par l’application."
        StringKey.PREFERENCES_SELECT_LANGUAGE -> "Sélectionner la langue"

        StringKey.COUNTERS_MUTABLE_STATE_NO_PERSISTENCE ->
            "mutableStateOf (sans persistance)"
        StringKey.COUNTERS_COUNTER_1 -> "Compteur 1"
        StringKey.COUNTERS_PLAIN_STATE_RESETS ->
            "état simple — réinitialisé au redémarrage"
        StringKey.COUNTERS_KSAFE_MUTABLE_STATE_PERSISTED ->
            "ksafe.mutableStateOf (persistant)"
        StringKey.COUNTERS_COUNTER_2 -> "Compteur 2"
        StringKey.COUNTERS_ENCRYPTED_OBSERVED_ON_FLOWS ->
            "chiffré — observé dans l’onglet Flux"
        StringKey.COUNTERS_COUNTER_3 -> "Compteur 3"
        StringKey.COUNTERS_UNENCRYPTED -> "non chiffré"
        StringKey.COUNTERS_COUNTER_2_SYNC_DESCRIPTION ->
            "Le compteur 2 est aussi observé dans l’onglet Flux. Touchez « + » ici, puis " +
                "consultez Flux : la valeur synchronisée est mise à jour en temps réel."
        StringKey.COUNTERS_COUNTER_2_REFRESH_DESCRIPTION ->
            "Si l’onglet Flux a modifié le compteur 2, touchez Actualiser pour voir la dernière " +
                "valeur (sans scope, une actualisation manuelle est nécessaire)."
        StringKey.COUNTERS_REFRESH_COUNTER_2 -> "Actualiser le compteur 2"
        StringKey.COUNTERS_MUTABLE_STATE_FLOW_PERSISTED ->
            "ksafe.asMutableStateFlow (persistant, réactif)"
        StringKey.COUNTERS_COUNTER_2B -> "Compteur 2b"
        StringKey.COUNTERS_COUNTER_2C -> "Compteur 2c"
        StringKey.COUNTERS_FLOW_NO_REFRESH ->
            "MutableStateFlow — aucune actualisation nécessaire"
        StringKey.COUNTERS_FLOW_EXPLANATION ->
            "Même stockage, forme différente : un MutableStateFlow au lieu d’un State Compose. " +
                "Il reçoit un scope, s’abonne donc à sa clé et détecte seul les écritures " +
                "externes — c’est pourquoi il n’a pas besoin du bouton ci-dessus."
        StringKey.COUNTERS_DATA_CLASS_PERSISTED ->
            "ksafe.mutableStateOf — classe de données (persistante, chiffrée)"
        StringKey.COUNTERS_BIOMETRIC_COUNT -> "Bio : {0}"
        StringKey.COUNTERS_HARDWARE_SECURED_VAULT ->
            "Coffre sécurisé par le matériel (StrongBox / Secure Enclave)"
        StringKey.COUNTERS_VAULT_TOKEN -> "Jeton du coffre"
        StringKey.COUNTERS_NO_TOKEN_STORED -> "Aucun jeton enregistré"
        StringKey.COUNTERS_GENERATE_TOKEN -> "Générer un jeton"
        StringKey.COUNTERS_CLEAR_VAULT -> "Vider le coffre"
        StringKey.COUNTERS_BIO_AUTH_WINDOW ->
            "Fenêtre d’authentification biométrique ouverte — aucune demande pendant {0} s"
        StringKey.COUNTERS_KEY_ROTATION -> "Rotation des clés"
        StringKey.COUNTERS_KEY_ROTATION_DESCRIPTION ->
            "Chiffre de nouveau chaque entrée chiffrée avec une nouvelle génération de clés et " +
                "supprime les clés remplacées. L’opération concerne tout le stockage ; les " +
                "valeurs ne changent jamais, les compteurs ci-dessus restent donc identiques."
        StringKey.COUNTERS_ROTATING -> "Rotation en cours..."
        StringKey.COUNTERS_ROTATE_KEYS -> "Renouveler les clés"
        StringKey.COUNTERS_LOCK_STATE_POLICY_TEST ->
            "Test de la politique d’état verrouillé"
        StringKey.COUNTERS_LOCK_TEST_INSTRUCTIONS ->
            "Lancez depuis l’écran d’accueil, pas depuis Xcode, pour des résultats fiables"
        StringKey.COUNTERS_LOCK_DEVICE_COUNTDOWN ->
            "Verrouillez votre appareil maintenant ! Lecture dans {0} s..."
        StringKey.COUNTERS_ATTEMPTING_ENCRYPTED_READ ->
            "Tentative de lecture chiffrée..."
        StringKey.COUNTERS_TEST_LOCK_DEFAULT -> "Standard"
        StringKey.COUNTERS_TEST_LOCK_HARDWARE -> "Isolated"
        StringKey.COUNTERS_LOCK_TEST_RESULT -> "Résultat du test de verrouillage"
        StringKey.COUNTERS_CLEAR_ALL_TITLE -> "Effacer toutes les valeurs ?"
        StringKey.COUNTERS_CLEAR_ALL_DESCRIPTION ->
            "Toutes les clés enregistrées seront supprimées et les compteurs seront réinitialisés."
        StringKey.COUNTERS_BIOMETRICS_UNAVAILABLE ->
            "Biométrie indisponible — aucune empreinte, reconnaissance faciale ou protection " +
                "de l’appareil n’est configurée"
        StringKey.COUNTERS_AUTHENTICATE_TO_SAVE -> "Authentifiez-vous pour enregistrer"
        StringKey.COUNTERS_AUTHENTICATION_FAILED ->
            "L’authentification a échoué ou a été annulée"
        StringKey.COUNTERS_ROTATION_RESULT ->
            "Chiffrées à nouveau : {0}\nIgnorées : {1}\nÉchecs : {2}\n" +
                "La génération de clés est maintenant {3}"
        StringKey.COUNTERS_ROTATION_RESULT_WITH_SKIPPED ->
            "Chiffrées à nouveau : {0}\nIgnorées : {1}\nÉchecs : {2}\n" +
                "La génération de clés est maintenant {3}\n\nLes entrées ignorées étaient en " +
                "cours d’écriture ou sont des entrées strictes sur un appareil verrouillé. " +
                "Elles restent lisibles avec leur ancienne clé ; relancez la rotation pour " +
                "les traiter."
        StringKey.COUNTERS_ROTATION_FAILED -> "Échec de la rotation :\n{0}"
        StringKey.COUNTERS_LOCK_SETUP_FAILED ->
            "ÉCHEC DE LA CONFIGURATION.\n\nImpossible d’enregistrer la valeur de test : {0}"
        StringKey.COUNTERS_LOCK_READ_SUCCEEDED ->
            "LECTURE RÉUSSIE pendant le verrouillage.\n\nLa lecture chiffrée n’a PAS été " +
                "bloquée.\n\nRemarque : requireUnlockedDevice est appliqué uniquement sur " +
                "Android et Apple. JVM Desktop n’a pas d’état de verrouillage auquel lier la " +
                "clé, et les navigateurs n’ont ni cela ni un déchiffrement synchrone. KSafe y " +
                "ignore donc l’option au lieu de rendre la valeur accessible en écriture seule. " +
                "Sur Web et Desktop, ce test lit donc toujours la valeur : c’est le comportement " +
                "documenté, pas une erreur."
        StringKey.COUNTERS_LOCK_READ_SUCCEEDED_WITH_DEBUGGER ->
            "LECTURE RÉUSSIE pendant le verrouillage.\n\nLa lecture chiffrée n’a PAS été " +
                "bloquée.\n\nRemarque : requireUnlockedDevice est appliqué uniquement sur " +
                "Android et Apple. JVM Desktop n’a pas d’état de verrouillage auquel lier la " +
                "clé, et les navigateurs n’ont ni cela ni un déchiffrement synchrone. KSafe y " +
                "ignore donc l’option au lieu de rendre la valeur accessible en écriture seule. " +
                "Sur Web et Desktop, ce test lit donc toujours la valeur : c’est le comportement " +
                "documenté, pas une erreur.\n\nUn débogueur ou une version de débogage a été " +
                "détecté. Le débogueur de Xcode empêche l’activation de la protection des données " +
                "iOS : le Trousseau reste déverrouillé lorsqu’il est connecté.\n\nPour tester " +
                "correctement :\n1. Compilez et lancez l’application sur votre appareil\n" +
                "2. Appuyez sur Stop dans Xcode\n3. Lancez l’application depuis l’écran d’accueil\n" +
                "4. Relancez ce test"
        StringKey.COUNTERS_LOCK_UNEXPECTED_RESULT ->
            "RÉSULTAT INATTENDU.\n\nLa lecture a renvoyé : « {0} »"
        StringKey.COUNTERS_LOCK_READ_BLOCKED ->
            "LECTURE BLOQUÉE — la fonctionnalité marche !\n\nLa lecture chiffrée a échoué " +
                "pendant que l’appareil était verrouillé :\n{0}\n\nCela confirme que " +
                "requireUnlockedDevice fonctionne correctement."

        StringKey.FLOWS_TITLE -> "Délégués de flux (1.8.0+)"
        StringKey.FLOWS_SUBTITLE -> "MutableStateFlow + chiffrement + persistance"
        StringKey.FLOWS_MUTABLE_STATE_FLOW_SECTION ->
            "asMutableStateFlow — MutableStateFlow directement compatible"
        StringKey.FLOWS_MOVIES_STATE -> "MoviesListState (persistant + chiffré)"
        StringKey.FLOWS_LOADING_MOVIES -> "Chargement des films..."
        StringKey.FLOWS_ERROR -> "Erreur : {0}"
        StringKey.FLOWS_NO_MOVIES_LOADED -> "Aucun film chargé"
        StringKey.FLOWS_LOAD_MOVIES -> "Charger les films"
        StringKey.FLOWS_AS_FLOW_SECTION -> "asFlow et liaison bidirectionnelle"
        StringKey.FLOWS_USERNAME -> "Nom d’utilisateur (asMutableStateFlow)"
        StringKey.FLOWS_TOGGLE_VALUE -> "Basculer une valeur (asFlow)"
        StringKey.FLOWS_DERIVED_VALUE -> "Dérivé : {0}"
        StringKey.FLOWS_ON_MODE -> "Mode activé"
        StringKey.FLOWS_OFF_MODE -> "Mode désactivé"
        StringKey.FLOWS_DEFAULT_USERNAME -> "Invité"
        StringKey.FLOWS_SCOPE_SYNC_SECTION ->
            "mutableStateOf(scope) — synchronisation entre écrans"
        StringKey.FLOWS_ISOLATED_NO_SCOPE -> "isolé (sans scope)"
        StringKey.FLOWS_SYNCED_WITH_SCOPE -> "synchronisé (avec scope)"
        StringKey.FLOWS_INCREMENT_FROM_SCREEN -> "+1 depuis cet écran"
        StringKey.FLOWS_REFRESH_ISOLATED -> "Actualiser la valeur isolée"
        StringKey.FLOWS_SYNC_EXPLANATION ->
            "La valeur avec scope est mise à jour en temps réel ; la valeur isolée nécessite " +
                "une actualisation manuelle."
        StringKey.FLOWS_CLEAR_ALL -> "Effacer toutes les démos de flux"

        StringKey.CUSTOM_JSON_TITLE -> "Sérialisation JSON personnalisée"
        StringKey.CUSTOM_JSON_SUBTITLE ->
            "Enregistrez des classes de données avec des champs @Contextual nécessitant des " +
                "sérialiseurs personnalisés."
        StringKey.CUSTOM_JSON_DEFINE_SERIALIZERS ->
            "1. Définir des sérialiseurs personnalisés"
        StringKey.CUSTOM_JSON_REGISTER_SERIALIZERS -> "2. Enregistrer les sérialiseurs"
        StringKey.CUSTOM_JSON_PASS_THROUGH_CONFIG -> "3. Les transmettre à KSafeConfig"
        StringKey.CUSTOM_JSON_USE_CONTEXTUAL_FIELDS -> "4. Utiliser les champs @Contextual"
        StringKey.CUSTOM_JSON_TRY_IT -> "Essayez"
        StringKey.CUSTOM_JSON_NAME -> "Nom"
        StringKey.CUSTOM_JSON_SAVED_COUNT ->
            "Enregistré {0} fois — la couleur change à chaque enregistrement"
        StringKey.CUSTOM_JSON_STORED_VALUES ->
            "Valeurs enregistrées (conservées après redémarrage)"
        StringKey.CUSTOM_JSON_ENCRYPTED -> "Chiffré"
        StringKey.CUSTOM_JSON_PLAIN_TEXT -> "Texte en clair"
        StringKey.CUSTOM_JSON_PROFILE_NAME -> "nom : {0}"
        StringKey.CUSTOM_JSON_PROFILE_CREATED_AT -> "créé le : {0}"
        StringKey.CUSTOM_JSON_PROFILE_FAVORITE_COLOR -> "couleur favorite : {0}"

        StringKey.SECURITY_TITLE -> "État de la sécurité"
        StringKey.SECURITY_SUBTITLE ->
            "Démonstration de la politique de sécurité et de la garde des clés KSafe"
        StringKey.SECURITY_SECURE_ENVIRONMENT -> "Environnement sécurisé"
        StringKey.SECURITY_WARNINGS_DETECTED -> "{0} avertissement(s) détecté(s)"
        StringKey.SECURITY_KEY_PROTECTION -> "Protection des clés"
        StringKey.SECURITY_INTENDED -> "Prévue"
        StringKey.SECURITY_EFFECTIVE -> "Réelle"
        StringKey.SECURITY_CUSTODY -> "Garde"
        StringKey.SECURITY_NOTES -> "Remarques"
        StringKey.SECURITY_DEGRADED_FROM -> "Dégradée depuis {0}"
        StringKey.SECURITY_REFRESH_STATUS -> "Actualiser l’état de la sécurité"
        StringKey.SECURITY_LIVE_INFO ->
            "Les informations de protection sont dynamiques et peuvent changer après une " +
                "rotation ou une récupération de clé."
        StringKey.SECURITY_CHECKS -> "Contrôles de sécurité"
        StringKey.SECURITY_WARNING -> "AVERTISSEMENT"
        StringKey.SECURITY_OK -> "OK"
        StringKey.SECURITY_CURRENT_POLICY -> "Politique actuelle : {0}"
        StringKey.SECURITY_POLICY_DESCRIPTION ->
            "La démo signale les problèmes de sécurité sans bloquer les fonctionnalités. Les " +
                "applications de production traitant des données sensibles doivent choisir " +
                "une politique adaptée à leur modèle de menace."
        StringKey.SECURITY_ROOTED_TITLE -> "Détection de root/jailbreak"
        StringKey.SECURITY_ROOTED_DESCRIPTION ->
            "L’appareil est rooté ou jailbreaké, ce qui peut affaiblir l’isolation de l’application."
        StringKey.SECURITY_DEBUGGER_TITLE -> "Détection du débogueur"
        StringKey.SECURITY_DEBUGGER_DESCRIPTION ->
            "Un débogueur peut inspecter la mémoire d’exécution, y compris les valeurs déchiffrées."
        StringKey.SECURITY_DEBUG_BUILD_TITLE -> "Détection de version de débogage"
        StringKey.SECURITY_DEBUG_BUILD_DESCRIPTION ->
            "Les versions de débogage peuvent exposer plus d’informations et employer des " +
                "contrôles opérationnels plus faibles."
        StringKey.SECURITY_EMULATOR_TITLE -> "Détection d’émulateur"
        StringKey.SECURITY_EMULATOR_DESCRIPTION ->
            "Les émulateurs et simulateurs n’offrent pas les mêmes garanties matérielles que " +
                "les appareils physiques."

        StringKey.ABOUT_TITLE -> "À propos"
        StringKey.ABOUT_KSAFE_VERSION -> "KSafe {0}"
        StringKey.ABOUT_DESCRIPTION ->
            "Une bibliothèque Kotlin Multiplatform de persistance sécurisée et chiffrée, " +
                "reposant sur le Keystore ou le Trousseau de chaque plateforme."
        StringKey.ABOUT_DEVELOPER -> "Développeur"
        StringKey.ABOUT_PERSONAL_WEBSITE -> "Site personnel"
        StringKey.ABOUT_GITHUB_PROFILE -> "Profil GitHub"
        StringKey.ABOUT_PROJECTS -> "Projets"
        StringKey.ABOUT_KSAFE_LIBRARY -> "Bibliothèque KSafe"
        StringKey.ABOUT_OPEN_KSAFE_REPOSITORY -> "Ouvrir le dépôt KSafe"
        StringKey.ABOUT_KSAFE_DEMO_APPLICATION -> "Application KSafeDemo"
        StringKey.ABOUT_OPEN_KSAFE_DEMO_REPOSITORY -> "Ouvrir le dépôt KSafeDemo"

        StringKey.UNMATCHED -> ""
    }
}
