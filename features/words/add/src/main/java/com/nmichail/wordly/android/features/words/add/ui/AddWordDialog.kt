package com.nmichail.wordly.android.features.words.add.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.wui.theme.WuiTypography
import com.nmichail.wordly.android.features.words.add.R
import com.nmichail.wordly.android.features.words.add.presentation.AddWordDialogState
import com.nmichail.wordly.android.features.words.domain.entity.WordExample

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddWordDialog(
	state: AddWordDialogState,
	onDismiss: () -> Unit,
	onWordInputChange: (String) -> Unit,
	onToggleTag: (String) -> Unit,
	onConfirm: () -> Unit,
) {
	val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
	ModalBottomSheet(
		onDismissRequest = onDismiss,
		sheetState = sheetState,
		shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
		containerColor = MaterialTheme.colorScheme.surface,
		dragHandle = { AddWordSheetHandle() },
	) {
		AddWordDialogBody(
			state = state,
			onDismiss = onDismiss,
			onWordInputChange = onWordInputChange,
			onToggleTag = onToggleTag,
			onConfirm = onConfirm,
		)
	}
}

@Composable
private fun AddWordDialogBody(
	state: AddWordDialogState,
	onDismiss: () -> Unit,
	onWordInputChange: (String) -> Unit,
	onToggleTag: (String) -> Unit,
	onConfirm: () -> Unit,
) {
	val focusManager = LocalFocusManager.current
	val keyboardController = LocalSoftwareKeyboardController.current

	LaunchedEffect(state.lookingUp) {
		if (!state.lookingUp && hasLookupContent(state)) {
			focusManager.clearFocus(force = true)
			keyboardController?.hide()
		}
	}

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.navigationBarsPadding()
			.padding(horizontal = 20.dp)
			.padding(top = 8.dp, bottom = 20.dp),
		verticalArrangement = Arrangement.spacedBy(14.dp),
	) {
		AddWordDialogHeader(onDismiss = onDismiss)
		AddWordInput(
			value = state.wordInput,
			onValueChange = onWordInputChange,
		)
		when {
			state.lookingUp -> Text(
				text = stringResource(R.string.words_looking_up),
				style = WuiTypography.addWordAutofillLabel,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
			)
			state.wordInput.isNotBlank() && hasLookupContent(state) -> {
				AddWordAutofillCard(
					phonetic = state.phonetic,
					examples = state.examples,
				)
				AddWordTags(
					tags = state.availableTags,
					selectedTagIds = state.selectedTagIds,
					onToggleTag = onToggleTag,
				)
			}
		}
		AddToDictionaryButton(
			onClick = onConfirm,
			enabled = state.wordInput.isNotBlank() && !state.lookingUp && !state.submitting,
			loading = state.submitting,
		)
	}
}

private fun hasLookupContent(state: AddWordDialogState): Boolean =
	!state.phonetic.isNullOrBlank() ||
		state.examples.isNotEmpty() ||
		!state.definition.isNullOrBlank() ||
		!state.translation.isNullOrBlank()

@Composable
private fun AddWordAutofillCard(
	phonetic: String?,
	examples: List<WordExample>,
) {
	val colorScheme = MaterialTheme.colorScheme
	val shape = RoundedCornerShape(16.dp)

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.border(1.dp, colorScheme.outlineVariant, shape)
			.clip(shape)
			.background(colorScheme.surface)
			.padding(16.dp),
		verticalArrangement = Arrangement.spacedBy(12.dp),
	) {
		AddWordAutofillBadge()
		if (!phonetic.isNullOrBlank()) {
			Text(
				text = phonetic,
				style = WuiTypography.addWordPhonetic,
				color = colorScheme.onSurfaceVariant,
			)
		}
		HorizontalDivider(color = colorScheme.outlineVariant)
		AddWordAutofillExamples(examples = examples)
	}
}

@Composable
private fun AddWordAutofillBadge() {
	val colorScheme = MaterialTheme.colorScheme
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(6.dp),
	) {
		Icon(
			imageVector = Icons.Outlined.AutoAwesome,
			contentDescription = null,
			tint = MaterialTheme.colorScheme.secondary,
			modifier = Modifier.size(16.dp),
		)
		Text(
			text = stringResource(R.string.words_add_autofilled),
			style = WuiTypography.addWordAutofillLabel,
			color = colorScheme.onSurfaceVariant,
		)
	}
}

@Composable
private fun AddWordAutofillExamples(examples: List<WordExample>) {
	val colorScheme = MaterialTheme.colorScheme
	Text(
		text = stringResource(R.string.words_add_examples_label),
		style = WuiTypography.addWordExamplesTitle,
		color = colorScheme.onSurfaceVariant,
	)
	if (examples.isEmpty()) {
		Text(
			text = "—",
			style = WuiTypography.addWordExample,
			color = colorScheme.onSurfaceVariant,
		)
		return
	}
	Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
		examples.forEach { example ->
			Text(
				text = example.text,
				style = WuiTypography.addWordExample,
				color = colorScheme.onSurface,
			)
		}
	}
}

@Composable
private fun AddWordSheetHandle() {
	Box(
		modifier = Modifier
			.padding(top = 10.dp)
			.size(width = 38.dp, height = 4.dp)
			.background(
				color = MaterialTheme.colorScheme.outlineVariant,
				shape = RoundedCornerShape(percent = 50),
			),
	)
}

@Composable
private fun AddWordDialogHeader(onDismiss: () -> Unit) {
	val colorScheme = MaterialTheme.colorScheme
	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically,
	) {
		Text(
			text = stringResource(R.string.words_add_title),
			style = WuiTypography.addWordTitle,
			color = colorScheme.onSurface,
		)
		Box(
			modifier = Modifier
				.size(34.dp)
				.clip(RoundedCornerShape(10.dp))
				.background(colorScheme.surfaceVariant)
				.clickable(role = Role.Button, onClick = onDismiss),
			contentAlignment = Alignment.Center,
		) {
			Icon(
				imageVector = Icons.Rounded.Close,
				contentDescription = stringResource(R.string.words_add_close),
				tint = colorScheme.onSurfaceVariant,
				modifier = Modifier.size(20.dp),
			)
		}
	}
}

@Composable
private fun AddWordInput(
	value: String,
	onValueChange: (String) -> Unit,
) {
	val colorScheme = MaterialTheme.colorScheme
	val shape = RoundedCornerShape(16.dp)

	Column(
		modifier = Modifier.fillMaxWidth(),
		verticalArrangement = Arrangement.spacedBy(6.dp),
	) {
		Text(
			text = stringResource(R.string.words_add_word_label),
			style = MaterialTheme.typography.bodyMedium,
			color = colorScheme.onSurfaceVariant,
		)
		BasicTextField(
			value = value,
			onValueChange = onValueChange,
			singleLine = true,
			cursorBrush = SolidColor(colorScheme.primary),
			textStyle = WuiTypography.addWordInput.copy(color = colorScheme.onSurface),
			decorationBox = { inner ->
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.border(1.dp, colorScheme.outlineVariant, shape)
						.clip(shape)
						.padding(horizontal = 16.dp, vertical = 14.dp),
					contentAlignment = Alignment.CenterStart,
				) {
					inner()
				}
			},
			modifier = Modifier.fillMaxWidth(),
		)
	}
}