# KSafe Demo

A comprehensive Kotlin Multiplatform demo application showcasing [KSafe](https://github.com/ioannisa/ksafe) - a secure encrypted storage library with biometric authentication and runtime security detection.

**Platforms:** Android, iOS, Desktop (JVM)

---

## Screenshots

| Storage Screen | Security Screen |
|:--------------:|:---------------:|
| <img width="270" alt="image" src="https://github.com/user-attachments/assets/94a27132-6842-413e-aa8f-d6b8bfa87b61" /> | <img width="270" alt="image" src="https://github.com/user-attachments/assets/968c0156-c97d-4ca4-8f4b-77614048f870" /> |

---

## What This Demo Shows

This demo application serves as a practical guide to understanding and implementing KSafe in your own projects. It demonstrates:

### 1. Encrypted Persistent Storage
- **Compose State Integration**: Using `ksafe.mutableStateOf()` for reactive, encrypted persistence
- **Property Delegation**: Using `by ksafe()` for non-Compose encrypted properties
- **Encryption Options**: Both encrypted (default) and unencrypted storage modes
- **Complex Data Types**: Storing `@Serializable` data classes with automatic serialization

### 2. Biometric Authentication
- **Cross-Platform Support**: Face ID, Touch ID, and Fingerprint authentication
- **Authorization Duration**: Caching authentication for a specified time period
- **Scoped Authentication**: Binding auth validity to ViewModel lifecycle

### 3. Security Policy (New in 1.4.0)
- **Runtime Security Detection**: Root/Jailbreak, Debugger, Debug Build, Emulator
- **Configurable Actions**: IGNORE, WARN, or BLOCK for each security check
- **Security Callbacks**: Custom handling when violations are detected

---

## App Screens

### Storage Screen

Demonstrates various ways to persist data with KSafe:

| Feature | Description |
|---------|-------------|
| **Counter 1** | Regular Compose `mutableStateOf` - no persistence (baseline comparison) |
| **Counter 2** | `ksafe.mutableStateOf` - encrypted persistent state |
| **Counter 3** | `ksafe.mutableStateOf` with `encrypted = false` |
| **AuthInfo** | Serializable data class with encrypted persistence |
| **Biometric Count** | Counter protected by biometric authentication with 6-second authorization cache |

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
        encrypted = false
    )
        private set
}
```

### Property Delegation (Non-Compose)

```kotlin
// Encrypted by default
var count4 by ksafe(10)
var count5 by ksafe(20)

// Encrypted string
var count6 by ksafe("30")

// Unencrypted string
var count7 by ksafe("40", encrypted = false)
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
    encrypted = true
)
```

### Biometric Authentication with Duration Cache

```kotlin
fun bioCounterIncrement() {
    ksafe.verifyBiometricDirect(
        reason = "Authenticate to save",
        authorizationDuration = BiometricAuthorizationDuration(
            duration = 6_000L,  // 6 seconds cache
            scope = viewModelScope.hashCode().toString()  // Scoped to this ViewModel
        )
    ) { success ->
        if (success) {
            bioCount++
        }
    }
}
```

### Security Policy Configuration (Modules.android.kt)

```kotlin
actual val platformModule: Module
    get() = module {
        single<KSafe> {
            KSafe(
                context = androidApplication(),
                securityPolicy = KSafeSecurityPolicy.Strict.copy(
                    debugBuild = SecurityAction.WARN,
                    emulator = SecurityAction.WARN,
                    onViolation = { violation ->
                        // Log or handle security violations
                        SecurityViolationsHolder.addViolation(violation)
                    }
                )
            )
        }
    }
```

### Flow-based Reactive Updates

```kotlin
viewModelScope.launch {
    ksafe.getFlow<String?>(
        key = "access-token",
        defaultValue = null,
        encrypted = true
    ).collect { value ->
        println("Token changed: $value")
    }
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

## Biometric Authentication Architecture

This demo implements cross-platform biometric authentication using the **expect/actual** pattern:

```
commonMain/
└── biometric/
    └── BiometricAuthenticator.kt      # Interface + expect function

androidMain/
└── biometric/
    └── BiometricAuthenticator.android.kt  # BiometricPrompt

iosMain/
└── biometric/
    └── BiometricAuthenticator.ios.kt      # LocalAuthentication (Face ID/Touch ID)

jvmMain/
└── biometric/
    └── BiometricAuthenticator.jvm.kt      # Auto-authenticate (no hardware)
```

### Interface

```kotlin
interface BiometricAuthenticator {
    fun isAvailable(): Boolean
    fun authenticate(
        title: String,
        subtitle: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    )
}

@Composable
expect fun rememberBiometricAuthenticator(): BiometricAuthenticator
```

---

## Project Structure

```
composeApp/src/
├── commonMain/kotlin/eu/anifantakis/ksafe_demo/
│   ├── App.kt                           # Navigation with bottom tabs
│   ├── biometric/
│   │   └── BiometricAuthenticator.kt    # expect interface
│   ├── di/
│   │   ├── Modules.kt                   # Shared DI module
│   │   └── SecurityViolationsHolder.kt  # Security violations state
│   └── screens/
│       ├── counters/
│       │   ├── LibCounterScreen.kt      # Storage demo UI
│       │   └── LibCounterViewModel.kt   # Storage demo logic
│       └── security/
│           ├── SecurityScreen.kt        # Security status UI
│           └── SecurityViewModel.kt     # Security status logic
│
├── androidMain/kotlin/eu/anifantakis/ksafe_demo/
│   ├── MainActivity.kt
│   ├── biometric/BiometricAuthenticator.android.kt
│   └── di/Modules.android.kt            # KSafe with SecurityPolicy
│
├── iosMain/kotlin/eu/anifantakis/ksafe_demo/
│   ├── MainViewController.kt
│   ├── biometric/BiometricAuthenticator.ios.kt
│   └── di/Modules.ios.kt
│
└── jvmMain/kotlin/eu/anifantakis/ksafe_demo/
    ├── main.kt
    ├── biometric/BiometricAuthenticator.jvm.kt
    └── di/Modules.jvm.kt
```

---

## Build & Run

### Android
```bash
./gradlew :composeApp:assembleDebug
./gradlew :composeApp:installDebug
```

### Desktop (JVM)
```bash
./gradlew :composeApp:run
```

### iOS
Open `iosApp/iosApp.xcodeproj` in Xcode and run.

---

## Dependencies

```kotlin
// build.gradle.kts
commonMain.dependencies {
    implementation("eu.anifantakis:ksafe:1.4.0")
    implementation("eu.anifantakis:ksafe-compose:1.4.0")
}

androidMain.dependencies {
    implementation("androidx.biometric:biometric:1.1.0")
}
```

---

## Key Takeaways

1. **Seamless Encryption**: KSafe makes encrypted storage as simple as regular storage
2. **Compose Integration**: `mutableStateOf` works exactly like Compose's native state
3. **Cross-Platform**: Same API across Android, iOS, and Desktop
4. **Security-First**: Runtime security detection helps protect sensitive data
5. **Biometric Ready**: Built-in support for biometric authentication with caching

---

## Resources

- [KSafe Library](https://github.com/ioannisa/ksafe)
- [KSafe Documentation](https://github.com/ioannisa/ksafe#readme)
- [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/)

---

## License

This demo is provided as-is for educational purposes. See the [KSafe library](https://github.com/ioannisa/ksafe) for licensing information.
