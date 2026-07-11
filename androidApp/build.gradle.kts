plugins {
    alias(libs.plugins.androidApplication)
    // No kotlin-android plugin: AGP 9's application plugin compiles Kotlin itself
    // (built-in Kotlin). Applying org.jetbrains.kotlin.android here would conflict.
    // The Compose compiler is still a Kotlin compiler plugin, so it is applied.
    alias(libs.plugins.composeCompiler)
}

android {
    // Distinct from :shared's namespace (…ksafe_demo.shared). AGP 9 enforces unique
    // package names across modules; a collision shows up as duplicate R classes.
    namespace = "eu.anifantakis.ksafe_demo"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "eu.anifantakis.ksafe_demo"
        minSdk = libs.versions.android.minSdk.get().toInt()
        // Explicit: AGP 9 defaults targetSdk to compileSdk when unset, rather than
        // to minSdk as it used to.
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            // Listed one per line — AGP 9 no longer expands the {a,b} brace syntax.
            excludes.add("/META-INF/AL2.0")
            excludes.add("/META-INF/LGPL2.1")
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
    implementation(projects.shared)

    // setContent / enableEdgeToEdge in MainActivity.
    implementation(libs.androidx.activity.compose)
    // Supplies the Theme.Material3.* parent used by res/values/themes.xml, and pulls
    // in AppCompat, whose AppCompatActivity MainActivity extends for biometric support.
    implementation(libs.google.material)
    implementation(libs.runtime)
    implementation(libs.ui.tooling.preview)

    debugImplementation(libs.ui.tooling)
}
