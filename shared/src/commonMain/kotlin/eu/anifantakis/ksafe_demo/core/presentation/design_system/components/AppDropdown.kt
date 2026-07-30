package eu.anifantakis.ksafe_demo.core.presentation.design_system.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.StringKey
import eu.anifantakis.ksafe_demo.core.presentation.string_resources.Strings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> AppDropdown(
    selectedItem: T?,
    items: List<T>,
    itemLabel: (T) -> String,
    label: String,
    onItemSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            if (enabled) {
                expanded = it
            }
        },
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = selectedItem?.let(itemLabel).orEmpty(),
            onValueChange = {},
            modifier = Modifier
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth(),
            enabled = enabled,
            readOnly = true,
            singleLine = true,
            label = {
                AppText(
                    text = label,
                    style = AppTextStyle.BODY,
                )
            },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = {
                        AppText(
                            text = itemLabel(item),
                            style = AppTextStyle.BODY,
                        )
                    },
                    onClick = {
                        expanded = false
                        onItemSelected(item)
                    },
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun PreviewAppDropdown() {
    AppPreview {
        AppDropdown(
            selectedItem = "English",
            items = listOf("English", "Deutsch", "Italiano"),
            itemLabel = { it },
            label = Strings[StringKey.PREFERENCES_SELECT_LANGUAGE],
            onItemSelected = {},
        )
    }
}
