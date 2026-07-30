package eu.anifantakis.ksafe_demo.core.presentation.helper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.localized
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.localizedCompose
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.withArgs

@Immutable
sealed interface UiText {
    data class Dynamic(val value: String) : UiText

    data class Res(
        val key: StringKey,
        val args: List<Any> = emptyList(),
    ) : UiText

    @Composable
    fun asString(): String = when (this) {
        is Dynamic -> value
        is Res -> key.localizedCompose().withArgs(args)
    }

    fun resolve(): String = when (this) {
        is Dynamic -> value
        is Res -> key.localized().withArgs(args)
    }

    companion object {
        fun res(key: StringKey, vararg args: Any): UiText = Res(key, args.toList())
    }
}
