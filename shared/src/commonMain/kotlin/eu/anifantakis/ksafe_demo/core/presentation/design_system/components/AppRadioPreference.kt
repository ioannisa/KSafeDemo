package eu.anifantakis.ksafe_demo.core.presentation.design_system.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.design_system.UIConst

@Composable
fun AppRadioPreference(
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppCard(
        modifier = modifier.selectable(
            selected = selected,
            role = Role.RadioButton,
            onClick = onClick,
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(UIConst.paddingRegular),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(UIConst.paddingSmall),
        ) {
            AppRadioButton(
                selected = selected,
                onClick = null,
            )
            Column(modifier = Modifier.weight(1f)) {
                AppText(
                    text = title,
                    style = AppTextStyle.CARD_TITLE,
                    fontWeight = FontWeight.Bold,
                )
                AppText(
                    text = description,
                    style = AppTextStyle.CAPTION,
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewAppRadioPreference() {
    AppPreview {
        AppRadioPreference(
            title = "System theme",
            description = "Follow the device appearance",
            selected = true,
            onClick = {},
        )
    }
}
