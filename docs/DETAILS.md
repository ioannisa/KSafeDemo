# KSafe Demo

> This is the detailed implementation and architecture guide for the demo application.
> For the KSafe-focused project overview, start with the repository [README](../README.md).

A comprehensive Kotlin Multiplatform demo application showcasing [KSafe](https://github.com/ioannisa/ksafe) - a secure encrypted storage library with biometric authentication, runtime security detection, device lock-state protection, persisted app preferences, and a live multilingual Compose UI.

**Platforms:** Android, iOS, macOS (native), Desktop (JVM), Browser (WASM/JS)

> The demo runs as a native binary on every supported KSafe target. macOS specifically has *two* paths in this demo — the JVM/Desktop binary (Compose Desktop, Skia + Swing) and the native `macosArm64`/`macosX64` binary (Compose Multiplatform native, Skia + AppKit). Both exercise KSafe end-to-end; the native binary additionally validates KSafe's `appleMain` Keychain + CryptoKit code path in a real Compose UI.

---

## Screenshots

| Counters Screen | Flows Screen | Custom JSON Screen | Security Screen | Preferences Bottom Sheet |
|:--------------:|:------------:|:------------------:|:---------------:|:------------------:|
| <img width="270" alt="image" src="https://github.com/user-attachments/assets/fbf461b5-b2c6-4b2a-9de4-1443a2aa3a84" /> | <img width="270" alt="image" src="https://github.com/user-attachments/assets/f05fed91-1df4-4810-a684-6b2258535700" /> | <img width="270" alt="image" src="https://github.com/user-attachments/assets/d7b3abcd-6f6e-4bad-9f2e-ef78f70aea6e" /> | <img width="270" alt="image" src="https://github.com/user-attachments/assets/af169904-64cb-4cc9-aeef-71668a269825" /> | <img width="270" alt="image" src="https://github.com/user-attachments/assets/1ba21590-9522-4dfc-a19a-136c535967cb" /> |

---

## What This Demo Shows

This demo application serves as a practical guide to understanding and implementing KSafe in your own projects. It demonstrates:

### 1. Encrypted Persistent Storage
- **Compose State Integration**: Using `ksafe.mutableStateOf()` for reactive, encrypted persistence (best for ViewModel-owned state)
- **Composable-body Persistent State**: `ksafe.rememberKSafeState()` — `rememberSaveable`-style ergonomics that survive app restarts, not just configuration changes (used in `App.kt` to persist the bottom-tab selection across cold launches with one line)
- **Property Delegation**: Using `by ksafe()` for non-Compose encrypted properties
- **Encryption Options**: Both encrypted (default) and unencrypted storage modes
- **Complex Data Types**: Storing `@Serializable` data classes with automatic serialization

### 2. Flow Delegates (New in 1.8.0)
- **`asStateFlow()`**: Read-only hot StateFlow delegate — key derived from property name
- **`asMutableStateFlow()`**: Read/write MutableStateFlow — drop-in replacement for the standard `MutableStateFlow` pattern, with automatic persistence
- **`mutableStateOf(scope=)`**: Existing Compose state enhanced with flow observation for cross-screen reactivity
- **Data class state**: `_settings` / `settings` pattern using `asMutableStateFlow()` with `.update{}`

### 3. Biometric Authentication
- **Cross-Platform Support**: Face ID, Touch ID, and Fingerprint authentication
- **Authorization Duration**: Caching authentication for a specified time period with visible countdown
- **Scoped Authentication**: Binding auth validity to ViewModel lifecycle

### 4. Security Policy (New in 1.4.0)
- **Runtime Security Detection**: Root/Jailbreak, Debugger, Debug Build, Emulator
- **Configurable Actions**: IGNORE, WARN, or BLOCK for each security check
- **Security Callbacks**: Custom handling when violations are detected

### 5. Custom JSON Serialization (New in 1.7.1)
- **@Contextual Types**: Storing data classes with third-party types you don't own (e.g., `UUID`, `Instant`)
- **Custom SerializersModule**: Registering custom serializers once at the KSafe instance level
- **Both Modes**: Works with encrypted and plain-text storage
- **Code Snippets**: The screen itself displays the setup code for reference

### 6. Persistent Appearance Preference
- **Day / Night / System**: Switch the entire shared Compose UI immediately
- **System-aware**: `System` follows the operating-system dark appearance on every target
- **KSafe Plain preference**: The non-sensitive setting lives in a dedicated KSafe preferences
  store and survives cold launches
- **Theme-safe components**: The `App*` façade keeps the demo's original light appearance and
  provides equivalent dark semantic colors

### 7. Multilingual UI and RTL
- **Six languages**: English, German, Italian, Hebrew, French, and Greek
- **Live switching**: Changing the language recomposes the complete shared UI without restarting
- **Persisted selection**: The ISO language code is stored as a non-sensitive KSafe Plain
  preference; first launch uses the supported system language and otherwise falls back to English
- **Compiler-checked translations**: Every language provider uses an exhaustive `when(StringKey)`
  with no `else`, so a missing translation is a compile error
- **RTL support**: Hebrew drives `LocalLayoutDirection` through `LocalizationProvider`, mirroring
  Material layouts and the auto-mirrored back icon
- **Reusable picker**: Preferences uses the generic `AppDropdown<T>` design-system component,
  with `Language::displayName` supplied by the feature

### 8. WASM/JS Browser Support (New in 1.6.0)
- **Browser localStorage**: Encrypted key-value storage in the browser via WebCrypto AES-256-GCM
- **Async Cache Initialization**: `awaitCacheReady()` gates rendering until WebCrypto decryption completes
- **Same API**: All KSafe features (property delegation, Compose state, StateFlow) work identically in the browser
- **Compose for Web**: Full `mutableStateOf` persistence via `ksafe-compose` WASM target

### 9. Device Lock-State Protection (New in 1.5.0)
- **`requireUnlockedDevice`**: Encrypted data is only accessible when the device is unlocked
- **Interactive Lock Test**: 15-second countdown to lock your device, then verifies encrypted reads are blocked
- **Platform Background Tasks**: iOS uses `beginBackgroundTaskWithExpirationHandler` to keep the test running while the screen is off

### 10. `rememberKSafeState` — composable-local persistent state (New in 2.0.0)
- **`rememberSaveable`-style API for KSafe**: composable-body local state that survives app restarts (not just config changes)
- **Auto-keying from property name**: `var idx by ksafe.rememberKSafeState(0)` stores under the key `"idx"` — explicit `key = "..."` available when you want it
- **Used in `App.kt`**: the bottom-tab route (`var currentRoute by ksafe.rememberKSafeState(AppRoute.Counters)`) persists across cold launches with no ViewModel involved — see [Code Examples → `rememberKSafeState`](#bottom-tab-persistence-with-rememberksafestate-appkt)

### 11. Native macOS Target (New in 2.0.1)
- **Native `macosArm64` / `macosX64` binary**: Compose Multiplatform on AppKit, Skia rendering, KSafe's `appleMain` Keychain + CryptoKit path. Same source as iOS — no UI rewrites
- **Two macOS paths in one demo**: Compose **Desktop** on JVM (`./gradlew :desktopApp:run`) uses KSafe's JDK crypto path; Compose **native macOS** (`./gradlew :macosApp:runDebugExecutableMacosArm64`) uses Keychain + Secure Enclave on Apple Silicon and T2-equipped Macs
- **Validates the lib's macOS target end-to-end**: KSafe ships 118 unit tests for macOS, the demo proves the same code works inside a real Compose UI

---

## App Screens

### Counters Screen

Demonstrates various ways to persist data with KSafe:

| Feature | Description |
|---------|-------------|
| **Counter 1** | Regular Compose `mutableStateOf` - no persistence (resets on restart) |
| **Counter 2** | `ksafe.mutableStateOf` - encrypted persistent state. **Cross-screen demo**: this value is observed on the Flows tab in real-time via `mutableStateOf(scope=)` |
| **Counter 3** | `ksafe.mutableStateOf` with `mode = KSafeWriteMode.Plain` - unencrypted persistent state |
| **AuthInfo** | `@Serializable` data class with encrypted persistence |
| **Biometric Count** | Counter protected by biometric authentication with authorization duration countdown |
| **Lock Test** | Interactive test to verify `requireUnlockedDevice` blocks access when the device is locked |

### Flows Screen (New in 1.8.0)

Demonstrates the new flow delegate APIs introduced in KSafe 1.8.0:

| Feature | Description |
|---------|-------------|
| **asMutableStateFlow (Movies)** | Drop-in replacement for standard `MutableStateFlow`. `MoviesListState` data class with loading/error/movies — `.update{}` persists the entire state atomically |
| **asMutableStateFlow (username)** | Editable persisted `MutableStateFlow<String>` bound directly to the text field |
| **asFlow (toggle mode)** | Read-only cold `Flow<Boolean>` delegate — toggle via `Switch`; a derived `StateFlow<StringKey>` demonstrates flow transformation without hardcoded UI text |
| **Cross-screen sync** | Two cards observe the Counters screen's Counter 2 (key `"count2"`): one **without scope** (frozen at init value) and one **with scope** (updates in real-time when you tap "+" on the Counters tab). Proves the difference between isolated and synced `mutableStateOf` across separate ViewModels |

### Custom JSON Screen

Demonstrates storing data classes that contain `@Contextual` types — types you don't own and can't annotate with `@Serializable`:

| Feature | Description |
|---------|-------------|
| **Two custom types** | `Timestamp` (Long) and `HexColor` (String) — stand-ins for types like `Instant` and `Color` |
| **Two @Contextual fields** | `UserProfile` with `@Contextual val createdAt` and `@Contextual val favoriteColor` |
| **Encrypted + Plain** | Same data stored in both modes to show it works everywhere |
| **Step-by-step code** | The screen itself displays the 4-step setup as inline code snippets |

### Security Screen

Displays real-time security status of the device:

| Check | Description |
|-------|-------------|
| **Root/Jailbreak** | Detects rooted Android devices or jailbroken iOS devices |
| **Debugger** | Detects if a debugger is attached to the process |
| **Debug Build** | Detects if the app is running in debug mode |
| **Emulator** | Detects if running on emulator/simulator |

### Preferences Bottom Sheet

Preferences is not a bottom-navigation destination. It opens as an animated modal bottom sheet
from the top application menu and persists both non-sensitive settings through the dedicated
KSafe preferences store:

| Setting | Options | Behaviour |
|---------|---------|-----------|
| **Appearance** | Day / Night / System | Applies immediately; `System` follows the operating-system appearance |
| **Language** | English / Deutsch / Italiano / עברית / Français / Ελληνικά | Applies immediately, survives cold launches, and mirrors the UI for Hebrew |

The language picker is the generic `AppDropdown<T>` design-system component. It has no dependency
on localization types; the Preferences feature supplies `Language` items and
`Language::displayName`.

### About Screen

The About destination opens from the same top application menu. It displays the KSafe version
reported by the active library instance, developer contact links, and links to the
[KSafe](https://github.com/ioannisa/KSafe) and
[KSafeDemo](https://github.com/ioannisa/KSafeDemo) repositories. The top bar uses the same runtime
version in its `Presenting KSafe {version}` title.

---

## Code Examples from the Demo

### Basic Encrypted State (CountersViewModel.kt)

```kotlin
class CountersViewModel(
    private val ksafe: KSafe,
) : BaseGlobalViewModel() {

    // Regular Compose state - no persistence
    private var count1 by mutableStateOf(1000)

    // KSafe encrypted state - persists across app restarts
    private var count2 by ksafe.mutableStateOf(2000)

    // KSafe unencrypted state with custom key
    private var count3 by ksafe.mutableStateOf(
        defaultValue = 3000,
        key = "counter3Key",
        mode = KSafeWriteMode.Plain
    )

    val state: State<CountersState> = derivedStateOf {
        CountersState(count1 = count1, count2 = count2, count3 = count3)
    }

    fun onAction(intent: CountersIntent) {
        when (intent) {
            CountersIntent.Increment -> increment()
            // ...
        }
    }
}
```

### Bottom-tab persistence with `rememberKSafeState` (App.kt)

The demo's `App.kt` uses **`rememberKSafeState`** to persist the selected bottom-tab across cold app launches. Pre-2.0 this would have required a `MainViewModel` + Koin wiring + `koinViewModel()` injection + flow observation just to remember which tab the user had open. With `rememberKSafeState` it's one line:

```kotlin
@Composable
fun AppContent() {
    val ksafe: KSafe = koinInject()

    // Persisted across app restarts via KSafe — the bottom-tab selection
    // survives process death without any boilerplate. Compare with the
    // pre-2.0 version that used `remember { mutableStateOf(AppRoute.Counters) }`,
    // which only survived recomposition.
    var currentRoute: AppRoute by ksafe.rememberKSafeState(AppRoute.Counters)

    NavigationRoot(
        selectedRoute = currentRoute,
        onRouteSelected = { currentRoute = it },
    )
}
```

**The split between `rememberKSafeState` and `mutableStateOf`:**

| Use case | API |
|---|---|
| ViewModel-owned / cross-screen state, business logic | `var x by ksafe.mutableStateOf(default)` |
| Composable-body local state (tab index, scroll, expanded sections, draft form input) | `var x by ksafe.rememberKSafeState(default)` |

**Auto-keying:** the property name (`currentRoute`) is captured at `provideDelegate` time, so the storage key falls through automatically. Pass `key = "..."` when you want it explicit. Mode defaults to `KSafeWriteMode.Plain` — UI ephemera doesn't need encryption — pass `mode = KSafeWriteMode.Encrypted(...)` to opt in.

**Try it yourself:** open the demo, navigate to the **Flows** tab (or any non-default), close the app, relaunch. The Flows tab is still selected.

### Multilingual UI, persisted selection, and RTL

User-facing copy is represented by a flat `StringKey` enum. Composables resolve keys through
`Strings[...]` so they subscribe to live language changes; ViewModels and callbacks use
`.localized()`. Dialog and snackbar state carries `UiText` so it resolves at the UI edge.

```kotlin
// Composable — recomposes when the language changes
AppText(
    text = Strings[StringKey.PREFERENCES_LANGUAGE],
    style = AppTextStyle.SECTION_HEADING,
)

// Non-composable callback / ViewModel
val biometricReason = StringKey.COUNTERS_AUTHENTICATE_TO_SAVE.localized()

// ViewModel-produced text stays localized at the UI edge
rotationResult = UiText.res(
    StringKey.COUNTERS_ROTATION_RESULT,
    result.rotated,
    result.skipped,
    result.failed,
    result.keyGeneration,
)
```

The generic dropdown has no dependency on `Language`:

```kotlin
AppDropdown(
    selectedItem = currentLanguage,
    items = LocalizationManager.availableLanguages(),
    itemLabel = Language::displayName,
    label = Strings[StringKey.PREFERENCES_SELECT_LANGUAGE],
    onItemSelected = { language ->
        onIntent(PreferencesIntent.LanguageSelected(language))
    },
)
```

Persistence remains at the feature edge while `LocalizationManager` stays an in-memory resolver:

```kotlin
is PreferencesIntent.LanguageSelected -> {
    appLanguageStore.languageCode = intent.language.code
    LocalizationManager.setLanguage(intent.language)
}
```

At startup the saved ISO code wins, then the supported system language, then English. The root
`LocalizationProvider` also maps `Language.isRtl` to `LocalLayoutDirection`, so selecting Hebrew
mirrors the whole shared Compose hierarchy immediately.

### Property Delegation (Non-Compose)

```kotlin
// Encrypted by default
var count4 by ksafe(10)
var count5 by ksafe(20)

// Encrypted string
var count6 by ksafe("30")

// Unencrypted string
var count7 by ksafe("40", mode = KSafeWriteMode.Plain)
```

### Serializable Data Classes

```kotlin
@Serializable
data class AuthInfo(
    val accessToken: String = "",
    val refreshToken: String = "",
    val expiresIn: Long = 0L
)

var authInfo by ksafe.mutableStateOf(
    defaultValue = AuthInfo(
        accessToken = "abc",
        refreshToken = "def",
        expiresIn = 3600L
    ),
    key = "authInfo",
    mode = KSafeWriteMode.Encrypted()
)
```

### Biometric Authentication with Duration Cache

```kotlin
private val bioAuthDurationSeconds = 6

fun bioCounterIncrement() {
    ksafe.verifyBiometricDirect(
        reason = "Authenticate to save",
        authorizationDuration = BiometricAuthorizationDuration(
            duration = bioAuthDurationSeconds * 1000L,
            scope = viewModelScope.hashCode().toString()
        )
    ) { success ->
        if (success) {
            bioCount++
            // Start visible countdown only on fresh auth
            if (bioAuthRemaining == 0) {
                startBioAuthTimer()
            }
        }
    }
}
```

### Custom JSON Serialization (CustomJsonViewModel.kt)

```kotlin
// 1. Define custom serializers for types you don't own
object TimestampSerializer : KSerializer<Timestamp> {
    override val descriptor = PrimitiveSerialDescriptor("Timestamp", PrimitiveKind.LONG)
    override fun serialize(encoder: Encoder, value: Timestamp) = encoder.encodeLong(value.epochMillis)
    override fun deserialize(decoder: Decoder) = Timestamp(decoder.decodeLong())
}

object HexColorSerializer : KSerializer<HexColor> {
    override val descriptor = PrimitiveSerialDescriptor("HexColor", PrimitiveKind.STRING)
    override fun serialize(encoder: Encoder, value: HexColor) = encoder.encodeString(value.hex)
    override fun deserialize(decoder: Decoder) = HexColor(decoder.decodeString())
}

// 2. Register all serializers in one place
val customJson = Json {
    ignoreUnknownKeys = true
    serializersModule = SerializersModule {
        contextual(TimestampSerializer)
        contextual(HexColorSerializer)
        // add as many as you need
    }
}

// 3. Pass it via KSafeConfig — one setup, used everywhere
val ksafe = KSafe(
    config = KSafeConfig(json = customJson)
)

// 4. Use @Contextual types directly — no extra work at the call site
@Serializable
data class UserProfile(
    val name: String,
    @Contextual val createdAt: Timestamp,
    @Contextual val favoriteColor: HexColor
)

var profile by ksafe.mutableStateOf(
    defaultValue = defaultProfile,
    key = "custom_json_profile",
    mode = KSafeWriteMode.Encrypted()
)
```

> **Note:** `kotlinx-serialization-json` is provided as a transitive dependency by KSafe — no need to add it manually.

### Device Lock-State Protection (Modules.android.kt)

```kotlin
actual val platformModule: Module
    get() = module {
        single<KSafe> {
            KSafe(
                context = androidApplication(),
                config = KSafeConfig(requireUnlockedDevice = true),
                securityPolicy = KSafeSecurityPolicy.Strict.copy(
                    debuggerAttached = SecurityAction.WARN,
                    debugBuild = SecurityAction.WARN,
                    emulator = SecurityAction.WARN,
                    onViolation = { violation ->
                        SecurityViolationsHolder.addViolation(violation)
                    }
                )
            )
        }
    }
```

### WASM/JS Platform Setup (Modules.wasmJs.kt)

```kotlin
actual val platformModule: Module
    get() = module {
        single<KSafe> {
            KSafe(
                fileName = "wasmdata",
                securityPolicy = KSafeSecurityPolicy.WarnOnly.copy(
                    onViolation = { violation ->
                        SecurityViolationsHolder.addViolation(violation)
                    }
                )
            )
        }
    }
```

### WASM Entry Point (main.kt)

```kotlin
fun main() {
    val body = document.body ?: return
    ComposeViewport(body) {
        KoinMultiplatformApplication(config = createKoinConfiguration()) {
            var cacheReady by remember { mutableStateOf(false) }

            LaunchedEffect(Unit) {
                val ksafe: KSafe = getKoin().get()
                ksafe.awaitCacheReady()
                cacheReady = true
            }

            if (cacheReady) {
                AppContent()
            }
        }
    }
}
```

> **Note:** On WASM, Koin must be initialized before `awaitCacheReady()` can retrieve the KSafe instance. The `AppContent()` composable (extracted from `App()`) renders only after the async WebCrypto initialization completes.

### Flow Delegates (FlowDelegatesViewModel.kt)

```kotlin
class FlowDelegatesViewModel(private val ksafe: KSafe) : BaseGlobalViewModel() {

    // 1. asMutableStateFlow — drop-in for standard MutableStateFlow pattern
    private val _moviesState by ksafe.asMutableStateFlow(MoviesListState(), viewModelScope)
    val moviesState: StateFlow<MoviesListState> get() = _moviesState

    fun loadMovies() {
        _moviesState.update { it.copy(loading = true) }  // persists atomically
        viewModelScope.launch {
            val movies = api.getMovies()
            _moviesState.update { it.copy(loading = false, movies = movies) }
        }
    }

    // 2. Editable persisted flow + read-only cold flow
    private val _username: MutableStateFlow<String> by ksafe.asMutableStateFlow(
        StringKey.FLOWS_DEFAULT_USERNAME.localized(),
        viewModelScope,
        key = "username",
    )
    private val toggleMode: Flow<Boolean> by ksafe.asFlow(defaultValue = false)

    // Cold flow transformed into localized UI state
    private val toggleLabel: StateFlow<StringKey> = toggleMode
        .map { isOn ->
            if (isOn) StringKey.FLOWS_ON_MODE else StringKey.FLOWS_OFF_MODE
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            StringKey.FLOWS_OFF_MODE,
        )

    fun onNameChanged(name: String) {
        _username.update { name }
    }

    // 3. Cross-screen sync — observes Counters screen's "count2" key
    //    Without scope: frozen at init value
    //    With scope: updates in real-time when Counters screen writes
    var storageCountIsolated by ksafe.mutableStateOf(2000, key = "count2")
    var storageCountSynced by ksafe.mutableStateOf(2000, key = "count2", scope = viewModelScope)
}
```

---

## Security Policies

KSafe 1.4.0 introduces configurable security policies:

| Policy | Root/Jailbreak | Debugger | Debug Build | Emulator |
|--------|---------------|----------|-------------|----------|
| **Default** | IGNORE | IGNORE | IGNORE | IGNORE |
| **WarnOnly** | WARN | WARN | WARN | WARN |
| **Strict** | BLOCK | BLOCK | WARN | WARN |

### Security Actions

| Action | Behavior |
|--------|----------|
| `IGNORE` | No detection performed |
| `WARN` | Detection runs, callback invoked, app continues |
| `BLOCK` | Detection runs, callback invoked, throws `SecurityViolationException` |

---

## Lock-State Policy Test

The demo includes an interactive test for the `requireUnlockedDevice` feature:

1. Tap **"Test Lock Feature"** - a test value is pre-stored while the device is unlocked
2. A 15-second countdown begins - lock your device during this time
3. After the countdown, the app attempts to read the encrypted value from the Keychain/Keystore
4. Results:
   - **"READ BLOCKED"** - The feature works correctly; the Keychain/Keystore denied access while locked
   - **"READ SUCCEEDED"** - If running from Xcode with the debugger, this is expected (see below)

**Important (iOS):** Xcode's debugger prevents iOS data protection from fully engaging. For accurate results:
1. Build & run the app on your device from Xcode
2. Press **Stop** in Xcode to disconnect the debugger
3. Launch the app from the **Home Screen**
4. Run the lock test

---

## Biometric Authentication

The demo uses the standalone **`:ksafe-biometrics`** module from the KSafe library directly — no per-platform `expect/actual` wrapper in the demo itself. The module ships its own platform actuals (`BiometricPrompt` on Android, `LAContext` on iOS / macOS, no-op auto-success on JVM and Web), so the demo's `CountersViewModel.kt` calls a single common API:

```kotlin
import eu.anifantakis.lib.ksafe.biometrics.KSafeBiometrics
import eu.anifantakis.lib.ksafe.biometrics.BiometricAuthorizationDuration

fun bioCounterIncrement() {
    KSafeBiometrics.verifyBiometricDirect(
        reason = "Authenticate to save",
        authorizationDuration = BiometricAuthorizationDuration(
            duration = bioAuthDurationSeconds * 1000L,
            scope = viewModelScope.hashCode().toString(),
        ),
    ) { success ->
        if (success) {
            bioCount++
            if (bioAuthRemaining == 0) startBioAuthTimer()
        }
    }
}
```

**Per-platform behaviour, all from the library:**

| Platform | Hardware path |
|---|---|
| **Android** | `androidx.biometric` — Face / Fingerprint / Iris via `BiometricPrompt` |
| **iOS** | `LocalAuthentication.LAContext` — Face ID / Touch ID; simulator returns `true` |
| **macOS (native)** | `LocalAuthentication.LAContext` — Touch ID / Apple Watch unlock; hosts without a biometric sensor surface `LAErrorBiometryNotAvailable` and the callback receives `false` |
| **JVM / Desktop** | No biometric hardware; auto-succeeds so the demo flow keeps working |
| **WASM / JS** | No biometric API; auto-succeeds |

The duration cache + scope behaviour (`BiometricAuthorizationDuration`) is identical everywhere — see the library's [BIOMETRICS.md](https://github.com/ioannisa/ksafe/blob/main/docs/BIOMETRICS.md) for the full API.

---

## Project Structure

The project is split one module per platform entry point, around a single shared
KMP library. AGP 9 forbids the Android application plugin and the Kotlin
Multiplatform plugin in the same module, and the rest of the layout follows that
same shape — every `*App` module is a thin launcher, and all the real code lives
in `shared/`.

```
KSafeDemo/
├── shared/          # KMP library — all UI, view models, DI, expect/actual
├── androidApp/      # Android launcher      → com.android.application
├── desktopApp/      # Compose Desktop (JVM) → main() + jlink config
├── webApp/          # Compose for Web, WASM → main() + index.html
├── jsApp/           # Compose for Web, JS   → main() + index.html
├── macosApp/        # Native macOS          → NSApplication bootstrap + kexe
└── iosApp/          # Xcode project, links :shared's ComposeApp.framework
```

```
shared/src/
├── commonMain/kotlin/eu/anifantakis/ksafe_demo/
│   ├── App.kt                              # Koin/theme/startup gate + persisted route
│   ├── app/navigation/                     # Navigation3 routes, navigator and root
│   ├── core/
│   │   ├── data/
│   │   │   ├── persistence/KSafeStartup.kt # app-wide KSafe cache startup barrier
│   │   │   └── preferences/KSafeAppLanguageStore.kt # KSafe Plain language adapter
│   │   ├── domain/preferences/AppLanguageStore.kt    # persistence seam
│   │   └── presentation/
│   │       ├── design_system/
│   │       │   └── components/             # App* façade, including generic AppDropdown<T>
│   │       │       └── content/             # Reusable content cards, headers and status UI
│   │       ├── global_state/                # app-wide loading/snackbar state
│   │       ├── helper/                      # StateFlow/effect bridges + UiText
│   │       ├── scaffold/                    # shared application scaffold
│   │       └── string_resources/            # StringKey, manager, RTL provider + 6 languages
│   ├── di/
│   │   ├── KoinConfiguration.kt
│   │   ├── Modules.kt                      # shared DI (with `expect val platformModule`)
│   │   └── SecurityViolationsHolder.kt
│   └── features/                           # package-per-feature boundary
│       ├── counters/
│       │   ├── domain/model/AuthInfo.kt
│       │   └── presentation/
│       │       ├── platform/LockTestExecutionWindow.kt # lock-test execution seam
│       │       └── screens/counters/        # Root + private Screen + MVI ViewModel
│       ├── flows/presentation/screens/flow_delegates/
│       ├── custom_json/
│       │   ├── domain/model/UserProfile.kt
│       │   ├── data/serialization/             # contextual serializers + Json config
│       │   └── presentation/screens/custom_json/
│       ├── security/presentation/screens/security/
│       ├── preferences/
│       │   ├── domain/                         # ThemeMode + repository contract
│       │   ├── data/repository/                # KSafe Plain theme implementation
│       │   └── presentation/screens/preferences/ # appearance + language bottom sheet
│       └── about/presentation/screens/about/   # developer, version and repository links
│
├── androidMain/kotlin/eu/anifantakis/ksafe_demo/
│   ├── core/data/persistence/KSafeStartup.android.kt # startup barrier no-op
│   ├── di/Modules.android.kt               # KSafe with requireUnlockedDevice
│   └── features/counters/presentation/platform/
│       └── LockTestExecutionWindow.android.kt # lock test runs directly
│
├── appleMain/kotlin/eu/anifantakis/ksafe_demo/        ← shared by iOS + macOS (NEW in 2.0.1)
│   ├── core/data/persistence/KSafeStartup.apple.kt # startup barrier no-op
│   └── di/Modules.apple.kt                  # one DI module for both Apple targets
│
├── iosMain/kotlin/eu/anifantakis/ksafe_demo/
│   ├── MainViewController.kt                # ComposeUIViewController { App() } — Xcode calls this
│   └── features/counters/presentation/platform/
│       └── LockTestExecutionWindow.ios.kt   # finite UIKit execution for lock test
│
├── macosMain/kotlin/eu/anifantakis/ksafe_demo/        ← native macOS (NEW in 2.0.1)
│   └── features/counters/presentation/platform/
│       └── LockTestExecutionWindow.macos.kt # lock test runs directly
│
├── jvmMain/kotlin/eu/anifantakis/ksafe_demo/
│   ├── core/data/persistence/KSafeStartup.jvm.kt # startup barrier no-op
│   ├── di/Modules.jvm.kt
│   └── features/counters/presentation/platform/LockTestExecutionWindow.jvm.kt
│
├── jsMain/kotlin/eu/anifantakis/ksafe_demo/
│   ├── core/data/persistence/KSafeStartup.js.kt # awaits WebCrypto caches
│   ├── di/Modules.js.kt                     # KSafe with localStorage + WebCrypto
│   └── features/counters/presentation/platform/LockTestExecutionWindow.js.kt
│
└── wasmJsMain/kotlin/eu/anifantakis/ksafe_demo/
    ├── core/data/persistence/KSafeStartup.wasmJs.kt # awaits WebCrypto caches
    ├── di/Modules.wasmJs.kt                 # KSafe with localStorage + WebCrypto
    └── features/counters/presentation/platform/LockTestExecutionWindow.wasmJs.kt
```

Each launcher module holds only its `main()` (or `MainActivity`):

```
androidApp/src/main/                         # MainActivity.kt, AndroidManifest.xml, res/ (icons, theme)
desktopApp/src/main/kotlin/.../main.kt       # Compose Desktop application { Window { App() } }
webApp/src/wasmJsMain/kotlin/.../main.kt     # ComposeViewport + awaitCacheReady
jsApp/src/jsMain/kotlin/.../main.kt          # ComposeViewport
macosApp/src/macosMain/kotlin/.../main.macos.kt   # NSApplication + Window(...) { App() } + run loop
```

**Note on Apple source-set sharing:** the demo mirrors the lib's appleMain split — the platform-agnostic Koin module (which just builds a `KSafe` with the demo's security policy) and KSafe startup barrier live in `appleMain/`, while the UIKit-specific entry point (`MainViewController` using `UIViewController`) and the counters feature's `LockTestExecutionWindow` (using `UIApplication.beginBackgroundTask`) stay in `iosMain/`. The iOS framework is still exported from `shared/` — Xcode links it as `ComposeApp` — whereas the AppKit `NSApplication` bootstrap is an executable entry point and so lives in its own `macosApp/` module.

---

## Build & Run

### Android
```bash
./gradlew :androidApp:assembleDebug
./gradlew :androidApp:installDebug
```

### Desktop (JVM) — runs on macOS, Windows, Linux
```bash
./gradlew :desktopApp:run
```

### iOS
Open `iosApp/iosApp.xcodeproj` in Xcode and run. The Xcode build phase invokes
`./gradlew :shared:embedAndSignAppleFrameworkForXcode`.

### macOS (native) — Apple Silicon
```bash
./gradlew :macosApp:runDebugExecutableMacosArm64          # debug
./gradlew :macosApp:runReleaseExecutableMacosArm64        # release (smaller, faster)
```

The compiled binary lands at `macosApp/build/bin/macosArm64/debugExecutable/macosApp.kexe` (~61 MB self-contained Mach-O). Only `macosArm64` is declared; Intel Macs need `macosX64()` added to both `macosApp/build.gradle.kts` and `shared/build.gradle.kts`.

> **First-time setup:** Compose Multiplatform's native macOS targets are gated behind an experimental flag. The demo enables it automatically via `gradle.properties`:
> ```properties
> org.jetbrains.compose.experimental.macos.enabled=true
> ```
> JetBrains hasn't lifted this flag yet — the runtime works (Skia → AppKit), but hot reload, Compose previews, and component-library completeness are weaker than on JVM/Desktop. The Compose Desktop / JVM target remains the better daily-driver Mac path for UI iteration; the native target is what proves the lib's `appleMain` Keychain + CryptoKit code path works end-to-end.

### Browser (WASM)
```bash
./gradlew :webApp:wasmJsBrowserDevelopmentRun
```
Then open `http://localhost:8080/` in your browser.

### Browser (legacy JS)
```bash
./gradlew :jsApp:jsBrowserDevelopmentRun
```

---

## Dependencies

```kotlin
// build.gradle.kts
commonMain.dependencies {
    implementation("eu.anifantakis:ksafe:3.0.0")
    implementation("eu.anifantakis:ksafe-compose:3.0.0")
    implementation("eu.anifantakis:ksafe-biometrics:3.0.0")
    implementation(libs.material.icons.extended)
}
```

> `kotlinx-serialization-json` is provided transitively by `:ksafe` — no need to declare it. `:ksafe-biometrics` is the optional standalone biometric module (Android `BiometricPrompt`, iOS / macOS `LAContext`); pull it in only when an app actually wants biometric prompts.

The application does not hardcode the displayed KSafe version: the top bar and About screen read
it from the active KSafe instance's protection metadata at runtime.

---

## Key Takeaways

1. **Seamless Encryption**: KSafe makes encrypted storage as simple as regular storage
2. **Compose Integration**: `mutableStateOf` works exactly like Compose's native state for ViewModel state, and `rememberKSafeState` brings `rememberSaveable`-style ergonomics for composable-body local state — both with persistence across app restarts
3. **Flow Delegates**: `asStateFlow()` and `asMutableStateFlow()` — drop-in replacements for standard Kotlin flow patterns, with automatic persistence
4. **Cross-Platform**: Same API across Android, iOS, **native macOS**, JVM/Desktop, and Browser. Two macOS paths in one demo — JVM (Compose Desktop) and Kotlin/Native (Compose for AppKit) — both exercising KSafe end-to-end
5. **Security-First**: Runtime security detection helps protect sensitive data
6. **Biometric Ready**: Built-in support for biometric authentication with duration caching, including macOS Touch ID / Apple Watch unlock via the `:ksafe-biometrics` module
7. **Lock-State Protection**: `requireUnlockedDevice` ensures encrypted data is inaccessible when the device is locked
8. **Custom JSON**: Support for `@Contextual` types via custom `SerializersModule` — store any type
9. **Live Localization**: Six compiler-checked languages, persisted selection, `UiText` for effects, and automatic RTL mirroring for Hebrew
10. **Reusable Design System**: Feature screens consume the `App*` façade; generic components such as `AppDropdown<T>` remain independent of feature and localization types

---

## Resources

- [KSafe Library](https://github.com/ioannisa/ksafe)
- [KSafe Documentation](https://github.com/ioannisa/ksafe#readme)
- [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/)

---

## License

This demo is provided as-is for educational purposes. See the [KSafe library](https://github.com/ioannisa/ksafe) for licensing information.
