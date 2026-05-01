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
        iosX64(),
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
        macosX64(),
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
            
            implementation("eu.anifantakis:ksafe:2.0.0")
            implementation("eu.anifantakis:ksafe-compose:2.0.0")
            implementation("eu.anifantakis:ksafe-biometrics:2.0.0")
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
        }
    }
}
