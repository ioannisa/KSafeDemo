import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

dependencies {
    implementation(projects.shared)

    implementation(compose.desktop.currentOs)
    implementation(libs.runtime)
    // Backs Dispatchers.Main on the Swing/AWT event thread.
    implementation(libs.kotlinx.coroutinesSwing)
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
