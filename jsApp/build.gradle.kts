plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    js {
        // The bundle is named after this Gradle module, so the <script> tag in
        // src/jsMain/resources/index.html points at jsApp.js.
        browser()
        binaries.executable()
    }

    sourceSets {
        jsMain.dependencies {
            implementation(projects.shared)

            implementation(libs.runtime)
            implementation(libs.ui)
        }
    }
}
