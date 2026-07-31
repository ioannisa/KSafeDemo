# KSafe Demo

The official companion showcase for [KSafe](https://github.com/ioannisa/KSafe), a
secure-by-default Kotlin Multiplatform key/value persistence library.

One shared Compose Multiplatform codebase, running on **Android · iOS · native macOS ·
JVM Desktop · Kotlin/Wasm · Kotlin/JS**, exercising encrypted variables, persistent Compose
state, reactive flows, custom serialization, hardware-isolated keys, key rotation,
biometrics and runtime security diagnostics.

[KSafe library](https://github.com/ioannisa/KSafe) ·
[KSafe docs](https://github.com/ioannisa/KSafe#readme) ·
**[Full implementation guide →](docs/DETAILS.md)**

---

## KSafe in one minute

```kotlin
var token by ksafe("")            // encrypted + persisted, by default
token = "secret"

var counter by ksafe.mutableStateOf(0)                      // Compose state, persisted
var tab by ksafe.rememberKSafeState(AppRoute.Counters)      // composable-local, survives cold launch
private val _state by ksafe.asMutableStateFlow(State(), viewModelScope)   // MutableStateFlow, persisted

var theme by ksafe(ThemeMode.SYSTEM, mode = KSafeWriteMode.Plain)   // opt OUT per entry
```

Add to your own app (this demo runs against `3.0.0`):

```kotlin
implementation("eu.anifantakis:ksafe:3.0.0")
implementation("eu.anifantakis:ksafe-compose:3.0.0")
implementation("eu.anifantakis:ksafe-biometrics:3.0.0")
```

`kotlinx-serialization-json` arrives transitively with `:ksafe`.

---

## Feature map

Each section points at the file that implements it, so you can read the real code instead of
a snippet. Long-form walkthroughs live in [docs/DETAILS.md](docs/DETAILS.md).

### Counters — every persistence flavour, side by side

**[`CountersViewModel.kt`](shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/features/counters/presentation/screens/counters/CountersViewModel.kt)** — the API sampler

- Four flavours declared next to each other: plain `mutableStateOf` (resets on relaunch),
  `ksafe.mutableStateOf` (key defaults to the property name), `asMutableStateFlow(…, viewModelScope)`,
  and non-Compose `by ksafe(…)`.
- The scope lesson: the *unscoped* delegate never sees the Flows tab's writes to the same
  `count2` key, so it needs `getDirect` to refresh — the scoped twin just updates.
- Three write modes: `KSafeWriteMode.Plain`, `Encrypted()`, and
  `Encrypted(protection = HARDWARE_ISOLATED)` for the StrongBox / Secure Enclave vault entry.
- `rotateKeys()` re-keys the **whole store** — no key argument — and reports
  `rotated / skipped / failed / keyGeneration`.
- Biometrics, defensively: probe `KSafeBiometrics.biometricsAvailableDirect(…)` first, then
  `verifyBiometricDirect(reason, BiometricAuthorizationDuration(duration, scope))` so a repeat
  action inside the window doesn't re-prompt.
- Direct vs suspend families (`getDirect`/`putDirect` vs `get`/`put` in a coroutine), plus
  `getKeyInfo(key)` to audit what each key actually got.
- The lock test: a value written with `requireUnlockedDevice = true`, then read back 15 s later
  to prove the read is refused while the device is locked.

**[`CountersScreen.kt`](shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/features/counters/presentation/screens/counters/CountersScreen.kt)** — contains **zero** `ksafe.*` imports: persistence never leaks into the UI layer. Its `@PreviewLightDark` renders without KSafe, Koin or a keystore.

**[`AuthInfo.kt`](shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/features/counters/domain/model/AuthInfo.kt)** — `@Serializable` is the *only* requirement for KSafe to round-trip a whole data class through one delegate.

**[`LockTestExecutionWindow.kt`](shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/features/counters/presentation/platform/LockTestExecutionWindow.kt)** — a generic, suspending, higher-order `expect fun` (six actuals); the iOS one wraps the test in `beginBackgroundTaskWithName` so a locked screen doesn't kill it.

### Flows — reactive persistence

**[`FlowDelegatesViewModel.kt`](shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/features/flows/presentation/screens/flow_delegates/FlowDelegatesViewModel.kt)**

- `asMutableStateFlow(…)` is a drop-in for `MutableStateFlow`: `.value =` and `.update { }`
  still work, but every emission is persisted — a loaded list survives a cold launch with no
  save/restore code.
- A whole `@Serializable` UI state (loading / list / error) round-trips as one value, instead
  of storing primitives key by key.
- `asFlow(defaultValue)` gives a cold, scope-free read-only Flow that picks up *external*
  writes, then `.map { }.stateIn(…)` derives a label from it.
- Two delegates over **one** key, declared next to each other: `mutableStateOf(key = "count2")`
  with and without `scope = viewModelScope` — only the scoped one follows the Counters tab live.

**[`FlowDelegatesScreen.kt`](shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/features/flows/presentation/screens/flow_delegates/FlowDelegatesScreen.kt)** — shows the isolated and synced values as two cards, so the scope difference is visible proof rather than prose.

### Custom JSON — serialization you control

**[`CustomJsonSerialization.kt`](shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/features/custom_json/data/serialization/CustomJsonSerialization.kt)**

- Defines the one `Json { }` instance every platform module hands to KSafe as
  `KSafeConfig(json = …)` — KSafe serializes with *your* format.
- A `SerializersModule` with `contextual(…)` entries is what resolves `@Contextual` fields at
  persist time.
- `ignoreUnknownKeys = true` is the persistence-specific lesson: stored JSON outlives the code
  that wrote it, so an older blob with an extra field still decodes.

**[`UserProfile.kt`](shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/features/custom_json/domain/model/UserProfile.kt)** — its wrapper types carry **no** `@Serializable`: the pattern for third-party or legacy types you cannot annotate.

### Security — what the device actually gave you

**[`SecurityViewModel.kt`](shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/features/security/presentation/screens/security/SecurityViewModel.kt)** / **[`SecurityScreen.kt`](shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/features/security/presentation/screens/security/SecurityScreen.kt)**

- `ksafe.protectionInfo` is re-read on every refresh, not cached — it is live and can change
  after rotation or recovery.
- Degradation is `effectiveLevel < intendedLevel`: `KSafeProtectionLevel` is an **ordered**
  enum, so a silent fallback to a weaker tier is detectable, not hidden.
- An exhaustive `when` over every protection level and every `SecurityViolation` means a new
  tier or check breaks the build instead of rendering silently.

**[`SecurityViolationsHolder.kt`](shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/di/SecurityViolationsHolder.kt)** — solves an ordering problem worth knowing: `KSafeSecurityPolicy(onViolation = …)` fires **while KSafe is being constructed**, before any ViewModel exists, so the callbacks are buffered for the UI.

### Preferences, theme & language — non-secret persistence

**[`ThemePreferenceRepositoryImpl.kt`](shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/features/preferences/data/repository/ThemePreferenceRepositoryImpl.kt)**

- `by preferences.asWritableFlow(default, key = …, mode = KSafeWriteMode.Plain)` is the
  *entire* persistence layer — read side and write side in one delegate.
- Persists the `ThemeMode` enum directly through the reified type parameter — no manual
  `toString()` / `valueOf()`.
- An explicit `key` (not the property name) so renaming the property can never orphan stored data.

**[`KSafeAppLanguageStore.kt`](shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/core/data/preferences/KSafeAppLanguageStore.kt)** — a KSafe delegate satisfying a plain domain interface, with non-suspend get/set so startup code can read the saved language synchronously.

**[`LocalizationManager.kt`](shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/core/presentation/string_resources/LocalizationManager.kt)** / **[`StringKey.kt`](shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/core/presentation/string_resources/StringKey.kt)** — six languages incl. RTL Hebrew; `resolveStartup` turns *saved code → device locale → fallback* into three lines, and the closed `StringKey` enum makes a missing translation a **compile error**.

### App shell — Compose state that outlives the process

**[`AppContent.kt`](shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/app/AppContent.kt)**

- `var currentRoute by ksafe.rememberKSafeState(AppRoute.Counters)` — the selected bottom tab
  survives a **cold launch** in one line, inside a composable, with no ViewModel and no
  `SavedStateHandle`.
- The persisted value is a `@Serializable` sealed-interface route, so `rememberKSafeState`
  handles any serializable type, not just primitives.
- Restored state is *validated*: a route no longer in the bottom bar falls back and is
  rewritten — persisted data can outlive the code that wrote it.
- Startup diagnostics print `intendedLevel` / `effectiveLevel` / `custody` / `notes`, and probe
  the same value written three ways to compare what each mode actually achieved.

**[`AppStartupCoordinator.kt`](shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/app/startup/AppStartupCoordinator.kt)** — the splash gate and the pipeline it waits on: KSafe barrier → the app's `preload` lambda → theme/language reads, all under one `withTimeout`, with `Loading / Ready(themeMode) / Failed` and a retry path.

**[`AppPreload.kt`](shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/app/startup/AppPreload.kt)** / **[`App.kt`](shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/App.kt)** — app-specific first-frame work is one suspend lambda: `App(preload = { get<MyRepository>().warmUp() })`. Storage readiness is **already guaranteed** before it runs, so the lambda never needs a barrier call.

**[`NavigationRoot.kt`](shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/app/navigation/NavigationRoot.kt)** — Navigation3 in common code, and a deliberate counter-example: transient sheet visibility stays `rememberSaveable`, only the durable tab selection goes to KSafe.

### DI & the browser readiness barrier

**[`Modules.kt`](shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/di/Modules.kt)** (+ [android](shared/src/androidMain/kotlin/eu/anifantakis/ksafe_demo/di/Modules.android.kt) / [jvm](shared/src/jvmMain/kotlin/eu/anifantakis/ksafe_demo/di/Modules.jvm.kt) actuals)

- Three independently configured stores told apart by Koin qualifiers — a plain-ish
  preferences store, a custom-`Json` store, and the default one.
- KSafe is always injected, never a global: repositories take the store as a constructor
  parameter, so tests can swap it.
- Android is the only target whose constructor needs a `Context`; every other platform builds
  from a file name alone.

**[`KSafeStartup.kt`](shared/src/commonMain/kotlin/eu/anifantakis/ksafe_demo/core/data/persistence/KSafeStartup.kt)** — the `expect/actual` barrier: [web](shared/src/wasmJsMain/kotlin/eu/anifantakis/ksafe_demo/core/data/persistence/KSafeStartup.wasmJs.kt) awaits each store's `awaitCacheReady()` (IndexedDB + WebCrypto hydrate asynchronously), while [Android](shared/src/androidMain/kotlin/eu/anifantakis/ksafe_demo/core/data/persistence/KSafeStartup.android.kt), Apple and JVM are `= Unit`. Common startup code calls it unconditionally — no `if (platform == WEB)` anywhere.

---

## Screenshots

| Counters | Flows | Custom JSON | Security | Preferences |
|:---:|:---:|:---:|:---:|:---:|
| <img width="270" alt="KSafe counters demo" src="https://github.com/user-attachments/assets/fbf461b5-b2c6-4b2a-9de4-1443a2aa3a84" /> | <img width="270" alt="KSafe flow delegates demo" src="https://github.com/user-attachments/assets/f05fed91-1df4-4810-a684-6b2258535700" /> | <img width="270" alt="KSafe custom JSON demo" src="https://github.com/user-attachments/assets/d7b3abcd-6f6e-4bad-9f2e-ef78f70aea6e" /> | <img width="270" alt="KSafe security diagnostics" src="https://github.com/user-attachments/assets/af169904-64cb-4cc9-aeef-71668a269825" /> | <img width="270" alt="KSafe Demo preferences" src="https://github.com/user-attachments/assets/1ba21590-9522-4dfc-a19a-136c535967cb" /> |

---

## Run it

| Target | Command |
|---|---|
| Android | `./gradlew :androidApp:installDebug` |
| JVM Desktop | `./gradlew :desktopApp:run` |
| iOS | open `iosApp/iosApp.xcodeproj` in Xcode, run the `iosApp` scheme |
| Native macOS | `./gradlew :macosApp:runDebugExecutableMacosArm64` |
| Browser (Wasm) | `./gradlew :webApp:wasmJsBrowserDevelopmentRun` |
| Browser (JS) | `./gradlew :jsApp:jsBrowserDevelopmentRun` |

The same shared API picks the right key custody per target — Android Keystore/StrongBox, Apple
Keychain/Secure Enclave, DPAPI / macOS Keychain / Secret Service on desktop, and a
non-extractable WebCrypto key in the browser. The Security screen reads
`ksafe.protectionInfo`, so simulator, hardware and fallback differences stay **visible**
instead of being hidden.

---

## Learn more

- [KSafe repository and setup guide](https://github.com/ioannisa/KSafe)
- [Usage reference](https://github.com/ioannisa/KSafe/blob/main/docs/USAGE.md) ·
  [Security model](https://github.com/ioannisa/KSafe/blob/main/docs/SECURITY_MODEL.md) ·
  [Key rotation](https://github.com/ioannisa/KSafe/blob/main/docs/KEY_ROTATION.md) ·
  [Biometrics](https://github.com/ioannisa/KSafe/blob/main/docs/BIOMETRICS.md)
- [Full KSafeDemo implementation guide](docs/DETAILS.md) — project structure, Koin wiring,
  per-platform notes and complete source walkthroughs

## License

This demo is provided as-is for educational purposes. See the
[KSafe repository](https://github.com/ioannisa/KSafe) for library licensing information.
