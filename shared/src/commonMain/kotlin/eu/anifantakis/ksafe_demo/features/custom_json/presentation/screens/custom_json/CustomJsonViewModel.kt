package eu.anifantakis.ksafe_demo.features.custom_json.presentation.screens.custom_json

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import eu.anifantakis.ksafe_demo.core.presentation.global_state.BaseGlobalViewModel
import eu.anifantakis.ksafe_demo.features.custom_json.domain.model.HexColor
import eu.anifantakis.ksafe_demo.features.custom_json.domain.model.Timestamp
import eu.anifantakis.ksafe_demo.features.custom_json.domain.model.UserProfile
import eu.anifantakis.lib.ksafe.KSafe
import eu.anifantakis.lib.ksafe.KSafeWriteMode
import eu.anifantakis.lib.ksafe.compose.mutableStateOf
import kotlin.time.TimeSource

@Immutable
data class CustomJsonState(
    val nameInput: String = "",
    val profile: UserProfile = defaultUserProfile,
    val plainProfile: UserProfile = defaultUserProfile,
    val saveCount: Int = 0,
)

sealed interface CustomJsonIntent {
    data class NameChanged(val name: String) : CustomJsonIntent
    data object Save : CustomJsonIntent
    data object Clear : CustomJsonIntent
}

/** This screen has no one-time local effects. */
sealed interface CustomJsonEffect

val defaultUserProfile = UserProfile(
    name = "guest",
    createdAt = Timestamp(0L),
    favoriteColor = HexColor("#000000"),
)

private val colors = listOf(
    HexColor("#FF5733"),
    HexColor("#33B5FF"),
    HexColor("#4CAF50"),
    HexColor("#9C27B0"),
    HexColor("#FF9800"),
    HexColor("#00BCD4"),
)

@Stable
class CustomJsonViewModel(
    private val ksafe: KSafe,
) : BaseGlobalViewModel() {
    private val startMark = TimeSource.Monotonic.markNow()

    private var nameInput by mutableStateOf("")

    private var profile by ksafe.mutableStateOf(
        defaultValue = defaultUserProfile,
        key = "custom_json_profile",
        mode = KSafeWriteMode.Encrypted(),
    )

    private var plainProfile by ksafe.mutableStateOf(
        defaultValue = defaultUserProfile,
        key = "custom_json_plain_profile",
        mode = KSafeWriteMode.Plain,
    )

    private var saveCount by mutableStateOf(0)

    val state: State<CustomJsonState> = derivedStateOf {
        CustomJsonState(
            nameInput = nameInput,
            profile = profile,
            plainProfile = plainProfile,
            saveCount = saveCount,
        )
    }

    fun onAction(intent: CustomJsonIntent) {
        when (intent) {
            is CustomJsonIntent.NameChanged -> nameInput = intent.name
            CustomJsonIntent.Save -> save()
            CustomJsonIntent.Clear -> clear()
        }
    }

    private fun save() {
        val elapsed = startMark.elapsedNow().inWholeMilliseconds
        val updated = UserProfile(
            name = nameInput.ifBlank { "user_${saveCount + 1}" },
            createdAt = Timestamp(elapsed),
            favoriteColor = colors[saveCount % colors.size],
        )
        profile = updated
        plainProfile = updated
        saveCount++
    }

    private fun clear() {
        profile = defaultUserProfile
        plainProfile = defaultUserProfile
        ksafe.deleteDirect("custom_json_profile")
        ksafe.deleteDirect("custom_json_plain_profile")
        saveCount = 0
        nameInput = ""
    }
}
