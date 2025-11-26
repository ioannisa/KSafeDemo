package eu.anifantakis.ksafe_demo.screens.counters

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.invoke
import eu.anifantakis.lib.ksafe.compose.mutableStateOf
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

class LibCounterViewModel(
    val ksafe: KSafe
) : ViewModel() {

    // just a normal mutableStateOf - no persistence
    var count1 by mutableStateOf(1000)
        private set

    // mutableStateOf via KSafe - with persistence
    // if key is unspecified, property name becomes the key
    // if encrypted is unspecified, it defaults to true
    var count2 by ksafe.mutableStateOf(2000)
        private set

    // mutableStateOf via KSafe - with persistence
    // key here is "counter3Key" and encrypted is false
    var count3 by ksafe.mutableStateOf(
        defaultValue = 3000,
        key = "counter3Key",
        encrypted = false
    )
        private set


    // KSafe without compose (regular variables, not states)
    // see console for output
    var count4 by ksafe(10)
    var count5 by ksafe(20)

    // encrypted string
    var count6 by ksafe("30")
    // unencrypted string
    var count7 by ksafe("40", encrypted = false)

    init {
        println("count 4 at startup: $count4")
        println("count 5 at startup: $count5")
        println("count 6 at startup: $count6")
        println("count 7 at startup: $count7")

        checkFlows()
    }

    fun increment() {
        println("count 4 before increment: $count4")
        println("count 5 before increment: $count5")
        println("count 6 before increment: $count6")
        println("count 7 before increment: $count7")

        count1++
        count2++
        count3++
        count4++
        count5++
        count6 = (count6.toInt()+1).toString()
        ksafe.putDirect("count7", count7.toInt() + 1)

        authInfo = authInfo.copy(
            expiresIn = authInfo.expiresIn + 1,
            accessToken = "abc_${authInfo.expiresIn + 1}",
            refreshToken = "def_${authInfo.expiresIn + 1}"
        )
    }

    fun clear() {
        // use deleteDirect to delete outside coroutines
        ksafe.deleteDirect("count1") // count 1 is normal mutableStateOf (not ksafe) deleting an non-existent key doesn't break the app
        ksafe.deleteDirect("count2")

        // or use delete for coroutines usage
        viewModelScope.launch {
            ksafe.delete("counter3Key")
            ksafe.delete("count4")
            ksafe.delete("count5")
            ksafe.delete("count6")
            ksafe.delete("count7")
            ksafe.delete("authInfo")
        }
    }


    // More complex example with data class and Serialization
    @Serializable
    data class AuthInfo(
        val accessToken: String = "",
        val refreshToken: String = "",
        val expiresIn: Long = 0L
    )

    // initialize the data class as a state so we watch for changes on the screen directly
    var authInfo by ksafe.mutableStateOf(
        defaultValue = AuthInfo(
            accessToken = "abc",
            refreshToken = "def",
            expiresIn = 3600L
        ),
        key = "authInfo",
        encrypted = true
    )

    private fun checkFlows() {
        val accessTokenKey = "access-token"

        viewModelScope.launch {
            launch {
                ksafe.getFlow<String?>(
                    key = accessTokenKey,
                    defaultValue = null,
                    encrypted = true,
                ).collect { value ->
                    println("KSafe flow - Current value: $value")
                }
            }

            // add value to flow
            ksafe.put(
                key = accessTokenKey,
                value = "some-value-1",
                encrypted = true,
            )

            // add another value to flow
            ksafe.put(
                key = accessTokenKey,
                value = "some-value-2",
                encrypted = true,
            )

            // delete value from flow
            ksafe.delete(key = accessTokenKey)
        }
    }
}