import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    // AGP 9: `com.android.library` cannot coexist with the KMP plugin. This is its
    // KMP-native replacement — it contributes an Android target inside the
    // `kotlin {}` block instead of a parallel top-level `android {}` extension.
    alias(libs.plugins.androidKmpLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)

    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    // Replaces the old `androidTarget { }` + top-level `android { }` pair. Note what
    // cannot be set here: applicationId, targetSdk, versionCode, versionName,
    // buildTypes. Those are application-only concepts and now live in :androidApp.
    // This module builds a single variant — there is no debug/release split.
    android {
        namespace = "eu.anifantakis.ksafe_demo.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }

        // Android resource processing is OFF by default in this plugin. Compose
        // Multiplatform resources (src/commonMain/composeResources) still route
        // through it on Android, and without this they are dropped silently — the
        // failure surfaces at runtime rather than at build time (CMP-9547).
        androidResources {
            enable = true
        }

        // Also opt-in, also silent when missing: without it the Android target
        // simply skips commonTest instead of running it on the host JVM.
        withHostTest {}
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            // Xcode links this framework by name (`import ComposeApp` in
            // ContentView.swift). Renaming it would mean editing the Swift sources
            // and the framework search paths, so it keeps its old name even though
            // the Gradle module is now :shared.
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    // Native macOS — the klib only. The executable that consumes it, and the
    // NSApplication bootstrap it needs, live in :macosApp.
    macosArm64()

    // Wire up the appleMain intermediate source set (shared by iOS + macOS).
    applyDefaultHierarchyTemplate()

    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
    }

    js {
        browser()
    }

    sourceSets {

        androidMain.dependencies {
            // Koin's Android context bridge — Modules.android.kt resolves the
            // Application instance through androidApplication().
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

            // `api` rather than `implementation`: KSafe is the subject of this demo
            // and its types surface in the entry-point modules — :webApp gates its
            // first frame on ksafe.awaitCacheReady() before mounting the app.
            api("eu.anifantakis:ksafe:2.1.3")
            implementation("eu.anifantakis:ksafe-compose:2.1.3")
            implementation("eu.anifantakis:ksafe-biometrics:2.1.3")
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
