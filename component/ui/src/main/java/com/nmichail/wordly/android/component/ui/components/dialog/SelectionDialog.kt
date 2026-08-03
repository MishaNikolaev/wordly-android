package com.nmichail.wordly.android.component.ui.components.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nmichail.wordly.android.component.ui.components.button.CustomButton
import com.nmichail.wordly.android.component.ui.theme.WordlyAndroidTheme
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.nmichail.wordly.android.component.ui.theme.PreviewTheme
import com.nmichail.wordly.android.component.ui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.ui.theme.WordlyPreviews

@Composable
fun SelectionDialog(
	title: String,
	options: List<String>,
	selectedOption: String,
	saveButtonText: String,
	onDismiss: () -> Unit,
	onSave: (String) -> Unit,
	cancelButtonText: String? = null,
	onCancel: (() -> Unit)? = null,
	onOptionSelected: ((String) -> Unit)? = null,
) {
	var pendingSelection by remember(selectedOption) { mutableStateOf(selectedOption) }
	val colorScheme = MaterialTheme.colorScheme

	Dialog(
		onDismissRequest = onDismiss,
		properties = DialogProperties(usePlatformDefaultWidth = false),
	) {
		Surface(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 20.dp),
			shape = MaterialTheme.shapes.large,
			color = colorScheme.surfaceContainerHigh,
		) {
			Column(
				modifier = Modifier.padding(
					horizontal = 20.dp,
					vertical = 24.dp,
				),
			) {
				Text(
					text = title,
					style = MaterialTheme.typography.titleLarge,
					fontWeight = FontWeight.Bold,
					color = colorScheme.onSurface,
				)
				SelectionDialogOptions(
					options = options,
					pendingSelection = pendingSelection,
					onSelect = { option ->
						pendingSelection = option
						onOptionSelected?.invoke(option)
					},
				)
				SelectionDialogActions(
					saveButtonText = saveButtonText,
					cancelButtonText = cancelButtonText,
					saveEnabled = pendingSelection.isNotBlank(),
					onDismiss = onCancel ?: onDismiss,
					onSave = { onSave(pendingSelection) },
				)
			}
		}
	}
}

@Composable
private fun SelectionDialogOptions(
	options: List<String>,
	pendingSelection: String,
	onSelect: (String) -> Unit,
) {
	Column(modifier = Modifier.padding(top = 16.dp)) {
		options.forEach { option ->
			SelectionDialogOptionRow(
				text = option,
				selected = pendingSelection == option,
				onClick = { onSelect(option) },
			)
		}
	}
}

@Composable
private fun SelectionDialogActions(
	saveButtonText: String,
	cancelButtonText: String?,
	saveEnabled: Boolean,
	onDismiss: () -> Unit,
	onSave: () -> Unit,
) {
	if (cancelButtonText != null) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 24.dp),
			horizontalArrangement = Arrangement.End,
			verticalAlignment = Alignment.CenterVertically,
		) {
			TextButton(onClick = onDismiss) {
				Text(text = cancelButtonText)
			}
			TextButton(
				onClick = onSave,
				enabled = saveEnabled,
			) {
				Text(text = saveButtonText)
			}
		}
	} else {
		CustomButton(
			text = saveButtonText,
			onClick = onSave,
			enabled = saveEnabled,
			modifier = Modifier.padding(top = 24.dp),
		)
	}
}

@Composable
private fun SelectionDialogOptionRow(
	text: String,
	selected: Boolean,
	onClick: () -> Unit,
) {
	val colorScheme = MaterialTheme.colorScheme

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.clip(MaterialTheme.shapes.small)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			)
			.padding(vertical = 12.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		SelectionDialogRadioIndicator(
			selected = selected,
			selectedColor = colorScheme.primary,
			unselectedBorderColor = colorScheme.outline,
		)
		Text(
			text = text,
			style = MaterialTheme.typography.bodyLarge,
			color = colorScheme.onSurface,
			modifier = Modifier.padding(start = 12.dp),
		)
	}
}

@Composable
private fun SelectionDialogRadioIndicator(
	selected: Boolean,
	selectedColor: Color,
	unselectedBorderColor: Color,
) {
	Box(
		modifier = Modifier
			.size(20.dp)
			.border(
				width = 2.dp,
				color = if (selected) selectedColor else unselectedBorderColor,
				shape = CircleShape,
			),
		contentAlignment = Alignment.Center,
	) {
		if (selected) {
			Box(
				modifier = Modifier
					.size(10.dp)
					.clip(CircleShape)
					.background(selectedColor),
			)
		}
	}
}

@WordlyPreviews
@Composable
private fun SelectionDialogPreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WordlyAndroidTheme(darkTheme = theme == PreviewTheme.Dark) {
		SelectionDialog(
			title = "Уровень",
			options = listOf("A1", "A2", "B1", "B2"),
			selectedOption = "B1",
			saveButtonText = "Сохранить",
			cancelButtonText = "Отмена",
			onDismiss = {},
			onSave = {},
			onCancel = {},
		)
	}
}
