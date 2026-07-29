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
            text = "Flow Delegates (1.8.0+)",
            style = AppTextStyle.SCREEN_TITLE,
            fontWeight = FontWeight.Bold,
        )
        AppText(
            text = "MutableStateFlow + encryption + persistence",
            style = AppTextStyle.CAPTION,
        )

        AppSectionHeader("asMutableStateFlow — drop-in MutableStateFlow")
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
                    text = "MoviesListState (persisted + encrypted)",
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
                            AppText("Loading movies...", AppTextStyle.BODY)
                        }
                    }

                    state.movies.error != null -> {
                        AppText(
                            text = "Error: ${state.movies.error}",
                            style = AppTextStyle.BODY,
                            color = AppColor.Error,
                        )
                    }

                    state.movies.movies.isNotEmpty() -> {
                        state.movies.movies.forEach { movie ->
                            AppText("• $movie", AppTextStyle.BODY)
                        }
                    }

                    else -> AppText("No movies loaded", AppTextStyle.CAPTION)
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(UIConst.paddingSmall)) {
            AppButton(
                label = "Load Movies",
                onClick = { onIntent(FlowDelegatesIntent.LoadMovies) },
            )
            AppButton(
                label = "Clear",
                onClick = { onIntent(FlowDelegatesIntent.ClearMovies) },
            )
        }

        AppSectionDivider()
        AppSectionHeader("asFlow & two-way binding")
        AppCodeBlock(
            "private val _username by kSafe.asMutableStateFlow(\"Guest\", scope)\n" +
                "val toggleMode: Flow<Boolean> by kSafe.asFlow(false)\n" +
                "val themeLabel = toggleMode.map { ... }.stateIn(...)",
            style = AppCodeBlockStyle.WARM,
        )
        AppTextField(
            value = state.username,
            onValueChange = { onIntent(FlowDelegatesIntent.NameChanged(it)) },
            label = "Username (asMutableStateFlow)",
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                AppText("Toggle Some Value (asFlow)", AppTextStyle.BODY)
                AppText(
                    text = "Derived: ${state.toggleLabel}",
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
        AppSectionHeader("mutableStateOf(scope) — cross-screen sync")
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
                label = "isolated (no scope)",
                value = state.storageCountIsolated.toString(),
                modifier = Modifier.weight(1f),
            )
            AppValueCard(
                label = "synced (with scope)",
                value = state.storageCountSynced.toString(),
                modifier = Modifier.weight(1f),
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(UIConst.paddingSmall)) {
            AppButton(
                label = "+1 from this screen",
                onClick = { onIntent(FlowDelegatesIntent.IncrementStorageCounter) },
            )
            AppButton(
                label = "Refresh isolated",
                onClick = { onIntent(FlowDelegatesIntent.RefreshIsolated) },
            )
        }
        AppText(
            text = "The scoped value updates in real time; the isolated value needs a manual refresh.",
            style = AppTextStyle.SMALL,
            color = AppColor.Primary,
            fontWeight = FontWeight.Bold,
        )

        AppSectionDivider()
        AppButton(
            label = "Clear All Flow Demos",
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
