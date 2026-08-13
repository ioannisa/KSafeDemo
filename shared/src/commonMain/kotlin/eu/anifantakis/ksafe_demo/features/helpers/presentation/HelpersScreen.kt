package eu.anifantakis.ksafe_demo.features.helpers.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.UIConst
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppButton
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppPreview
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppText
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppTextStyle
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content.AppCodeBlock
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content.AppValueCard
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.Strings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.koin.compose.viewmodel.koinViewModel

/** One mode-typed view = one section: its three declaration styles plus their increments. */
@Immutable
data class HelperCounterSection(
    val titleKey: StringKey,
    val counter1: Int,
    val counter2: Int,
    val counter3: Int,
    val onIncrement1: () -> Unit,
    val onIncrement2: () -> Unit,
    val onIncrement3: () -> Unit,
)

@Composable
fun HelpersScreenRoot(
    viewModel: HelpersViewModel = koinViewModel(),
) {
    // Only the "2" tier needs collecting here — that is the point of the comparison:
    // tier 1 is Compose state already, tier 3 was pre-collected via toComposeState().
    val plainCounter2 by viewModel.plainCounter2.collectAsState()
    val encryptedCounter2 by viewModel.encryptedCounter2.collectAsState()
    val hardwareIsolatedCounter2 by viewModel.hardwareIsolatedCounter2.collectAsState()

    HelpersScreen(
        sections = persistentListOf(
            HelperCounterSection(
                titleKey = StringKey.HELPERS_SECTION_PLAIN,
                counter1 = viewModel.plainCounter1,
                counter2 = plainCounter2,
                counter3 = viewModel.plainCounter3.value,
                onIncrement1 = viewModel::incrementPlainCounter1,
                onIncrement2 = viewModel::incrementPlainCounter2,
                onIncrement3 = viewModel::incrementPlainCounter3,
            ),
            HelperCounterSection(
                titleKey = StringKey.HELPERS_SECTION_ENCRYPTED,
                counter1 = viewModel.encryptedCounter1,
                counter2 = encryptedCounter2,
                counter3 = viewModel.encryptedCounter3.value,
                onIncrement1 = viewModel::incrementEncryptedCounter1,
                onIncrement2 = viewModel::incrementEncryptedCounter2,
                onIncrement3 = viewModel::incrementEncryptedCounter3,
            ),
            HelperCounterSection(
                titleKey = StringKey.HELPERS_SECTION_HARDWARE_ISOLATED,
                counter1 = viewModel.hardwareIsolatedCounter1,
                counter2 = hardwareIsolatedCounter2,
                counter3 = viewModel.hardwareIsolatedCounter3.value,
                onIncrement1 = viewModel::incrementHardwareIsolatedCounter1,
                onIncrement2 = viewModel::incrementHardwareIsolatedCounter2,
                onIncrement3 = viewModel::incrementHardwareIsolatedCounter3,
            ),
        ),
    )
}

@Composable
private fun HelpersScreen(
    sections: ImmutableList<HelperCounterSection>,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                vertical = UIConst.screenVerticalPadding,
                horizontal = UIConst.screenHorizontalPadding,
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(UIConst.paddingExtraSmall),
    ) {
        AppText(
            text = Strings[StringKey.HELPERS_TITLE],
            style = AppTextStyle.SCREEN_TITLE,
            fontWeight = FontWeight.Bold,
        )
        AppText(
            text = Strings[StringKey.HELPERS_SUBTITLE],
            style = AppTextStyle.CAPTION,
        )

        AppCodeBlock(
            "// The handle IS the write mode — no mode argument anywhere\n" +
                "single { KSafePlain(get()) }\n" +
                "single { KSafeEncrypted(get()) }\n" +
                "single { KSafeHardwareIsolated(get()) }\n" +
                "\n" +
                "var plainCounter1 by ksafePlain.mutableStateOf(0)\n" +
                "val counter2 by ksafeEncrypted.asMutableStateFlow(0, scope)\n" +
                "val counter3 = flow3.toComposeState(scope) // no collectAsState()",
            modifier = Modifier.fillMaxWidth(),
        )

        sections.forEach { section ->
            Spacer(modifier = Modifier.height(UIConst.paddingExtraSmall))
            AppText(
                text = Strings[section.titleKey],
                style = AppTextStyle.EYEBROW,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(UIConst.paddingCompact),
            ) {
                AppValueCard(
                    label = "mutableStateOf",
                    sublabel = Strings[StringKey.HELPERS_VARIANT_DIRECT_STATE],
                    value = section.counter1.toString(),
                    modifier = Modifier.weight(1f),
                )
                AppValueCard(
                    label = "asMutableStateFlow",
                    sublabel = Strings[StringKey.HELPERS_VARIANT_COLLECT_AS_STATE],
                    value = section.counter2.toString(),
                    modifier = Modifier.weight(1f),
                )
                AppValueCard(
                    label = "toComposeState",
                    sublabel = Strings[StringKey.HELPERS_VARIANT_TO_COMPOSE_STATE],
                    value = section.counter3.toString(),
                    modifier = Modifier.weight(1f),
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(UIConst.paddingCompact),
            ) {
                AppButton(
                    label = "+1",
                    onClick = section.onIncrement1,
                    modifier = Modifier.weight(1f),
                    textStyle = AppTextStyle.ACTION_SMALL,
                )
                AppButton(
                    label = "+1",
                    onClick = section.onIncrement2,
                    modifier = Modifier.weight(1f),
                    textStyle = AppTextStyle.ACTION_SMALL,
                )
                AppButton(
                    label = "+1",
                    onClick = section.onIncrement3,
                    modifier = Modifier.weight(1f),
                    textStyle = AppTextStyle.ACTION_SMALL,
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewHelpersScreen() {
    AppPreview {
        HelpersScreen(
            sections = persistentListOf(
                HelperCounterSection(
                    titleKey = StringKey.HELPERS_SECTION_PLAIN,
                    counter1 = 1, counter2 = 2, counter3 = 3,
                    onIncrement1 = {}, onIncrement2 = {}, onIncrement3 = {},
                ),
                HelperCounterSection(
                    titleKey = StringKey.HELPERS_SECTION_ENCRYPTED,
                    counter1 = 4, counter2 = 5, counter3 = 6,
                    onIncrement1 = {}, onIncrement2 = {}, onIncrement3 = {},
                ),
                HelperCounterSection(
                    titleKey = StringKey.HELPERS_SECTION_HARDWARE_ISOLATED,
                    counter1 = 7, counter2 = 8, counter3 = 9,
                    onIncrement1 = {}, onIncrement2 = {}, onIncrement3 = {},
                ),
            ),
        )
    }
}
