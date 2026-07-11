package eu.anifantakis.ksafe_demo.di

import eu.anifantakis.lib.ksafe.SecurityViolation

/**
 * Singleton to hold security violations detected during KSafe initialization.
 * Since KSafe is initialized before ViewModels, we need a way to pass
 * the detected violations to the SecurityViewModel.
 */
object SecurityViolationsHolder {
    private val _violations = mutableListOf<SecurityViolation>()
    val violations: List<SecurityViolation> get() = _violations.toList()

    fun addViolation(violation: SecurityViolation) {
        if (violation !in _violations) {
            _violations.add(violation)
            println("KSafe Security Violation: ${violation.name}")
        }
    }

    fun clear() {
        _violations.clear()
    }
}
