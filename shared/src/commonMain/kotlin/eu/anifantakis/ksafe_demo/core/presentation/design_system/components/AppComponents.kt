package eu.anifantakis.ksafe_demo.core.presentation.design_system.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import eu.anifantakis.ksafe_demo.core.presentation.design_system.AppTheme
import eu.anifantakis.ksafe_demo.core.presentation.design_system.UIConst

@Composable
fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier.minimumInteractiveComponentSize(),
        enabled = enabled,
        content = content,
    )
}

@Composable
fun AppButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    textStyle: AppTextStyle = AppTextStyle.ACTION,
) {
    AppButton(onClick = onClick, modifier = modifier, enabled = enabled) {
        AppText(text = label, style = textStyle)
    }
}

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.Unspecified,
    bordered: Boolean = false,
    content: @Composable ColumnScope.() -> Unit,
) {
    val resolvedContainerColor = when {
        containerColor != Color.Unspecified -> containerColor
        bordered -> AppTheme.colors.surface
        else -> Color.Unspecified
    }
    Card(
        modifier = modifier,
        shape = if (bordered) {
            RoundedCornerShape(UIConst.cornerRadius)
        } else {
            MaterialTheme.shapes.medium
        },
        colors = if (resolvedContainerColor == Color.Unspecified) {
            CardDefaults.cardColors()
        } else {
            CardDefaults.cardColors(containerColor = resolvedContainerColor)
        },
        border = if (bordered) {
            BorderStroke(UIConst.borderWidth, AppTheme.colors.cardBorder)
        } else {
            null
        },
        content = content,
    )
}

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = { AppText(label, AppTextStyle.BODY) },
        singleLine = singleLine,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
    )
}

@Composable
fun AppSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Switch(checked = checked, onCheckedChange = onCheckedChange, modifier = modifier)
}

@Composable
fun AppRadioButton(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    RadioButton(
        selected = selected,
        onClick = onClick,
        modifier = modifier.minimumInteractiveComponentSize(),
    )
}

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

@Composable
fun AppProgressIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(modifier = modifier)
}

@Composable
fun AppDivider(modifier: Modifier = Modifier) {
    HorizontalDivider(modifier = modifier)
}

@Composable
fun AppDialog(
    title: String,
    text: String,
    confirmLabel: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    dismissLabel: String? = null,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { AppText(title, AppTextStyle.SCREEN_TITLE_LARGE) },
        text = { AppText(text, AppTextStyle.BODY) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                AppText(confirmLabel, AppTextStyle.ACTION)
            }
        },
        dismissButton = dismissLabel?.let { label ->
            {
                TextButton(onClick = onDismiss) {
                    AppText(label, AppTextStyle.ACTION)
                }
            }
        },
    )
}
