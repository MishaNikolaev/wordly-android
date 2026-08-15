package com.nmichail.wordly.android.component.wui.components.field

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.wui.R
import com.nmichail.wordly.android.component.wui.components.dialog.WuiSelectionDialog
import com.nmichail.wordly.android.component.wui.theme.WuiTheme
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.nmichail.wordly.android.component.wui.theme.PreviewTheme
import com.nmichail.wordly.android.component.wui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.wui.theme.WuiPreviews

@Composable
fun WuiSelectionField(
	label: String,
	value: String,
	options: List<String>,
	onValueChange: (String) -> Unit,
	modifier: Modifier = Modifier,
	errorVisible: Boolean = false,
	errorMessage: String = "",
) {
	var showDialog by remember { mutableStateOf(false) }

	Column(modifier = modifier.fillMaxWidth()) {
		Text(
			text = label,
			style = MaterialTheme.typography.labelMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			modifier = Modifier.padding(bottom = 8.dp),
		)
		SelectionFieldInput(
			value = value,
			errorVisible = errorVisible,
			errorMessage = errorMessage,
			onClick = { showDialog = true },
		)
	}

	if (showDialog) {
		WuiSelectionDialog(
			title = label,
			options = options,
			selectedOption = value,
			saveButtonText = stringResource(R.string.common_save),
			onDismiss = { showDialog = false },
			onSave = { selected ->
				onValueChange(selected)
				showDialog = false
			},
		)
	}
}

@Composable
private fun SelectionFieldInput(
	value: String,
	errorVisible: Boolean,
	errorMessage: String,
	onClick: () -> Unit,
) {
	Box(modifier = Modifier.fillMaxWidth()) {
		OutlinedTextField(
			value = value,
			onValueChange = {},
			readOnly = true,
			modifier = Modifier.fillMaxWidth(),
			isError = errorVisible,
			supportingText = fieldErrorText(errorMessage),
			trailingIcon = {
				Icon(
					imageVector = Icons.Filled.KeyboardArrowDown,
					contentDescription = null,
					tint = MaterialTheme.colorScheme.onSurfaceVariant,
				)
			},
			shape = MaterialTheme.shapes.extraSmall,
			colors = selectionFieldColors(),
		)
		Box(
			modifier = Modifier
				.matchParentSize()
				.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = null,
					onClick = onClick,
				),
		)
	}
}

@Composable
private fun fieldErrorText(errorMessage: String): (@Composable () -> Unit)? =
	if (errorMessage.isNotEmpty()) {
		{
			Text(
				text = errorMessage,
				color = MaterialTheme.colorScheme.error,
			)
		}
	} else {
		null
	}

@Composable
private fun selectionFieldColors() =
	OutlinedTextFieldDefaults.colors(
		focusedContainerColor = MaterialTheme.colorScheme.surface,
		unfocusedContainerColor = MaterialTheme.colorScheme.surface,
		disabledContainerColor = MaterialTheme.colorScheme.surface,
		focusedBorderColor = MaterialTheme.colorScheme.primary,
		unfocusedBorderColor = MaterialTheme.colorScheme.outline,
		errorBorderColor = MaterialTheme.colorScheme.error,
		errorSupportingTextColor = MaterialTheme.colorScheme.error,
	)

@WuiPreviews
@Composable
private fun SelectionFieldPreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WuiTheme(darkTheme = theme == PreviewTheme.Dark) {
		WuiSelectionField(
			label = "Уровень",
			value = "B2",
			options = listOf("A1", "A2", "B1", "B2"),
			onValueChange = {},
			modifier = Modifier.padding(16.dp),
		)
	}
}