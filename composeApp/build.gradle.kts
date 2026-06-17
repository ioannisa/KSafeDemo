import com.android.build.api.dsl.ApplicationExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)

    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    // Native macOS — produces a self-contained executable that exercises
    // KSafe's macosArm64 / macosX64 targets (Keychain + CryptoKit) end-to-end.
    // The JVM/Desktop target also runs on macOS, but uses the JDK-flavoured
    // KSafe path. This native target is what proves the appleMain migration
    // works in a real CMP app.
    listOf(
        macosArm64(),
    ).forEach { macTarget ->
        macTarget.binaries.executable {
            entryPoint = "eu.anifantakis.ksafe_demo.main"
        }
    }

    // Wire up the appleMain intermediate source set (shared by iOS + macOS).
    applyDefaultHierarchyTemplate()

    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    js {
        browser()
        binaries.executable()
    }

    sourceSets {

        androidMain.dependencies {
            implementation(libs.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.google.material)

            // DI
            implementation(libs.koin.android)
            implementation(libs.koin.androidx.compose)
        }
        commonMain.dependencies {
            implementation(libs.runtime)
            implementation(libs.foundation)
            implementation(libs.material3)
            implementation(libs.ui)
            implementation(libs.components.resources)
            implementation(libs.ui.tooling.preview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // DI
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            api(libs.koin.core)

            // Immutable collections for better compose stability
            implementation(libs.kotlinx.collections.immutable)
            
            implementation("eu.anifantakis:ksafe:2.1.3")
            implementation("eu.anifantakis:ksafe-compose:2.1.3")
            implementation("eu.anifantakis:ksafe-biometrics:2.1.3")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

configure<ApplicationExtension> {
    namespace = "eu.anifantakis.ksafe_demo"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "eu.anifantakis.ksafe_demo"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    debugImplementation(libs.ui.tooling)
}

compose.desktop {
    application {
        mainClass = "eu.anifantakis.ksafe_demo.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "eu.anifantakis.ksafe_demo"
            packageVersion = "1.0.0"

            // STRONGLY RECOMMENDED for Compose Desktop release distributables —
            // restores OS-backed key custody. Both DataStore's protobuf and JNA
            // (the OS keyvault) need sun.misc.Unsafe (module jdk.unsupported),
            // which jlink can't detect statically. WITHOUT it (KSafe 2.1.1+) the
            // app does NOT crash: it persists through the same DataStore engine +
            // AES-256-GCM with the key in a 0700 file (the SOFTWARE tier); the
            // Security screen shows SOFTWARE and a one-time "KSafe NOTICE" prints.
            // Re-add the line and KSafe migrates that data forward automatically.
            //
            // java.management — only because this demo uses KSafeSecurityPolicy.WarnOnly
            // (its debugger probe reads java.lang.management). Default policy → omit.
            //
            // To SEE the fallback: comment this line out, run runReleaseDistributable.
            // Background + the key-file risk: KSafe docs/JVM_PROTECTION.md, issue #32.
            modules("jdk.unsupported", "java.management")
        }
    }
}

// Ad-hoc code-sign the macOS native kexe after linking. Without a stable code
// signature the system Keychain treats every launch as a new caller and prompts
// for the login keychain password on each item — once signed, "Always Allow"
// can stick across reruns of the same build. New builds may invalidate the ACL
// when the cdhash changes; sign with a real Developer ID identity if you want
// the allow decision to survive rebuilds.
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink>().configureEach {
    val targetName = binary.target.name
    val isMacExecutable = binary.outputKind == org.jetbrains.kotlin.gradle.plugin.mpp.NativeOutputKind.EXECUTABLE &&
        targetName == "macosArm64"
    if (!isMacExecutable) return@configureEach

    val outputFile = binary.outputFile
    doLast {
        val proc = ProcessBuilder("codesign", "--force", "--sign", "-", outputFile.absolutePath)
            .inheritIO()
            .start()
        val rc = proc.waitFor()
        if (rc != 0) error("codesign failed with exit code $rc for ${outputFile.absolutePath}")
    }
}
