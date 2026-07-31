package eu.anifantakis.ksafe_demo.features.flows.presentation.screens.flow_delegates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.AppColor
import eu.anifantakis.ksafe_demo.core.presentation.design_system.UIConst
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppButton
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppCard
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppPreview
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppProgressIndicator
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppSwitch
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppText
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppTextField
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.AppTextStyle
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content.AppCodeBlock
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content.AppCodeBlockStyle
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content.AppSectionDivider
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content.AppSectionHeader
import eu.anifantakis.ksafe_demo.core.presentation.design_system.components.content.AppValueCard
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.Strings
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.withArgs
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FlowDelegatesScreenRoot(
    viewModel: FlowDelegatesViewModel = koinViewModel(),
) {
    FlowDelegatesScreen(
        state = viewModel.state.value,
        onIntent = viewModel::onAction,
    )
}

@Composable
private fun FlowDelegatesScreen(
    state: FlowDelegatesState,
    onIntent: (FlowDelegatesIntent) -> Unit,
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
        verticalArrangement = Arrangement.spacedBy(UIConst.paddingCompact),
    ) {
        AppText(
            text = Strings[StringKey.FLOWS_TITLE],
            style = AppTextStyle.SCREEN_TITLE,
            fontWeight = FontWeight.Bold,
        )
        AppText(
            text = Strings[StringKey.FLOWS_SUBTITLE],
            style = AppTextStyle.CAPTION,
        )

        AppSectionHeader(Strings[StringKey.FLOWS_MUTABLE_STATE_FLOW_SECTION])
        AppCodeBlock(
            "// Standard: private val _state = MutableStateFlow(MoviesListState())\n" +
                "// KSafe: private val _state by kSafe.asMutableStateFlow(...)\n" +
                "// Same .update{} and .value API, persisted automatically",
            style = AppCodeBlockStyle.WARM,
        )
        AppCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = UIConst.paddingExtraSmall),
            bordered = true,
        ) {
            Column(modifier = Modifier.padding(UIConst.paddingSmall)) {
                AppText(
                    text = Strings[StringKey.FLOWS_MOVIES_STATE],
                    style = AppTextStyle.CAPTION,
                )
                Spacer(Modifier.height(UIConst.paddingExtraSmall))
                when {
                    state.movies.loading -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(UIConst.paddingSmall),
                        ) {
                            AppProgressIndicator()
                            AppText(
                                Strings[StringKey.FLOWS_LOADING_MOVIES],
                                AppTextStyle.BODY,
                            )
                        }
                    }

                    state.movies.error != null -> {
                        AppText(
                            text = Strings[StringKey.FLOWS_ERROR]
                                .withArgs(listOf(state.movies.error)),
                            style = AppTextStyle.BODY,
                            color = AppColor.Error,
                        )
                    }

                    state.movies.movies.isNotEmpty() -> {
                        state.movies.movies.forEach { movie ->
                            AppText("• $movie", AppTextStyle.BODY)
                        }
                    }

                    else -> AppText(
                        Strings[StringKey.FLOWS_NO_MOVIES_LOADED],
                        AppTextStyle.CAPTION,
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(UIConst.paddingSmall),
        ) {
            AppButton(
                label = Strings[StringKey.FLOWS_LOAD_MOVIES],
                onClick = { onIntent(FlowDelegatesIntent.LoadMovies) },
                modifier = Modifier.weight(1f),
            )
            AppButton(
                label = Strings[StringKey.COMMON_CLEAR],
                onClick = { onIntent(FlowDelegatesIntent.ClearMovies) },
                modifier = Modifier.weight(1f),
            )
        }

        AppSectionDivider()
        AppSectionHeader(Strings[StringKey.FLOWS_AS_FLOW_SECTION])
        AppCodeBlock(
            "private val _username by kSafe.asMutableStateFlow(\"Guest\", scope)\n" +
                "val toggleMode: Flow<Boolean> by kSafe.asFlow(false)\n" +
                "val themeLabel = toggleMode.map { ... }.stateIn(...)",
            style = AppCodeBlockStyle.WARM,
        )
        AppTextField(
            value = state.username,
            onValueChange = { onIntent(FlowDelegatesIntent.NameChanged(it)) },
            label = Strings[StringKey.FLOWS_USERNAME],
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                AppText(
                    Strings[StringKey.FLOWS_TOGGLE_VALUE],
                    AppTextStyle.BODY,
                )
                AppText(
                    text = Strings[StringKey.FLOWS_DERIVED_VALUE]
                        .withArgs(listOf(Strings[state.toggleLabelKey])),
                    style = AppTextStyle.CAPTION,
                    color = if (state.toggleMode) AppColor.Success else AppColor.Error,
                    fontWeight = FontWeight.Bold,
                )
            }
            AppSwitch(
                checked = state.toggleMode,
                onCheckedChange = { onIntent(FlowDelegatesIntent.ToggleMode) },
            )
        }

        AppSectionDivider()
        AppSectionHeader(Strings[StringKey.FLOWS_SCOPE_SYNC_SECTION])
        AppCodeBlock(
            "// CountersViewModel owns key \"count2\"\n" +
                "var isolated by kSafe.mutableStateOf(2000, key = \"count2\")\n" +
                "var synced by kSafe.mutableStateOf(2000, key = \"count2\", scope = viewModelScope)",
            style = AppCodeBlockStyle.WARM,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(UIConst.paddingSmall),
        ) {
            AppValueCard(
                label = Strings[StringKey.FLOWS_ISOLATED_NO_SCOPE],
                value = state.storageCountIsolated.toString(),
                modifier = Modifier.weight(1f),
            )
            AppValueCard(
                label = Strings[StringKey.FLOWS_SYNCED_WITH_SCOPE],
                value = state.storageCountSynced.toString(),
                modifier = Modifier.weight(1f),
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(UIConst.paddingSmall),
        ) {
            AppButton(
                label = Strings[StringKey.FLOWS_INCREMENT_FROM_SCREEN],
                onClick = { onIntent(FlowDelegatesIntent.IncrementStorageCounter) },
                modifier = Modifier.weight(1f),
            )
            AppButton(
                label = Strings[StringKey.FLOWS_REFRESH_ISOLATED],
                onClick = { onIntent(FlowDelegatesIntent.RefreshIsolated) },
                modifier = Modifier.weight(1f),
            )
        }
        AppText(
            text = Strings[StringKey.FLOWS_SYNC_EXPLANATION],
            style = AppTextStyle.SMALL,
            color = AppColor.Primary,
            fontWeight = FontWeight.Bold,
        )

        AppSectionDivider()
        AppButton(
            label = Strings[StringKey.FLOWS_CLEAR_ALL],
            onClick = { onIntent(FlowDelegatesIntent.ClearAll) },
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewFlowDelegatesScreen() {
    AppPreview {
        FlowDelegatesScreen(
            state = FlowDelegatesState(
                movies = MoviesListState(movies = listOf("Inception", "Interstellar")),
                username = "Ada",
                storageCountSynced = 2001,
            ),
            onIntent = {},
        )
    }
}
