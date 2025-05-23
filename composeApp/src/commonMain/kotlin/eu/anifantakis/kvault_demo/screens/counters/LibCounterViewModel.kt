package eu.anifantakis.kvault_demo.screens.counters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import eu.anifantakis.lib.kvault.KVault
import eu.anifantakis.lib.kvault.invoke
import eu.eu.anifantakis.lib.kvault.compose.mutableStateOf
import kotlinx.serialization.Serializable

class LibCounterViewModel(
    kvault: KVault
) : ViewModel() {

    // just a normal mutableStateOf - no persistence
    var count1 by mutableStateOf(1000)
        private set

    // mutableStateOf via KVault - with persistence
    // if key is unspecified, property name becomes the key
    // if encrypted is unspecified, it defaults to true
    var count2 by kvault.mutableStateOf(2000)
        private set

    // mutableStateOf via KVault - with persistence
    // key here is "counter3Key" and encrypted is false
    var count3 by kvault.mutableStateOf(
        defaultValue = 3000,
        key = "counter3Key",
        encrypted = false
    )
        private set

    // KVault without compose
    var count4 by kvault(10)
    var count5 by kvault(20)

    init {
        println("count 4 at startup: $count4")
        println("count 5 at startup: $count5")
    }

    fun increment() {
        println("count 4 before increment: $count4")
        println("count 5 before increment: $count5")

        count1++
        count2++
        count3++
        count4++
        count5++
    }



    // More complex example
    @Serializable
    data class AuthInfo(
        val accessToken: String = "",
        val refreshToken: String = "",
        val expiresIn: Long = 0L
    )

    // Encrypted DataStore
    var authInfo by kvault(AuthInfo())

    init {
//        authInfo = AuthInfo(
//            accessToken = "token123",
//            refreshToken = "refresh123",
//            expiresIn = 3600L
//        )

        // Access as if it was a normal variable
        // It retrieves the encrypted shared preference
        println(authInfo)

        // Deleting data
        // if you try to access the delegate again it will return default value
        //kvault.deleteDirect("authInfo")
    }
}