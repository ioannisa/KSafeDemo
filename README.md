# KSafe Demo

The official companion showcase for [KSafe](https://github.com/ioannisa/KSafe), a
secure-by-default Kotlin Multiplatform key/value persistence library.

This application lets you see and exercise KSafe in a real Compose Multiplatform project:
encrypted variables, persistent Compose state, reactive flows, serializable objects,
hardware-isolated protection, key rotation, biometrics, and runtime security diagnostics —
all from shared Kotlin code.

**Runs on:** Android · iOS · native macOS · JVM Desktop · Kotlin/Wasm · Kotlin/JS

[KSafe library](https://github.com/ioannisa/KSafe) ·
[KSafe documentation](https://github.com/ioannisa/KSafe#readme) ·
[Detailed demo guide](docs/DETAILS.md)

---

## KSafe in One Minute

KSafe persists ordinary Kotlin values across app restarts with encryption enabled by
default:

```kotlin
var token by ksafe("")
token = "secret" // encrypted and persisted automatically
```

The same model extends naturally to Compose state, flows, and serializable objects:

```kotlin
// ViewModel-owned Compose state
var counter by ksafe.mutableStateOf(0)

// Composable-local state that survives process death
var currentTab by ksafe.rememberKSafeState(AppRoute.Counters)

// Persisted and reactive MutableStateFlow
private val _state by ksafe.asMutableStateFlow(
    MoviesListState(),
    viewModelScope,
)

// Plain storage is an explicit per-entry opt-out
var theme by ksafe(
    ThemeMode.System,
    mode = KSafeWriteMode.Plain,
)
```

The demo currently consumes:

```kotlin
implementation("eu.anifantakis:ksafe:3.0.0")
implementation("eu.anifantakis:ksafe-compose:3.0.0")
implementation("eu.anifantakis:ksafe-biometrics:3.0.0")
```

`kotlinx-serialization-json` is provided transitively by `:ksafe`.

---

## What You Can Test

| Area | What the demo proves |
|---|---|
| Encrypted persistence | Property delegates and Compose state survive cold launches with encryption enabled by default |
| Plain persistence | Non-sensitive values can opt out per entry with `KSafeWriteMode.Plain` |
| Compose integration | `mutableStateOf` works for ViewModel state, while `rememberKSafeState` persists composable-local state |
| Reactive storage | `asFlow` and `asMutableStateFlow` stay synchronized with writes from other screens |
| Complex data | `@Serializable` data classes are stored without manual encoding |
| Custom serialization | A configured `Json` and `SerializersModule` support `@Contextual` third-party-style types |
| Hardware isolation | A value requests StrongBox on Android or Secure Enclave on Apple platforms through `HARDWARE_ISOLATED` |
| Key rotation | `rotateKeys()` re-encrypts the complete store under fresh key material and reports its result |
| Device lock policy | An interactive test exercises `requireUnlockedDevice` on Android and Apple platforms |
| Biometrics | The standalone biometrics module gates an action and demonstrates a scoped authorization window |
| Protection diagnostics | The app shows intended versus effective protection, key custody, per-key information, and reported degradation |
| Runtime policy | Root/jailbreak, debugger, debug-build, and emulator checks demonstrate configurable security actions |
| Browser startup | Web targets await KSafe's asynchronous WebCrypto cache before rendering the application |

---

## Explore the Demo

### Counters

Compare ordinary in-memory state with KSafe-backed encrypted and plain Compose state. The
screen also demonstrates:

- property delegates outside Compose;
- an encrypted `@Serializable` `AuthInfo`;
- a hardware-isolated vault entry;
- per-key protection information;
- biometric-gated updates;
- whole-store key rotation;
- the interactive device-lock test.

### Flows

See persisted `asFlow` and `asMutableStateFlow` values used with familiar Flow APIs. A
shared counter illustrates the difference between isolated state and
`mutableStateOf(scope = viewModelScope)`, which observes external writes in real time.

### Custom JSON

Store the same data class in encrypted and plain modes while its `@Contextual` fields use
custom serializers registered once through `KSafeConfig(json = customJson)`.

### Security

Inspect the active platform's live KSafe posture:

- intended and effective protection levels;
- key-custody description and degradation notes;
- root/jailbreak, debugger, debug-build, and emulator signals;
- the security action configured for each signal.

Preferences and About live in the top application menu rather than the bottom navigation.
Preferences stores theme and language as non-sensitive KSafe values. The complete shared UI
supports English, German, Italian, Hebrew, French, and Greek, including RTL layout for
Hebrew. About and the top bar report the KSafe version from the active library instance.

---

## Screenshots

| Counters | Flows | Custom JSON | Security | Preferences |
|:---:|:---:|:---:|:---:|:---:|
| <img width="270" alt="KSafe counters demo" src="https://github.com/user-attachments/assets/fbf461b5-b2c6-4b2a-9de4-1443a2aa3a84" /> | <img width="270" alt="KSafe flow delegates demo" src="https://github.com/user-attachments/assets/f05fed91-1df4-4810-a684-6b2258535700" /> | <img width="270" alt="KSafe custom JSON demo" src="https://github.com/user-attachments/assets/d7b3abcd-6f6e-4bad-9f2e-ef78f70aea6e" /> | <img width="270" alt="KSafe security diagnostics" src="https://github.com/user-attachments/assets/af169904-64cb-4cc9-aeef-71668a269825" /> | <img width="270" alt="KSafe Demo preferences" src="https://github.com/user-attachments/assets/1ba21590-9522-4dfc-a19a-136c535967cb" /> |

---

## Platform Protection

The same shared API selects the appropriate storage and key-custody mechanism on each
target:

| Platform | KSafe key custody |
|---|---|
| Android | Android Keystore / TEE, with StrongBox for requested hardware-isolated entries |
| iOS and native macOS | Apple Keychain, with Secure Enclave for requested hardware-isolated entries |
| JVM Desktop | Windows DPAPI, macOS Keychain, or Linux Secret Service, with a reported software fallback when necessary |
| Kotlin/Wasm and Kotlin/JS | WebCrypto with a non-extractable key in IndexedDB and encrypted values in browser storage |

The Security screen is deliberately based on `ksafe.protectionInfo`, so simulator,
hardware, browser, and desktop fallback differences remain visible instead of being hidden.

---

## Run the Demo

Clone the repository, then choose a target.

### Android

```bash
./gradlew :androidApp:installDebug
```

### JVM Desktop

```bash
./gradlew :desktopApp:run
```

### iOS

Open `iosApp/iosApp.xcodeproj` in Xcode and run the `iosApp` scheme.

### Native macOS

```bash
./gradlew :macosApp:runDebugExecutableMacosArm64
```

### Browser — Kotlin/Wasm

```bash
./gradlew :webApp:wasmJsBrowserDevelopmentRun
```

### Browser — Kotlin/JS

```bash
./gradlew :jsApp:jsBrowserDevelopmentRun
```

For platform notes, project structure, Koin wiring, full source walkthroughs, localization
architecture, and additional build details, read [docs/DETAILS.md](docs/DETAILS.md).

---

## Learn More

- [KSafe repository and setup guide](https://github.com/ioannisa/KSafe)
- [KSafe usage reference](https://github.com/ioannisa/KSafe/blob/main/docs/USAGE.md)
- [KSafe security model](https://github.com/ioannisa/KSafe/blob/main/docs/SECURITY_MODEL.md)
- [KSafe key rotation](https://github.com/ioannisa/KSafe/blob/main/docs/KEY_ROTATION.md)
- [KSafe biometric authentication](https://github.com/ioannisa/KSafe/blob/main/docs/BIOMETRICS.md)
- [Detailed KSafeDemo implementation guide](docs/DETAILS.md)

## License

This demo is provided as-is for educational purposes. See the
[KSafe repository](https://github.com/ioannisa/KSafe) for library licensing information.
