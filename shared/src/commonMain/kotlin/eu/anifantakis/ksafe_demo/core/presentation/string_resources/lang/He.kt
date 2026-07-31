package eu.anifantakis.ksafe_demo.core.presentation.string_resources.lang

import eu.anifantakis.ksafe_demo.core.presentation.string_resources.LanguageStrings
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey

internal class He : LanguageStrings {
    override fun getString(key: StringKey): String = when (key) {
        StringKey.COMMON_PROCESSING -> "מעבד"
        StringKey.COMMON_BACK -> "חזרה"
        StringKey.COMMON_OPEN_APPLICATION_MENU -> "פתיחת תפריט היישום"
        StringKey.COMMON_PREFERENCES -> "העדפות"
        StringKey.COMMON_ABOUT -> "אודות"
        StringKey.COMMON_CLEAR -> "ניקוי"
        StringKey.COMMON_CANCEL -> "ביטול"
        StringKey.COMMON_OK -> "אישור"
        StringKey.COMMON_SAVE -> "שמירה"
        StringKey.COMMON_EMAIL -> "דוא״ל"
        StringKey.APP_PRESENTING_KSAFE -> "מציגים את KSafe {0}"
        StringKey.APP_STARTUP_LOADING -> "טעינת KSafe…"
        StringKey.APP_STARTUP_FAILED -> "לא ניתן להפעיל את KSafeDemo."
        StringKey.APP_STARTUP_RETRY -> "ניסיון חוזר"
        StringKey.NAV_COUNTERS -> "מונים"
        StringKey.NAV_FLOWS -> "זרימות"
        StringKey.NAV_CUSTOM_JSON -> "JSON מותאם אישית"
        StringKey.NAV_SECURITY -> "אבטחה"

        StringKey.PREFERENCES_APPEARANCE -> "מראה"
        StringKey.PREFERENCES_APPEARANCE_DESCRIPTION ->
            "בחרו כיצד KSafeDemo קובע את ערכת הצבעים. ההעדפה נשמרת מקומית ומוחלת מיד " +
                "בכל פלטפורמה."
        StringKey.PREFERENCES_THEME_DAY -> "יום"
        StringKey.PREFERENCES_THEME_DAY_DESCRIPTION ->
            "שימוש קבוע בערכת הצבעים הבהירה"
        StringKey.PREFERENCES_THEME_NIGHT -> "לילה"
        StringKey.PREFERENCES_THEME_NIGHT_DESCRIPTION ->
            "שימוש קבוע בערכת הצבעים הכהה"
        StringKey.PREFERENCES_THEME_SYSTEM -> "מערכת"
        StringKey.PREFERENCES_THEME_SYSTEM_DESCRIPTION ->
            "התאמה למראה של מערכת ההפעלה"
        StringKey.PREFERENCES_LANGUAGE -> "שפה"
        StringKey.PREFERENCES_LANGUAGE_DESCRIPTION ->
            "בחרו את השפה שבה היישום ישתמש."
        StringKey.PREFERENCES_SELECT_LANGUAGE -> "בחירת שפה"

        StringKey.COUNTERS_MUTABLE_STATE_NO_PERSISTENCE ->
            "mutableStateOf (ללא התמדה)"
        StringKey.COUNTERS_COUNTER_1 -> "מונה 1"
        StringKey.COUNTERS_PLAIN_STATE_RESETS ->
            "מצב רגיל — מתאפס בהפעלה מחדש"
        StringKey.COUNTERS_KSAFE_MUTABLE_STATE_PERSISTED ->
            "ksafe.mutableStateOf (מתמיד)"
        StringKey.COUNTERS_COUNTER_2 -> "מונה 2"
        StringKey.COUNTERS_ENCRYPTED_OBSERVED_ON_FLOWS ->
            "מוצפן — נצפה בכרטיסיית זרימות"
        StringKey.COUNTERS_COUNTER_3 -> "מונה 3"
        StringKey.COUNTERS_UNENCRYPTED -> "לא מוצפן"
        StringKey.COUNTERS_COUNTER_2_SYNC_DESCRIPTION ->
            "מונה 2 נצפה גם בכרטיסיית זרימות. לחצו כאן על „+” ולאחר מכן עברו לזרימות — " +
                "הערך המסונכרן מתעדכן בזמן אמת."
        StringKey.COUNTERS_COUNTER_2_REFRESH_DESCRIPTION ->
            "אם כרטיסיית זרימות כתבה למונה 2, לחצו על רענון כדי לראות את הערך האחרון " +
                "(ללא scope נדרש רענון ידני)."
        StringKey.COUNTERS_REFRESH_COUNTER_2 -> "רענון מונה 2"
        StringKey.COUNTERS_MUTABLE_STATE_FLOW_PERSISTED ->
            "ksafe.asMutableStateFlow (מתמיד, תגובתי)"
        StringKey.COUNTERS_COUNTER_2B -> "מונה 2b"
        StringKey.COUNTERS_COUNTER_2C -> "מונה 2c"
        StringKey.COUNTERS_FLOW_NO_REFRESH ->
            "MutableStateFlow — אין צורך ברענון"
        StringKey.COUNTERS_FLOW_EXPLANATION ->
            "אותו אחסון, צורה שונה: MutableStateFlow במקום Compose State. הוא מקבל scope, " +
                "נרשם למפתח שלו ומזהה בעצמו כתיבות חיצוניות — לכן אינו זקוק לכפתור שלמעלה."
        StringKey.COUNTERS_DATA_CLASS_PERSISTED ->
            "ksafe.mutableStateOf — מחלקת נתונים (מתמידה, מוצפנת)"
        StringKey.COUNTERS_BIOMETRIC_COUNT -> "ביומטרי: {0}"
        StringKey.COUNTERS_HARDWARE_SECURED_VAULT ->
            "כספת מאובטחת בחומרה (StrongBox / Secure Enclave)"
        StringKey.COUNTERS_VAULT_TOKEN -> "אסימון הכספת"
        StringKey.COUNTERS_NO_TOKEN_STORED -> "לא נשמר אסימון"
        StringKey.COUNTERS_GENERATE_TOKEN -> "יצירת אסימון"
        StringKey.COUNTERS_CLEAR_VAULT -> "ניקוי הכספת"
        StringKey.COUNTERS_BIO_AUTH_WINDOW ->
            "חלון האימות הביומטרי פתוח — לא תופיע בקשה במשך {0} שניות"
        StringKey.COUNTERS_KEY_ROTATION -> "החלפת מפתחות"
        StringKey.COUNTERS_KEY_ROTATION_DESCRIPTION ->
            "מצפין מחדש כל רשומה מוצפנת באמצעות דור מפתחות חדש ומסיר את המפתחות שהוחלפו. " +
                "הפעולה חלה על כל האחסון; הערכים עצמם לעולם אינם משתנים, ולכן המונים שלמעלה " +
                "נשארים בדיוק כפי שהיו."
        StringKey.COUNTERS_ROTATING -> "מחליף מפתחות..."
        StringKey.COUNTERS_ROTATE_KEYS -> "החלפת מפתחות"
        StringKey.COUNTERS_LOCK_STATE_POLICY_TEST ->
            "בדיקת מדיניות מצב נעילה"
        StringKey.COUNTERS_LOCK_TEST_INSTRUCTIONS ->
            "לתוצאות מדויקות, הפעילו ממסך הבית ולא מתוך Xcode"
        StringKey.COUNTERS_LOCK_DEVICE_COUNTDOWN ->
            "נעלו את המכשיר עכשיו! הקריאה תתבצע בעוד {0} שניות..."
        StringKey.COUNTERS_ATTEMPTING_ENCRYPTED_READ ->
            "מנסה לבצע קריאה מוצפנת..."
        StringKey.COUNTERS_TEST_LOCK_DEFAULT -> "רגיל"
        StringKey.COUNTERS_TEST_LOCK_HARDWARE -> "Isolated"
        StringKey.COUNTERS_LOCK_TEST_RESULT -> "תוצאת בדיקת הנעילה"
        StringKey.COUNTERS_CLEAR_ALL_TITLE -> "לנקות את כל הערכים?"
        StringKey.COUNTERS_CLEAR_ALL_DESCRIPTION ->
            "כל המפתחות השמורים יימחקו והמונים יחזרו לערכי ברירת המחדל."
        StringKey.COUNTERS_BIOMETRICS_UNAVAILABLE ->
            "אימות ביומטרי אינו זמין — לא הוגדרו טביעת אצבע, זיהוי פנים או נעילת מכשיר"
        StringKey.COUNTERS_AUTHENTICATE_TO_SAVE -> "יש לבצע אימות כדי לשמור"
        StringKey.COUNTERS_AUTHENTICATION_FAILED ->
            "האימות נכשל או בוטל"
        StringKey.COUNTERS_ROTATION_RESULT ->
            "הוצפנו מחדש: {0}\nדולגו: {1}\nנכשלו: {2}\nדור המפתח הוא כעת {3}"
        StringKey.COUNTERS_ROTATION_RESULT_WITH_SKIPPED ->
            "הוצפנו מחדש: {0}\nדולגו: {1}\nנכשלו: {2}\nדור המפתח הוא כעת {3}\n\n" +
                "הרשומות שדולגו היו בתהליך כתיבה, או שהן רשומות מחמירות במכשיר נעול. " +
                "הן נשארות קריאות באמצעות המפתח הקודם — הפעילו שוב את ההחלפה כדי לכלול אותן."
        StringKey.COUNTERS_ROTATION_FAILED -> "החלפת המפתחות נכשלה:\n{0}"
        StringKey.COUNTERS_LOCK_SETUP_FAILED ->
            "ההגדרה נכשלה.\n\nלא ניתן היה לשמור את ערך הבדיקה: {0}"
        StringKey.COUNTERS_LOCK_READ_SUCCEEDED ->
            "הקריאה הצליחה בזמן שהמכשיר היה נעול.\n\nהקריאה המוצפנת לא נחסמה.\n\n" +
                "הערה: requireUnlockedDevice נאכף רק ב-Android ובפלטפורמות Apple. ב-JVM " +
                "Desktop אין מושג של נעילת מכשיר שאליו ניתן לקשור מפתח, ולדפדפנים אין גם " +
                "פענוח סינכרוני. לכן KSafe מתעלם שם מהדגל במקום להשאיר את הערך לכתיבה בלבד. " +
                "ב-Web וב-Desktop הבדיקה תמיד מצליחה לקרוא — זו ההתנהגות המתועדת, לא כשל."
        StringKey.COUNTERS_LOCK_READ_SUCCEEDED_WITH_DEBUGGER ->
            "הקריאה הצליחה בזמן שהמכשיר היה נעול.\n\nהקריאה המוצפנת לא נחסמה.\n\n" +
                "הערה: requireUnlockedDevice נאכף רק ב-Android ובפלטפורמות Apple. ב-JVM " +
                "Desktop אין מושג של נעילת מכשיר שאליו ניתן לקשור מפתח, ולדפדפנים אין גם " +
                "פענוח סינכרוני. לכן KSafe מתעלם שם מהדגל במקום להשאיר את הערך לכתיבה בלבד. " +
                "ב-Web וב-Desktop הבדיקה תמיד מצליחה לקרוא — זו ההתנהגות המתועדת, לא כשל.\n\n" +
                "זוהה debugger או debug build. ה-debugger של Xcode מונע מהגנת הנתונים של iOS " +
                "להיכנס לפעולה — ה-Keychain נשאר פתוח כל עוד ה-debugger מחובר.\n\nלבדיקה מדויקת:\n" +
                "1. בנו והפעילו את היישום במכשיר\n2. לחצו Stop ב-Xcode\n" +
                "3. הפעילו את היישום ממסך הבית\n4. הפעילו שוב את הבדיקה"
        StringKey.COUNTERS_LOCK_UNEXPECTED_RESULT ->
            "תוצאה בלתי צפויה.\n\nהקריאה החזירה: „{0}”"
        StringKey.COUNTERS_LOCK_READ_BLOCKED ->
            "הקריאה נחסמה — התכונה פועלת!\n\nהקריאה המוצפנת נכשלה בזמן שהמכשיר היה " +
                "נעול:\n{0}\n\nהתוצאה מאשרת ש-requireUnlockedDevice פועל כראוי."

        StringKey.FLOWS_TITLE -> "נציגי זרימה (1.8.0+)"
        StringKey.FLOWS_SUBTITLE -> "MutableStateFlow + הצפנה + התמדה"
        StringKey.FLOWS_MUTABLE_STATE_FLOW_SECTION ->
            "asMutableStateFlow — תחליף ישיר ל-MutableStateFlow"
        StringKey.FLOWS_MOVIES_STATE -> "MoviesListState (מתמיד + מוצפן)"
        StringKey.FLOWS_LOADING_MOVIES -> "טוען סרטים..."
        StringKey.FLOWS_ERROR -> "שגיאה: {0}"
        StringKey.FLOWS_NO_MOVIES_LOADED -> "לא נטענו סרטים"
        StringKey.FLOWS_LOAD_MOVIES -> "טעינת סרטים"
        StringKey.FLOWS_AS_FLOW_SECTION -> "asFlow וקישור דו-כיווני"
        StringKey.FLOWS_USERNAME -> "שם משתמש (asMutableStateFlow)"
        StringKey.FLOWS_TOGGLE_VALUE -> "החלפת ערך (asFlow)"
        StringKey.FLOWS_DERIVED_VALUE -> "נגזר: {0}"
        StringKey.FLOWS_ON_MODE -> "מצב פעיל"
        StringKey.FLOWS_OFF_MODE -> "מצב כבוי"
        StringKey.FLOWS_DEFAULT_USERNAME -> "אורח"
        StringKey.FLOWS_SCOPE_SYNC_SECTION ->
            "mutableStateOf(scope) — סנכרון בין מסכים"
        StringKey.FLOWS_ISOLATED_NO_SCOPE -> "מבודד (ללא scope)"
        StringKey.FLOWS_SYNCED_WITH_SCOPE -> "מסונכרן (עם scope)"
        StringKey.FLOWS_INCREMENT_FROM_SCREEN -> "+1 מהמסך הזה"
        StringKey.FLOWS_REFRESH_ISOLATED -> "רענון הערך המבודד"
        StringKey.FLOWS_SYNC_EXPLANATION ->
            "הערך עם scope מתעדכן בזמן אמת; הערך המבודד דורש רענון ידני."
        StringKey.FLOWS_CLEAR_ALL -> "ניקוי כל הדגמות הזרימה"

        StringKey.CUSTOM_JSON_TITLE -> "סריאליזציית JSON מותאמת אישית"
        StringKey.CUSTOM_JSON_SUBTITLE ->
            "שמירת מחלקות נתונים עם שדות @Contextual שזקוקים לסריאלייזרים מותאמים."
        StringKey.CUSTOM_JSON_DEFINE_SERIALIZERS ->
            "1. הגדרת סריאלייזרים מותאמים"
        StringKey.CUSTOM_JSON_REGISTER_SERIALIZERS -> "2. רישום הסריאלייזרים"
        StringKey.CUSTOM_JSON_PASS_THROUGH_CONFIG -> "3. העברתם דרך KSafeConfig"
        StringKey.CUSTOM_JSON_USE_CONTEXTUAL_FIELDS -> "4. שימוש בשדות @Contextual"
        StringKey.CUSTOM_JSON_TRY_IT -> "נסו בעצמכם"
        StringKey.CUSTOM_JSON_NAME -> "שם"
        StringKey.CUSTOM_JSON_SAVED_COUNT ->
            "נשמר {0} פעמים — הצבע מתחלף בכל שמירה"
        StringKey.CUSTOM_JSON_STORED_VALUES ->
            "ערכים שמורים (נשמרים גם לאחר הפעלה מחדש)"
        StringKey.CUSTOM_JSON_ENCRYPTED -> "מוצפן"
        StringKey.CUSTOM_JSON_PLAIN_TEXT -> "טקסט גלוי"
        StringKey.CUSTOM_JSON_PROFILE_NAME -> "שם: {0}"
        StringKey.CUSTOM_JSON_PROFILE_CREATED_AT -> "נוצר בתאריך: {0}"
        StringKey.CUSTOM_JSON_PROFILE_FAVORITE_COLOR -> "צבע מועדף: {0}"

        StringKey.SECURITY_TITLE -> "מצב אבטחה"
        StringKey.SECURITY_SUBTITLE -> "הדגמת מדיניות האבטחה ומשמורת המפתחות של KSafe"
        StringKey.SECURITY_SECURE_ENVIRONMENT -> "סביבה מאובטחת"
        StringKey.SECURITY_WARNINGS_DETECTED -> "זוהו {0} אזהרות"
        StringKey.SECURITY_KEY_PROTECTION -> "הגנת מפתחות"
        StringKey.SECURITY_INTENDED -> "מיועדת"
        StringKey.SECURITY_EFFECTIVE -> "בפועל"
        StringKey.SECURITY_CUSTODY -> "משמורת"
        StringKey.SECURITY_NOTES -> "הערות"
        StringKey.SECURITY_DEGRADED_FROM -> "הורדה מרמה {0}"
        StringKey.SECURITY_REFRESH_STATUS -> "רענון מצב האבטחה"
        StringKey.SECURITY_LIVE_INFO ->
            "מידע ההגנה מתעדכן בזמן אמת ועשוי להשתנות לאחר החלפת מפתח או שחזור."
        StringKey.SECURITY_CHECKS -> "בדיקות אבטחה"
        StringKey.SECURITY_WARNING -> "אזהרה"
        StringKey.SECURITY_OK -> "תקין"
        StringKey.SECURITY_CURRENT_POLICY -> "מדיניות נוכחית: {0}"
        StringKey.SECURITY_POLICY_DESCRIPTION ->
            "ההדגמה מדווחת על בעיות אבטחה בלי לחסום פונקציונליות. יישומי ייצור המטפלים " +
                "במידע רגיש צריכים לבחור מדיניות בהתאם למודל האיומים שלהם."
        StringKey.SECURITY_ROOTED_TITLE -> "זיהוי Root/Jailbreak"
        StringKey.SECURITY_ROOTED_DESCRIPTION ->
            "המכשיר עבר root או jailbreak, דבר שעלול להחליש את בידוד היישום."
        StringKey.SECURITY_DEBUGGER_TITLE -> "זיהוי debugger"
        StringKey.SECURITY_DEBUGGER_DESCRIPTION ->
            "Debugger יכול לבדוק את זיכרון הריצה, כולל ערכים בזמן שהם מפוענחים."
        StringKey.SECURITY_DEBUG_BUILD_TITLE -> "זיהוי גרסת debug"
        StringKey.SECURITY_DEBUG_BUILD_DESCRIPTION ->
            "גרסאות debug עשויות לחשוף מידע רב יותר ולהשתמש בבקרות תפעוליות חלשות יותר."
        StringKey.SECURITY_EMULATOR_TITLE -> "זיהוי אמולטור"
        StringKey.SECURITY_EMULATOR_DESCRIPTION ->
            "אמולטורים וסימולטורים אינם מספקים את אותן הבטחות אבטחה מבוססות חומרה כמו מכשירים."

        StringKey.ABOUT_TITLE -> "אודות"
        StringKey.ABOUT_KSAFE_VERSION -> "KSafe {0}"
        StringKey.ABOUT_DESCRIPTION ->
            "ספריית Kotlin Multiplatform להתמדה מאובטחת ומוצפנת, הנשענת על ה-Keystore או " +
                "ה-Keychain של הפלטפורמה."
        StringKey.ABOUT_DEVELOPER -> "מפתח"
        StringKey.ABOUT_PERSONAL_WEBSITE -> "אתר אישי"
        StringKey.ABOUT_GITHUB_PROFILE -> "פרופיל GitHub"
        StringKey.ABOUT_PROJECTS -> "פרויקטים"
        StringKey.ABOUT_KSAFE_LIBRARY -> "ספריית KSafe"
        StringKey.ABOUT_OPEN_KSAFE_REPOSITORY -> "פתיחת מאגר KSafe"
        StringKey.ABOUT_KSAFE_DEMO_APPLICATION -> "יישום KSafeDemo"
        StringKey.ABOUT_OPEN_KSAFE_DEMO_REPOSITORY -> "פתיחת מאגר KSafeDemo"

        StringKey.UNMATCHED -> ""
    }
}
