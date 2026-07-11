import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        // The bundle is named after this Gradle module, so the <script> tag in
        // src/wasmJsMain/resources/index.html points at webApp.js.
        browser()
        binaries.executable()
    }

    sourceSets {
        wasmJsMain.dependencies {
            implementation(projects.shared)

            implementation(libs.runtime)
            implementation(libs.ui)
            // main() builds its own KoinApplication so it can await the KSafe cache
            // before the first frame — see src/wasmJsMain/.../main.kt.
            implementation(libs.koin.compose)
        }
    }
}
