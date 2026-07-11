rootProject.name = "KSafeDemo"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        // mavenLocal() first so locally-published KSafe snapshots
        // (./gradlew :ksafe:publishToMavenLocal etc.) take precedence over
        // the released artifacts on Maven Central. Comment this out and
        // rely on mavenCentral() below for a clean against-released build.
        mavenLocal()
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

// KMP library holding every line of shared code: UI, view models, DI, and the
// expect/actual platform bindings. Each module below it is nothing but an entry
// point for one platform — AGP 9 no longer allows the Android application plugin
// to sit in the same module as the KMP plugin, and the rest of the split follows
// that same one-module-per-entry-point shape.
include(":shared")

include(":androidApp")
include(":desktopApp")
include(":webApp")
include(":jsApp")
include(":macosApp")