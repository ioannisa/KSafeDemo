package eu.anifantakis.ksafe_demo.di

import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeConfig
import eu.anifantakis.lib.ksafe.KSafeSecurityPolicy
import eu.anifantakis.lib.ksafe.SecurityAction
import eu.anifantakis.ksafe_demo.screens.customjson.customJsonForKSafe
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Apple-platform Koin module — used by both iOS and native macOS targets.
 *
 * On iOS: jailbreak-style heuristics fire as designed; we keep them active
 *   under WARN so the demo's "violations" panel stays meaningful.
 *
 * On macOS: jailbreak heuristics short-circuit to "not rooted" inside KSafe
 *   itself (see SecurityChecker on appleMain), so this same policy works on a
 *   real Mac without spurious violations. Debugger / debug-build heuristics
 *   are still useful — they flag Xcode-launched builds.
 */
actual val platformModule: Module
    get() = module {
        single<KSafe> {
            KSafe(
                config = KSafeConfig(requireUnlockedDevice = false),
                securityPolicy = KSafeSecurityPolicy.Strict.copy(
                    debuggerAttached = SecurityAction.WARN,
                    debugBuild = SecurityAction.WARN,
                    emulator = SecurityAction.WARN,
                    onViolation = { violation ->
                        SecurityViolationsHolder.addViolation(violation)
                    }
                )
            )
        }

        // Second KSafe instance demonstrating custom JSON serialization
        // via KSafeConfig(json = ...). Introduced in KSafe 1.7.1.
        single<KSafe>(customJsonKSafe) {
            KSafe(
                fileName = "iosdata_customjson",
                config = KSafeConfig(json = customJsonForKSafe)
            )
        }
    }