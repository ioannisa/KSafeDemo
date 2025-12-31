# KSafe Demo

A Kotlin Multiplatform demo app showcasing [KSafe](https://github.com/ianifantakis/ksafe) - a secure storage library with biometric authentication support.

**Targets:** Android, iOS, Desktop (JVM)

## Features

- Encrypted/unencrypted persistent storage
- Biometric authentication (Face ID, Touch ID, Fingerprint)
- Compose Multiplatform UI
- Koin dependency injection

---

## Biometric Authentication

This project implements multiplatform biometric authentication using the **expect/actual** pattern.

### Architecture

```
commonMain/
└── biometric/
    └── BiometricAuthenticator.kt      # Interface + expect function

androidMain/
└── biometric/
    └── BiometricAuthenticator.android.kt  # BiometricPrompt implementation

iosMain/
└── biometric/
    └── BiometricAuthenticator.ios.kt      # LocalAuthentication (Face ID/Touch ID)

jvmMain/
└── biometric/
    └── BiometricAuthenticator.jvm.kt      # Auto-authenticate (no biometric hardware)
```

### Usage in Compose

```kotlin
@Composable
fun MyScreen() {
    // Get platform-specific biometric authenticator
    val biometricAuthenticator = rememberBiometricAuthenticator()
    var isAuthenticated by remember { mutableStateOf(false) }

    if (!isAuthenticated) {
        Button(onClick = {
            biometricAuthenticator.authenticate(
                title = "Unlock",
                subtitle = "Authenticate to access secure data",
                onSuccess = {
                    isAuthenticated = true
                    // Load your secure data here
                },
                onError = { errorMessage ->
                    println("Auth failed: $errorMessage")
                }
            )
        }) {
            Text("Unlock with Biometrics")
        }
    } else {
        // Show secure content
        Text("Authenticated!")
    }
}
```

### BiometricAuthenticator Interface

```kotlin
interface BiometricAuthenticator {
    /**
     * Check if biometric authentication is available on this device
     */
    fun isAvailable(): Boolean

    /**
     * Authenticate the user with biometrics
     */
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

### Platform Implementations

#### Android (BiometricPrompt)

```kotlin
class AndroidBiometricAuthenticator(
    private val activity: FragmentActivity
) : BiometricAuthenticator {

    override fun isAvailable(): Boolean {
        val biometricManager = BiometricManager.from(activity)
        return biometricManager.canAuthenticate(
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
            BiometricManager.Authenticators.DEVICE_CREDENTIAL
        ) == BiometricManager.BIOMETRIC_SUCCESS
    }

    override fun authenticate(
        title: String,
        subtitle: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val biometricPrompt = BiometricPrompt(
            activity,
            ContextCompat.getMainExecutor(activity),
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    onSuccess()
                }
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    onError(errString.toString())
                }
            }
        )

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        biometricPrompt.authenticate(promptInfo)
    }
}

@Composable
actual fun rememberBiometricAuthenticator(): BiometricAuthenticator {
    val context = LocalContext.current
    val activity = context as FragmentActivity
    return remember { AndroidBiometricAuthenticator(activity) }
}
```

#### iOS (LocalAuthentication)

```kotlin
class IosBiometricAuthenticator : BiometricAuthenticator {

    override fun isAvailable(): Boolean {
        val context = LAContext()
        return context.canEvaluatePolicy(
            LAPolicyDeviceOwnerAuthenticationWithBiometrics,
            error = null
        )
    }

    override fun authenticate(
        title: String,
        subtitle: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val context = LAContext()
        context.evaluatePolicy(
            LAPolicyDeviceOwnerAuthenticationWithBiometrics,
            localizedReason = subtitle
        ) { success, error ->
            if (success) {
                onSuccess()
            } else {
                onError(error?.localizedDescription ?: "Authentication failed")
            }
        }
    }
}

@Composable
actual fun rememberBiometricAuthenticator(): BiometricAuthenticator {
    return remember { IosBiometricAuthenticator() }
}
```

#### JVM/Desktop (Auto-authenticate)

```kotlin
class JvmBiometricAuthenticator : BiometricAuthenticator {

    override fun isAvailable(): Boolean = true

    override fun authenticate(
        title: String,
        subtitle: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        // Desktop has no biometric hardware - auto-authenticate
        onSuccess()
    }
}

@Composable
actual fun rememberBiometricAuthenticator(): BiometricAuthenticator {
    return remember { JvmBiometricAuthenticator() }
}
```

---

## KSafe Usage Examples

### Basic Persistent State

```kotlin
class MyViewModel(val ksafe: KSafe) : ViewModel() {

    // Encrypted by default
    var counter by ksafe.mutableStateOf(0)
        private set

    // Explicitly not encrypted
    var username by ksafe.mutableStateOf(
        defaultValue = "",
        key = "username",
        encrypted = false
    )

    // Property delegation (non-compose)
    var token by ksafe("default_token")
}
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
    defaultValue = AuthInfo(),
    key = "authInfo",
    encrypted = true
)
```

### Flows

```kotlin
viewModelScope.launch {
    ksafe.getFlow<String?>(
        key = "token",
        defaultValue = null,
        encrypted = true
    ).collect { value ->
        println("Token changed: $value")
    }
}
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
    implementation("eu.anifantakis:ksafe:1.3.0-RC01")
    implementation("eu.anifantakis:ksafe-compose:1.3.0-RC01")
}

androidMain.dependencies {
    implementation("androidx.biometric:biometric:1.1.0")
}
```

---

## Project Structure

```
composeApp/src/
├── commonMain/kotlin/
│   └── eu/anifantakis/ksafe_demo/
│       ├── App.kt
│       ├── biometric/BiometricAuthenticator.kt
│       ├── di/Modules.kt
│       └── screens/counters/
│           ├── LibCounterScreen.kt
│           └── LibCounterViewModel.kt
├── androidMain/kotlin/
│   └── eu/anifantakis/ksafe_demo/
│       ├── MainActivity.kt
│       ├── biometric/BiometricAuthenticator.android.kt
│       └── di/Modules.android.kt
├── iosMain/kotlin/
│   └── eu/anifantakis/ksafe_demo/
│       ├── MainViewController.kt
│       ├── biometric/BiometricAuthenticator.ios.kt
│       └── di/Modules.ios.kt
└── jvmMain/kotlin/
    └── eu/anifantakis/ksafe_demo/
        ├── main.kt
        ├── biometric/BiometricAuthenticator.jvm.kt
        └── di/Modules.jvm.kt
```

---

## Resources

- [KSafe Library](https://github.com/ianifantakis/ksafe)
- [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform/)
