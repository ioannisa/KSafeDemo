package eu.anifantakis.ksafe_demo.core.presentation.string_resources.lang

import eu.anifantakis.ksafe_demo.core.presentation.string_resources.LanguageStrings
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey

internal class El : LanguageStrings {
    override fun getString(key: StringKey): String = when (key) {
        StringKey.COMMON_PROCESSING -> "Επεξεργασία"
        StringKey.COMMON_BACK -> "Πίσω"
        StringKey.COMMON_OPEN_APPLICATION_MENU -> "Άνοιγμα μενού εφαρμογής"
        StringKey.COMMON_PREFERENCES -> "Προτιμήσεις"
        StringKey.COMMON_ABOUT -> "Σχετικά"
        StringKey.COMMON_CLEAR -> "Καθ/σμος"
        StringKey.COMMON_CANCEL -> "Ακύρωση"
        StringKey.COMMON_OK -> "OK"
        StringKey.COMMON_SAVE -> "Αποθήκευση"
        StringKey.COMMON_EMAIL -> "Email"
        StringKey.APP_PRESENTING_KSAFE -> "Παρουσιάζοντας το KSafe {0}"
        StringKey.APP_STARTUP_LOADING -> "Φόρτωση της KSafe…"
        StringKey.APP_STARTUP_FAILED -> "Δεν ήταν δυνατή η εκκίνηση του KSafeDemo."
        StringKey.APP_STARTUP_RETRY -> "Δοκιμή ξανά"
        StringKey.NAV_COUNTERS -> "Μετρητές"
        StringKey.NAV_FLOWS -> "Ροές"
        StringKey.NAV_CUSTOM_JSON -> "Cust. JSON"
        StringKey.NAV_SECURITY -> "Ασφάλεια"

        StringKey.PREFERENCES_APPEARANCE -> "Εμφάνιση"
        StringKey.PREFERENCES_APPEARANCE_DESCRIPTION ->
            "Επιλέξτε πώς το KSafeDemo καθορίζει το χρωματικό θέμα του. Η προτίμηση " +
                "αποθηκεύεται τοπικά και εφαρμόζεται αμέσως σε κάθε πλατφόρμα."
        StringKey.PREFERENCES_THEME_DAY -> "Ημέρα"
        StringKey.PREFERENCES_THEME_DAY_DESCRIPTION ->
            "Πάντα χρήση της φωτεινής χρωματικής παλέτας"
        StringKey.PREFERENCES_THEME_NIGHT -> "Νύχτα"
        StringKey.PREFERENCES_THEME_NIGHT_DESCRIPTION ->
            "Πάντα χρήση της σκοτεινής χρωματικής παλέτας"
        StringKey.PREFERENCES_THEME_SYSTEM -> "Σύστημα"
        StringKey.PREFERENCES_THEME_SYSTEM_DESCRIPTION ->
            "Ακολούθηση της εμφάνισης του λειτουργικού συστήματος"
        StringKey.PREFERENCES_LANGUAGE -> "Γλώσσα"
        StringKey.PREFERENCES_LANGUAGE_DESCRIPTION ->
            "Επιλέξτε τη γλώσσα που χρησιμοποιεί η εφαρμογή."
        StringKey.PREFERENCES_SELECT_LANGUAGE -> "Επιλογή γλώσσας"

        StringKey.COUNTERS_MUTABLE_STATE_NO_PERSISTENCE ->
            "mutableStateOf (χωρίς αποθήκευση)"
        StringKey.COUNTERS_COUNTER_1 -> "Μετρητής 1"
        StringKey.COUNTERS_PLAIN_STATE_RESETS ->
            "απλή κατάσταση — μηδενίζεται στην επανεκκίνηση"
        StringKey.COUNTERS_KSAFE_MUTABLE_STATE_PERSISTED ->
            "ksafe.mutableStateOf (αποθηκευμένο)"
        StringKey.COUNTERS_COUNTER_2 -> "Μετρητής 2"
        StringKey.COUNTERS_ENCRYPTED_OBSERVED_ON_FLOWS ->
            "κρυπτογραφημένο — παρακολουθείται στην καρτέλα Ροές"
        StringKey.COUNTERS_COUNTER_3 -> "Μετρητής 3"
        StringKey.COUNTERS_UNENCRYPTED -> "μη κρυπτογραφημένο"
        StringKey.COUNTERS_COUNTER_2_SYNC_DESCRIPTION ->
            "Ο Μετρητής 2 παρακολουθείται και στην καρτέλα Ροές. Πατήστε «+» εδώ και μετά " +
                "ελέγξτε τις Ροές — η συγχρονισμένη τιμή ενημερώνεται σε πραγματικό χρόνο."
        StringKey.COUNTERS_COUNTER_2_REFRESH_DESCRIPTION ->
            "Αν η καρτέλα Ροές έγραψε στον Μετρητή 2, πατήστε Ανανέωση για την τελευταία τιμή " +
                "(χωρίς scope απαιτείται χειροκίνητη ανανέωση)."
        StringKey.COUNTERS_REFRESH_COUNTER_2 -> "Ανανέωση Μετρητή 2"
        StringKey.COUNTERS_MUTABLE_STATE_FLOW_PERSISTED ->
            "ksafe.asMutableStateFlow (αποθηκευμένο, αντιδραστικό)"
        StringKey.COUNTERS_COUNTER_2B -> "Μετρητής 2b"
        StringKey.COUNTERS_COUNTER_2C -> "Μετρητής 2c"
        StringKey.COUNTERS_FLOW_NO_REFRESH ->
            "MutableStateFlow — δε χρειάζεται ανανέωση"
        StringKey.COUNTERS_FLOW_EXPLANATION ->
            "Ίδια αποθήκευση, διαφορετική μορφή: ένα MutableStateFlow αντί για Compose State. " +
                "Δέχεται scope, άρα εγγράφεται στο κλειδί του και εντοπίζει μόνο του εξωτερικές " +
                "εγγραφές — γι’ αυτό δε χρειάζεται το παραπάνω κουμπί."
        StringKey.COUNTERS_DATA_CLASS_PERSISTED ->
            "ksafe.mutableStateOf — data class (αποθηκευμένο, κρυπτογραφημένο)"
        StringKey.COUNTERS_BIOMETRIC_COUNT -> "BIO: {0}"
        StringKey.COUNTERS_HARDWARE_SECURED_VAULT ->
            "Θυρίδα με ασφάλεια υλικού (StrongBox / Secure Enclave)"
        StringKey.COUNTERS_VAULT_TOKEN -> "Token θυρίδας"
        StringKey.COUNTERS_NO_TOKEN_STORED -> "Δεν έχει αποθηκευτεί token"
        StringKey.COUNTERS_GENERATE_TOKEN -> "Δημιουργία token"
        StringKey.COUNTERS_CLEAR_VAULT -> "Εκκαθάριση θυρίδας"
        StringKey.COUNTERS_BIO_AUTH_WINDOW ->
            "Το παράθυρο βιομετρικής ταυτοποίησης είναι ανοικτό — χωρίς ερώτημα για {0} δ."
        StringKey.COUNTERS_KEY_ROTATION -> "Περιστροφή κλειδιών"
        StringKey.COUNTERS_KEY_ROTATION_DESCRIPTION ->
            "Κρυπτογραφεί ξανά κάθε κρυπτογραφημένη εγγραφή με νέα γενιά κλειδιού και " +
                "απορρίπτει τα παλαιά κλειδιά. Αφορά όλη την αποθήκη· οι ίδιες οι τιμές δεν " +
                "αλλάζουν ποτέ, οπότε οι παραπάνω μετρητές παραμένουν ακριβώς ίδιοι."
        StringKey.COUNTERS_ROTATING -> "Γίνεται περιστροφή..."
        StringKey.COUNTERS_ROTATE_KEYS -> "Περιστροφή κλειδιών"
        StringKey.COUNTERS_LOCK_STATE_POLICY_TEST ->
            "Δοκιμή πολιτικής κατάστασης κλειδώματος"
        StringKey.COUNTERS_LOCK_TEST_INSTRUCTIONS ->
            "Εκτελέστε από την Αρχική οθόνη, όχι από το Xcode, για ακριβή αποτελέσματα"
        StringKey.COUNTERS_LOCK_DEVICE_COUNTDOWN ->
            "Κλειδώστε τώρα τη συσκευή σας! Ανάγνωση σε {0} δ..."
        StringKey.COUNTERS_ATTEMPTING_ENCRYPTED_READ ->
            "Δοκιμή κρυπτογραφημένης ανάγνωσης..."
        StringKey.COUNTERS_TEST_LOCK_DEFAULT -> "Απλό"
        StringKey.COUNTERS_TEST_LOCK_HARDWARE -> "Isolated"
        StringKey.COUNTERS_LOCK_TEST_RESULT -> "Αποτέλεσμα δοκιμής κλειδώματος"
        StringKey.COUNTERS_CLEAR_ALL_TITLE -> "Εκκαθάριση όλων των τιμών;"
        StringKey.COUNTERS_CLEAR_ALL_DESCRIPTION ->
            "Θα διαγραφούν όλα τα αποθηκευμένα κλειδιά και οι μετρητές θα επιστρέψουν στις " +
                "προεπιλογές τους."
        StringKey.COUNTERS_BIOMETRICS_UNAVAILABLE ->
            "Τα βιομετρικά δεν είναι διαθέσιμα — δεν έχει ρυθμιστεί δακτυλικό αποτύπωμα, " +
                "αναγνώριση προσώπου ή κλείδωμα συσκευής"
        StringKey.COUNTERS_AUTHENTICATE_TO_SAVE ->
            "Ταυτοποιηθείτε για αποθήκευση"
        StringKey.COUNTERS_AUTHENTICATION_FAILED ->
            "Η ταυτοποίηση απέτυχε ή ακυρώθηκε"
        StringKey.COUNTERS_ROTATION_RESULT ->
            "Επανακρυπτογραφήθηκαν: {0}\nΠαραλείφθηκαν: {1}\nΑπέτυχαν: {2}\n" +
                "Η γενιά κλειδιού είναι τώρα {3}"
        StringKey.COUNTERS_ROTATION_RESULT_WITH_SKIPPED ->
            "Επανακρυπτογραφήθηκαν: {0}\nΠαραλείφθηκαν: {1}\nΑπέτυχαν: {2}\n" +
                "Η γενιά κλειδιού είναι τώρα {3}\n\nΟι εγγραφές που παραλείφθηκαν γράφονταν " +
                "εκείνη τη στιγμή ή είναι αυστηρές εγγραφές σε κλειδωμένη συσκευή. Παραμένουν " +
                "αναγνώσιμες με το προηγούμενο κλειδί τους — εκτελέστε ξανά την περιστροφή για " +
                "να συμπεριληφθούν."
        StringKey.COUNTERS_ROTATION_FAILED -> "Η περιστροφή απέτυχε:\n{0}"
        StringKey.COUNTERS_LOCK_SETUP_FAILED ->
            "ΑΠΟΤΥΧΙΑ ΡΥΘΜΙΣΗΣ.\n\nΔεν ήταν δυνατή η αποθήκευση της δοκιμαστικής τιμής: {0}"
        StringKey.COUNTERS_LOCK_READ_SUCCEEDED ->
            "Η ΑΝΑΓΝΩΣΗ ΠΕΤΥΧΕ ενώ η συσκευή ήταν κλειδωμένη.\n\nΗ κρυπτογραφημένη ανάγνωση " +
                "ΔΕΝ αποκλείστηκε.\n\nΣημείωση: το requireUnlockedDevice εφαρμόζεται μόνο σε " +
                "Android και Apple. Το JVM Desktop δεν έχει έννοια κλειδώματος συσκευής για " +
                "σύνδεση με κλειδί, ενώ οι browsers δεν έχουν ούτε αυτή ούτε σύγχρονη " +
                "αποκρυπτογράφηση. Γι’ αυτό το KSafe αγνοεί εκεί τη σημαία αντί να αφήσει την " +
                "τιμή μόνο για εγγραφή. Σε Web και Desktop αυτή η δοκιμή πάντα διαβάζει την " +
                "τιμή — είναι η τεκμηριωμένη συμπεριφορά, όχι αποτυχία."
        StringKey.COUNTERS_LOCK_READ_SUCCEEDED_WITH_DEBUGGER ->
            "Η ΑΝΑΓΝΩΣΗ ΠΕΤΥΧΕ ενώ η συσκευή ήταν κλειδωμένη.\n\nΗ κρυπτογραφημένη ανάγνωση " +
                "ΔΕΝ αποκλείστηκε.\n\nΣημείωση: το requireUnlockedDevice εφαρμόζεται μόνο σε " +
                "Android και Apple. Το JVM Desktop δεν έχει έννοια κλειδώματος συσκευής για " +
                "σύνδεση με κλειδί, ενώ οι browsers δεν έχουν ούτε αυτή ούτε σύγχρονη " +
                "αποκρυπτογράφηση. Γι’ αυτό το KSafe αγνοεί εκεί τη σημαία αντί να αφήσει την " +
                "τιμή μόνο για εγγραφή. Σε Web και Desktop αυτή η δοκιμή πάντα διαβάζει την " +
                "τιμή — είναι η τεκμηριωμένη συμπεριφορά, όχι αποτυχία.\n\nΕντοπίστηκε debugger " +
                "ή debug build. Ο debugger του Xcode εμποδίζει την ενεργοποίηση της προστασίας " +
                "δεδομένων του iOS — το Keychain μένει ξεκλείδωτο όσο είναι συνδεδεμένος.\n\n" +
                "Για ακριβή δοκιμή:\n1. Κάντε build και εκτελέστε την εφαρμογή στη συσκευή\n" +
                "2. Πατήστε Stop στο Xcode\n3. Ανοίξτε την εφαρμογή από την Αρχική οθόνη\n" +
                "4. Εκτελέστε ξανά αυτή τη δοκιμή"
        StringKey.COUNTERS_LOCK_UNEXPECTED_RESULT ->
            "ΜΗ ΑΝΑΜΕΝΟΜΕΝΟ ΑΠΟΤΕΛΕΣΜΑ.\n\nΗ ανάγνωση επέστρεψε: «{0}»"
        StringKey.COUNTERS_LOCK_READ_BLOCKED ->
            "Η ΑΝΑΓΝΩΣΗ ΑΠΟΚΛΕΙΣΤΗΚΕ — η λειτουργία δουλεύει!\n\nΗ κρυπτογραφημένη ανάγνωση " +
                "απέτυχε όσο η συσκευή ήταν κλειδωμένη:\n{0}\n\nΑυτό επιβεβαιώνει ότι το " +
                "requireUnlockedDevice λειτουργεί σωστά."

        StringKey.FLOWS_TITLE -> "Flow Delegates (1.8.0+)"
        StringKey.FLOWS_SUBTITLE -> "MutableStateFlow + κρυπτογράφηση + αποθήκευση"
        StringKey.FLOWS_MUTABLE_STATE_FLOW_SECTION ->
            "asMutableStateFlow — άμεση αντικατάσταση MutableStateFlow"
        StringKey.FLOWS_MOVIES_STATE -> "MoviesListState (αποθηκευμένο + κρυπτογραφημένο)"
        StringKey.FLOWS_LOADING_MOVIES -> "Φόρτωση ταινιών..."
        StringKey.FLOWS_ERROR -> "Σφάλμα: {0}"
        StringKey.FLOWS_NO_MOVIES_LOADED -> "Δεν φορτώθηκαν ταινίες"
        StringKey.FLOWS_LOAD_MOVIES -> "Φόρτωση ταινιών"
        StringKey.FLOWS_AS_FLOW_SECTION -> "asFlow και αμφίδρομη σύνδεση"
        StringKey.FLOWS_USERNAME -> "Όνομα χρήστη (asMutableStateFlow)"
        StringKey.FLOWS_TOGGLE_VALUE -> "Εναλλαγή τιμής (asFlow)"
        StringKey.FLOWS_DERIVED_VALUE -> "Παράγωγο: {0}"
        StringKey.FLOWS_ON_MODE -> "Ενεργή λειτουργία"
        StringKey.FLOWS_OFF_MODE -> "Ανενεργή λειτουργία"
        StringKey.FLOWS_DEFAULT_USERNAME -> "Επισκέπτης"
        StringKey.FLOWS_WRITABLE_FLOW_SECTION -> "asWritableFlow — ψυχρή, εγγράψιμη, χωρίς scope"
        StringKey.FLOWS_FAVOURITE_MOVIE -> "Αγαπημένη ταινία"
        StringKey.FLOWS_NO_FAVOURITE_MOVIE -> "Δεν έχει επιλεγεί ακόμα"
        StringKey.FLOWS_FAVOURITE_HINT -> "Πάτησε μια ταινία παραπάνω για αποθήκευση με .set() — χωρίς scope, χωρίς getter .value."
        StringKey.FLOWS_SCOPE_SYNC_SECTION ->
            "mutableStateOf(scope) — συγχρονισμός μεταξύ οθονών"
        StringKey.FLOWS_ISOLATED_NO_SCOPE -> "απομονωμένο (χωρίς scope)"
        StringKey.FLOWS_SYNCED_WITH_SCOPE -> "συγχρονισμένο (με scope)"
        StringKey.FLOWS_INCREMENT_FROM_SCREEN -> "+1 από αυτή την οθόνη"
        StringKey.FLOWS_REFRESH_ISOLATED -> "Ανανέωση απομονωμένης τιμής"
        StringKey.FLOWS_SYNC_EXPLANATION ->
            "Η τιμή με scope ενημερώνεται σε πραγματικό χρόνο· η απομονωμένη τιμή χρειάζεται " +
                "χειροκίνητη ανανέωση."
        StringKey.FLOWS_CLEAR_ALL -> "Εκκαθάριση όλων των παραδειγμάτων ροής"

        StringKey.CUSTOM_JSON_TITLE -> "Προσαρμοσμένη σειριοποίηση JSON"
        StringKey.CUSTOM_JSON_SUBTITLE ->
            "Αποθηκεύστε data classes με πεδία @Contextual που χρειάζονται προσαρμοσμένους " +
                "serializers."
        StringKey.CUSTOM_JSON_DEFINE_SERIALIZERS ->
            "1. Ορισμός προσαρμοσμένων serializers"
        StringKey.CUSTOM_JSON_REGISTER_SERIALIZERS -> "2. Καταχώριση serializers"
        StringKey.CUSTOM_JSON_PASS_THROUGH_CONFIG -> "3. Πέρασμα μέσω του KSafeConfig"
        StringKey.CUSTOM_JSON_USE_CONTEXTUAL_FIELDS -> "4. Χρήση πεδίων @Contextual"
        StringKey.CUSTOM_JSON_TRY_IT -> "Δοκιμή"
        StringKey.CUSTOM_JSON_NAME -> "Όνομα"
        StringKey.CUSTOM_JSON_SAVED_COUNT ->
            "Αποθηκεύτηκε {0} φορά/φορές — το χρώμα αλλάζει σε κάθε αποθήκευση"
        StringKey.CUSTOM_JSON_STORED_VALUES ->
            "Αποθηκευμένες τιμές (διατηρούνται μετά την επανεκκίνηση)"
        StringKey.CUSTOM_JSON_ENCRYPTED -> "Κρυπτογραφημένο"
        StringKey.CUSTOM_JSON_PLAIN_TEXT -> "Απλό κείμενο"
        StringKey.CUSTOM_JSON_PROFILE_NAME -> "όνομα: {0}"
        StringKey.CUSTOM_JSON_PROFILE_CREATED_AT -> "δημιουργήθηκε: {0}"
        StringKey.CUSTOM_JSON_PROFILE_FAVORITE_COLOR -> "αγαπημένο χρώμα: {0}"

        StringKey.SECURITY_TITLE -> "Κατάσταση ασφάλειας"
        StringKey.SECURITY_SUBTITLE ->
            "Επίδειξη πολιτικής ασφάλειας και φύλαξης κλειδιών του KSafe"
        StringKey.SECURITY_SECURE_ENVIRONMENT -> "Ασφαλές περιβάλλον"
        StringKey.SECURITY_WARNINGS_DETECTED -> "Εντοπίστηκαν {0} προειδοποιήσεις"
        StringKey.SECURITY_KEY_PROTECTION -> "Προστασία κλειδιών"
        StringKey.SECURITY_INTENDED -> "Επιθυμητή"
        StringKey.SECURITY_EFFECTIVE -> "Πραγματική"
        StringKey.SECURITY_CUSTODY -> "Φύλαξη"
        StringKey.SECURITY_NOTES -> "Σημειώσεις"
        StringKey.SECURITY_DEGRADED_FROM -> "Υποβαθμίστηκε από {0}"
        StringKey.SECURITY_REFRESH_STATUS -> "Ανανέωση κατάστασης ασφάλειας"
        StringKey.SECURITY_LIVE_INFO ->
            "Οι πληροφορίες προστασίας είναι ζωντανές και μπορεί να αλλάξουν μετά από " +
                "περιστροφή ή ανάκτηση κλειδιού."
        StringKey.SECURITY_CHECKS -> "Έλεγχοι ασφάλειας"
        StringKey.SECURITY_WARNING -> "ΠΡΟΕΙΔΟΠΟΙΗΣΗ"
        StringKey.SECURITY_OK -> "OK"
        StringKey.SECURITY_CURRENT_POLICY -> "Τρέχουσα πολιτική: {0}"
        StringKey.SECURITY_POLICY_DESCRIPTION ->
            "Η εφαρμογή επίδειξης αναφέρει προβλήματα ασφάλειας χωρίς να αποκλείει λειτουργίες. " +
                "Οι εφαρμογές παραγωγής που χειρίζονται ευαίσθητα δεδομένα πρέπει να επιλέγουν " +
                "πολιτική βάσει του μοντέλου απειλών τους."
        StringKey.SECURITY_ROOTED_TITLE -> "Εντοπισμός root/jailbreak"
        StringKey.SECURITY_ROOTED_DESCRIPTION ->
            "Η συσκευή είναι rooted ή jailbroken, κάτι που μπορεί να αποδυναμώσει την απομόνωση " +
                "της εφαρμογής."
        StringKey.SECURITY_DEBUGGER_TITLE -> "Εντοπισμός debugger"
        StringKey.SECURITY_DEBUGGER_DESCRIPTION ->
            "Ένας debugger μπορεί να επιθεωρήσει τη μνήμη εκτέλεσης, μαζί με τις τιμές όταν " +
                "είναι αποκρυπτογραφημένες."
        StringKey.SECURITY_DEBUG_BUILD_TITLE -> "Εντοπισμός debug build"
        StringKey.SECURITY_DEBUG_BUILD_DESCRIPTION ->
            "Τα debug builds μπορεί να εκθέτουν περισσότερες πληροφορίες και να χρησιμοποιούν " +
                "ασθενέστερους λειτουργικούς ελέγχους."
        StringKey.SECURITY_EMULATOR_TITLE -> "Εντοπισμός emulator"
        StringKey.SECURITY_EMULATOR_DESCRIPTION ->
            "Οι emulators και οι simulators δεν προσφέρουν τις ίδιες εγγυήσεις ασφάλειας " +
                "υλικού με τις φυσικές συσκευές."

        StringKey.ABOUT_TITLE -> "Σχετικά"
        StringKey.ABOUT_KSAFE_VERSION -> "KSafe {0}"
        StringKey.ABOUT_DESCRIPTION ->
            "Μια Kotlin Multiplatform βιβλιοθήκη για ασφαλή, κρυπτογραφημένη αποθήκευση που " +
                "υποστηρίζεται από το Keystore ή το Keychain της πλατφόρμας."
        StringKey.ABOUT_DEVELOPER -> "Προγραμματιστής"
        StringKey.ABOUT_PERSONAL_WEBSITE -> "Προσωπική ιστοσελίδα"
        StringKey.ABOUT_GITHUB_PROFILE -> "Προφίλ GitHub"
        StringKey.ABOUT_PROJECTS -> "Έργα"
        StringKey.ABOUT_KSAFE_LIBRARY -> "Βιβλιοθήκη KSafe"
        StringKey.ABOUT_OPEN_KSAFE_REPOSITORY -> "Άνοιγμα repository του KSafe"
        StringKey.ABOUT_KSAFE_DEMO_APPLICATION -> "Εφαρμογή KSafeDemo"
        StringKey.ABOUT_OPEN_KSAFE_DEMO_REPOSITORY ->
            "Άνοιγμα repository του KSafeDemo"

        StringKey.UNMATCHED -> ""
    }
}
