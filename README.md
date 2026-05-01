# KSafe Demo

A comprehensive Kotlin Multiplatform demo application showcasing [KSafe](https://github.com/ioannisa/ksafe) - a secure encrypted storage library with biometric authentication, runtime security detection, and device lock-state protection.

**Platforms:** Android, iOS, macOS (native), Desktop (JVM), Browser (WASM/JS)

> The demo runs as a native binary on every supported KSafe target. macOS specifically has *two* paths in this demo — the JVM/Desktop binary (Compose Desktop, Skia + Swing) and the native `macosArm64`/`macosX64` binary (Compose Multiplatform native, Skia + AppKit). Both exercise KSafe end-to-end; the native binary additionally validates KSafe's `appleMain` Keychain + CryptoKit code path in a real Compose UI.

---

## Screenshots

| Storage Screen | Flows Screen | Custom JSON Screen | Security Screen |
|:--------------:|:------------:|:------------------:|:---------------:|
| <img width="270" alt="image" src="https://github.com/user-attachments/assets/4de5a40c-6335-4fbe-9f59-0b8bdcde8ff4" /> | *(screenshot pending)* | *(screenshot pending)* | <img width="270" alt="image" src="https://github.com/user-attachments/assets/968c0156-c97d-4ca4-8f4b-77614048f870" /> |

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

### 6. WASM/JS Browser Support (New in 1.6.0)
- **Browser localStorage**: Encrypted key-value storage in the browser via WebCrypto AES-256-GCM
- **Async Cache Initialization**: `awaitCacheReady()` gates rendering until WebCrypto decryption completes
- **Same API**: All KSafe features (property delegation, Compose state, StateFlow) work identically in the browser
- **Compose for Web**: Full `mutableStateOf` persistence via `ksafe-compose` WASM target

### 7. Device Lock-State Protection (New in 1.5.0)
- **`requireUnlockedDevice`**: Encrypted data is only accessible when the device is unlocked
- **Interactive Lock Test**: 15-second countdown to lock your device, then verifies encrypted reads are blocked
- **Platform Background Tasks**: iOS uses `beginBackgroundTaskWithExpirationHandler` to keep the test running while the screen is off

### 8. `rememberKSafeState` — composable-local persistent state (New in 2.0.0)
- **`rememberSaveable`-style API for KSafe**: composable-body local state that survives app restarts (not just config changes)
- **Auto-keying from property name**: `var idx by ksafe.rememberKSafeState(0)` stores under the key `"idx"` — explicit `key = "..."` available when you want it
- **Used in `App.kt`**: the bottom-tab selection (`var currentScreen by ksafe.rememberKSafeState(Screen.Storage)`) persists across cold launches with no ViewModel involved — see [Code Examples → `rememberKSafeState`](#bottom-tab-persistence-with-rememberksafestate-appkt)

### 9. Native macOS Target (New in 2.0.1)
- **Native `macosArm64` / `macosX64` binary**: Compose Multiplatform on AppKit, Skia rendering, KSafe's `appleMain` Keychain + CryptoKit path. Same source as iOS — no UI rewrites
- **Two macOS paths in one demo**: Compose **Desktop** on JVM (`./gradlew :composeApp:run`) uses KSafe's JDK crypto path; Compose **native macOS** (`./gradlew :composeApp:runDebugExecutableMacosArm64`) uses Keychain + Secure Enclave on Apple Silicon and T2-equipped Macs
- **Validates the lib's macOS target end-to-end**: KSafe ships 118 unit tests for macOS, the demo proves the same code works inside a real Compose UI

---

## App Screens

### Storage Screen

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
| **asStateFlow (username)** | Read-only hot `StateFlow<String>` delegate — editable via `OutlinedTextField`, writes through `kSafe.put()` |
| **asFlow (dark mode)** | Read-only cold `Flow<Boolean>` delegate — toggle via `Switch`, derived `themeLabel` via `.map{}.stateIn()` shows real flow transformation |
| **Cross-screen sync** | Two cards observe the Storage screen's Counter 2 (key `"count2"`): one **without scope** (frozen at init value) and one **with scope** (updates in real-time when you tap "+" on the Storage tab). Proves the difference between isolated and synced `mutableStateOf` across separate ViewModels |

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

---

## Code Examples from the Demo

### Basic Encrypted State (LibCounterViewModel.kt)

```kotlin
class LibCounterViewModel(val ksafe: KSafe) : ViewModel() {

    // Regular Compose state - no persistence
    var count1 by mutableStateOf(1000)
        private set

    // KSafe encrypted state - persists across app restarts
    var count2 by ksafe.mutableStateOf(2000)
        private set

    // KSafe unencrypted state with custom key
    var count3 by ksafe.mutableStateOf(
        defaultValue = 3000,
        key = "counter3Key",
        mode = KSafeWriteMode.Plain
    )
        private set
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
    // pre-2.0 version that used `remember { mutableStateOf(Screen.Storage) }`,
    // which only survived recomposition.
    var currentScreen by ksafe.rememberKSafeState(Screen.Storage)

    Scaffold(
        bottomBar = {
            NavigationBar {
                Screen.entries.forEach { screen ->
                    NavigationBarItem(
                        selected = currentScreen == screen,
                        onClick = { currentScreen = screen },
                        label = { Text(screen.title) },
                        icon = { }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when (currentScreen) {
                Screen.Storage -> LibCounterScreen()
                Screen.Flows -> FlowDelegatesScreen()
                Screen.CustomJson -> CustomJsonScreen()
                Screen.Security -> SecurityScreen()
            }
        }
    }
}
```

**The split between `rememberKSafeState` and `mutableStateOf`:**

| Use case | API |
|---|---|
| ViewModel-owned / cross-screen state, business logic | `var x by ksafe.mutableStateOf(default)` |
| Composable-body local state (tab index, scroll, expanded sections, draft form input) | `var x by ksafe.rememberKSafeState(default)` |

**Auto-keying:** the property name (`currentScreen`) is captured at `provideDelegate` time, so the storage key falls through automatically. Pass `key = "..."` when you want it explicit. Mode defaults to `KSafeWriteMode.Plain` — UI ephemera doesn't need encryption — pass `mode = KSafeWriteMode.Encrypted(...)` to opt in.

**Try it yourself:** open the demo, navigate to the **Flows** tab (or any non-default), close the app, relaunch. The Flows tab is still selected.

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
class FlowDelegatesViewModel(private val ksafe: KSafe) : ViewModel() {

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

    // 2. asStateFlow & asFlow — read-only reactive observation
    val username: StateFlow<String> by ksafe.asStateFlow("Guest", viewModelScope)
    val darkMode: Flow<Boolean> by ksafe.asFlow(defaultValue = false)

    // Cold flow transformed into derived state — real-world pattern
    val themeLabel: StateFlow<String> = darkMode
        .map { if (it) "Dark Mode" else "Light Mode" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "Light Mode")

    fun onNameChanged(name: String) {
        viewModelScope.launch { ksafe.put("username", name) }
    }

    // 3. Cross-screen sync — observes Storage screen's "count2" key
    //    Without scope: frozen at init value
    //    With scope: updates in real-time when Storage screen writes
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

The demo uses the standalone **`:ksafe-biometrics`** module from the KSafe library directly — no per-platform `expect/actual` wrapper in the demo itself. The module ships its own platform actuals (`BiometricPrompt` on Android, `LAContext` on iOS / macOS, no-op auto-success on JVM and Web), so the demo's `LibCounterViewModel.kt` calls a single common API:

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

```
composeApp/src/
├── commonMain/kotlin/eu/anifantakis/ksafe_demo/
│   ├── App.kt                              # bottom-tab nav; uses ksafe.rememberKSafeState
│   ├── di/
│   │   ├── KoinConfiguration.kt
│   │   ├── Modules.kt                      # shared DI (with `expect val platformModule`)
│   │   └── SecurityViolationsHolder.kt
│   ├── util/
│   │   └── BackgroundTask.kt               # expect for platform background tasks
│   └── screens/
│       ├── counters/
│       │   ├── LibCounterScreen.kt         # Storage demo UI
│       │   └── LibCounterViewModel.kt      # Storage demo logic + KSafeBiometrics calls
│       ├── flows/
│       │   ├── FlowDelegatesScreen.kt      # Flow delegates demo UI (1.8.0)
│       │   └── FlowDelegatesViewModel.kt   # asFlow / asStateFlow / asMutableStateFlow
│       ├── customjson/
│       │   ├── CustomJsonScreen.kt
│       │   └── CustomJsonViewModel.kt      # @Contextual types + custom SerializersModule
│       └── security/
│           ├── SecurityScreen.kt
│           └── SecurityViewModel.kt
│
├── androidMain/kotlin/eu/anifantakis/ksafe_demo/
│   ├── MainActivity.kt
│   ├── di/Modules.android.kt               # KSafe with requireUnlockedDevice
│   └── util/BackgroundTask.android.kt      # No-op (Android doesn't suspend)
│
├── appleMain/kotlin/eu/anifantakis/ksafe_demo/        ← shared by iOS + macOS (NEW in 2.0.1)
│   └── di/Modules.apple.kt                  # one DI module for both Apple targets
│
├── iosMain/kotlin/eu/anifantakis/ksafe_demo/
│   ├── MainViewController.kt                # ComposeUIViewController { App() }
│   └── util/BackgroundTask.ios.kt           # UIApplication.beginBackgroundTask for lock test
│
├── macosMain/kotlin/eu/anifantakis/ksafe_demo/        ← native macOS (NEW in 2.0.1)
│   ├── main.macos.kt                        # NSApplication + Window(...) { App() } + run loop
│   └── util/BackgroundTask.macos.kt         # No-op (macOS doesn't suspend apps)
│
├── jvmMain/kotlin/eu/anifantakis/ksafe_demo/
│   ├── main.kt                              # Compose Desktop application { Window { App() } }
│   ├── di/Modules.jvm.kt
│   └── util/BackgroundTask.jvm.kt           # No-op
│
└── wasmJsMain/kotlin/eu/anifantakis/ksafe_demo/
    ├── main.kt                              # ComposeViewport + awaitCacheReady
    ├── di/Modules.wasmJs.kt                 # KSafe with localStorage + WebCrypto
    └── util/BackgroundTask.wasmJs.kt        # No-op
```

**Note on Apple source-set sharing:** the demo mirrors the lib's appleMain split — the platform-agnostic Koin module (which just builds a `KSafe` with the demo's security policy) lives in `appleMain/`, while UIKit-specific entry points (`MainViewController` using `UIViewController`, `BackgroundTask` using `UIApplication.beginBackgroundTask`) stay in `iosMain/` and AppKit-specific entry points (`NSApplication` bootstrap) live in `macosMain/`.

---

## Build & Run

### Android
```bash
./gradlew :composeApp:assembleDebug
./gradlew :composeApp:installDebug
```

### Desktop (JVM) — runs on macOS, Windows, Linux
```bash
./gradlew :composeApp:run
```

### iOS
Open `iosApp/iosApp.xcodeproj` in Xcode and run.

### macOS (native) — Apple Silicon
```bash
./gradlew :composeApp:runDebugExecutableMacosArm64        # debug
./gradlew :composeApp:runReleaseExecutableMacosArm64      # release (smaller, faster)
```

For Intel Macs, swap `MacosArm64` → `MacosX64`. The compiled binary lands at `composeApp/build/bin/macosArm64/debugExecutable/composeApp.kexe` (~61 MB self-contained Mach-O).

> **First-time setup:** Compose Multiplatform's native macOS targets are gated behind an experimental flag. The demo enables it automatically via `gradle.properties`:
> ```properties
> org.jetbrains.compose.experimental.macos.enabled=true
> ```
> JetBrains hasn't lifted this flag yet — the runtime works (Skia → AppKit), but hot reload, Compose previews, and component-library completeness are weaker than on JVM/Desktop. The Compose Desktop / JVM target remains the better daily-driver Mac path for UI iteration; the native target is what proves the lib's `appleMain` Keychain + CryptoKit code path works end-to-end.

### Browser (WASM/JS)
```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```
Then open `http://localhost:8080/` in your browser.

---

## Dependencies

```kotlin
// build.gradle.kts
commonMain.dependencies {
    implementation("eu.anifantakis:ksafe:2.0.1-Beta01")
    implementation("eu.anifantakis:ksafe-compose:2.0.1-Beta01")
    implementation("eu.anifantakis:ksafe-biometrics:2.0.1-Beta01")
}
```

> `kotlinx-serialization-json` is provided transitively by `:ksafe` — no need to declare it. `:ksafe-biometrics` is the optional standalone biometric module (Android `BiometricPrompt`, iOS / macOS `LAContext`); pull it in only when an app actually wants biometric prompts.

**Why 2.0.1-Beta01 and not 2.0.0:** 2.0.1 is the release that added native `macosArm64` / `macosX64` artifacts to all three modules. If your project doesn't target native macOS, 2.0.0 also works — the iOS / Android / JVM / Web paths are identical.

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

---

## Resources

- [KSafe Library](https://github.com/ioannisa/ksafe)
- [KSafe Documentation](https://github.com/ioannisa/ksafe#readme)
- [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/)

---

## License

This demo is provided as-is for educational purposes. See the [KSafe library](https://github.com/ioannisa/ksafe) for licensing information.
