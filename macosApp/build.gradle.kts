plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    macosArm64 {
        binaries.executable {
            entryPoint = "eu.anifantakis.ksafe_demo.main"
        }
    }

    sourceSets {
        macosMain.dependencies {
            implementation(projects.shared)

            implementation(libs.runtime)
            implementation(libs.ui)
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
    val isMacExecutable =
        binary.outputKind == org.jetbrains.kotlin.gradle.plugin.mpp.NativeOutputKind.EXECUTABLE &&
            binary.target.name == "macosArm64"
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
