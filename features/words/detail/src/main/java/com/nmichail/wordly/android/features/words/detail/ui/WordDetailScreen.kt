@file:Suppress("LongMethod", "TooManyFunctions")

package com.nmichail.wordly.android.features.words.detail.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.PlaylistAdd
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nmichail.wordly.android.component.ui.components.Button
import com.nmichail.wordly.android.component.ui.components.CalendarDialog
import com.nmichail.wordly.android.component.ui.theme.WordlyColors
import com.nmichail.wordly.android.component.ui.theme.WordlyTypography
import com.nmichail.wordly.android.features.words.detail.R
import com.nmichail.wordly.android.features.words.detail.presentation.WordDetailDialogState
import com.nmichail.wordly.android.features.words.domain.entity.WordExample
import com.nmichail.wordly.android.features.words.domain.entity.WordStatus

@Composable
fun WordDetailScreen(
	state: WordDetailDialogState,
	onDismiss: () -> Unit,
	onStatusChange: (WordStatus) -> Unit,
	onOpenCalendar: () -> Unit,
	onDismissCalendar: () -> Unit,
	onCalendarPreviousMonth: () -> Unit,
	onCalendarNextMonth: () -> Unit,
	onCalendarToday: () -> Unit,
	onCalendarDayClick: (Int) -> Unit,
	onConfirmAddToReview: () -> Unit,
	onPlayAudio: () -> Unit,
	modifier: Modifier = Modifier,
) {
	WordDetailBody(
		state = state,
		onDismiss = onDismiss,
		onStatusChange = onStatusChange,
		onOpenCalendar = onOpenCalendar,
		onConfirmAddToReview = onConfirmAddToReview,
		onPlayAudio = onPlayAudio,
		modifier = modifier,
	)
	state.calendar?.let { calendar ->
		CalendarDialog(
			monthTitle = calendar.monthTitle,
			days = calendar.days,
			onDismiss = onDismissCalendar,
			onTodayClick = onCalendarToday,
			onPreviousMonthClick = onCalendarPreviousMonth,
			onNextMonthClick = onCalendarNextMonth,
			onDayClick = onCalendarDayClick,
		)
	}
}

@Composable
private fun WordDetailBody(
	state: WordDetailDialogState,
	onDismiss: () -> Unit,
	onStatusChange: (WordStatus) -> Unit,
	onOpenCalendar: () -> Unit,
	onConfirmAddToReview: () -> Unit,
	onPlayAudio: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme
	Column(
		modifier = modifier
			.fillMaxSize()
			.navigationBarsPadding()
			.padding(horizontal = 16.dp)
			.padding(top = 20.dp),
		verticalArrangement = Arrangement.spacedBy(12.dp),
	) {
		Column(
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth()
				.verticalScroll(rememberScrollState()),
			verticalArrangement = Arrangement.spacedBy(12.dp),
		) {
			Box(modifier = Modifier.fillMaxWidth()) {
				Column(
					modifier = Modifier
						.fillMaxWidth()
						.padding(start = 42.dp),
					verticalArrangement = Arrangement.spacedBy(4.dp),
				) {
					Text(
						text = state.word,
						style = WordlyTypography.wordDetailWord,
						color = colorScheme.onSurface,
					)
					WordDetailPhonetic(
						phonetic = state.phonetic,
						onPlayAudio = onPlayAudio,
					)
				}
				WordDetailCloseButton(
					onDismiss = onDismiss,
					modifier = Modifier
						.align(Alignment.TopStart)
						.padding(top = 10.dp),
				)
			}
			WordDetailStatusRow(
				status = state.status,
				onStatusChange = onStatusChange,
			)
			if (!state.translation.isNullOrBlank()) {
				Text(
					text = state.translation,
					style = WordlyTypography.wordDetailTranslation,
					color = colorScheme.onSurface,
				)
			}
			if (!state.definition.isNullOrBlank()) {
				Text(
					text = stringResource(R.string.words_detail_definition_label),
					style = MaterialTheme.typography.labelLarge,
					fontWeight = FontWeight.SemiBold,
					color = colorScheme.onSurface,
				)
				Text(
					text = state.definition,
					style = MaterialTheme.typography.bodyMedium,
					color = colorScheme.onSurfaceVariant,
				)
			}
			WordDetailExamples(examples = state.examples)
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.height(IntrinsicSize.Max),
				horizontalArrangement = Arrangement.spacedBy(8.dp),
			) {
				DifficultyBox(
					difficulty = state.difficulty,
					maxDifficulty = state.maxDifficulty,
					modifier = Modifier
						.weight(1f)
						.fillMaxHeight(),
				)
				RepeatDateBox(
					label = state.repeatDateLabel,
					onClick = onOpenCalendar,
					modifier = Modifier
						.weight(1f)
						.fillMaxHeight(),
				)
			}
		}
		if (state.tags.isNotEmpty()) {
			WordDetailTags(
				tags = state.tags,
				modifier = Modifier.padding(top = 4.dp),
			)
		}
		Button(
			text = stringResource(R.string.words_detail_add_to_review),
			onClick = onConfirmAddToReview,
			enabled = !state.isSubmittingReview,
			loading = state.isSubmittingReview,
			leadingIcon = Icons.AutoMirrored.Outlined.PlaylistAdd,
			modifier = Modifier.padding(bottom = 10.dp),
		)
	}
}

@Composable
private fun WordDetailCloseButton(
	onDismiss: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme
	Box(
		modifier = modifier
			.size(34.dp)
			.clip(RoundedCornerShape(10.dp))
			.background(colorScheme.surfaceVariant)
			.clickable(role = Role.Button, onClick = onDismiss),
		contentAlignment = Alignment.Center,
	) {
		Icon(
			imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
			contentDescription = stringResource(R.string.words_detail_back),
			tint = colorScheme.onSurfaceVariant,
			modifier = Modifier.size(20.dp),
		)
	}
}

@Composable
private fun WordDetailPhonetic(
	phonetic: String?,
	onPlayAudio: () -> Unit,
) {
	if (phonetic.isNullOrBlank()) return
	val muted = if (isSystemInDarkTheme()) WordlyColors.DarkOnSurfaceVariant2 else WordlyColors.LightOnSurfaceVariant2
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(6.dp),
	) {
		Text(
			text = phonetic,
			style = WordlyTypography.addWordPhonetic,
			color = muted,
		)
		Icon(
			imageVector = Icons.AutoMirrored.Rounded.VolumeUp,
			contentDescription = stringResource(R.string.words_detail_play_audio),
			tint = muted,
			modifier = Modifier
				.size(18.dp)
				.clickable(role = Role.Button, onClick = onPlayAudio),
		)
	}
}

@Composable
private fun WordDetailStatusRow(
	status: WordStatus,
	onStatusChange: (WordStatus) -> Unit,
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(10.dp),
	) {
		Text(
			text = stringResource(R.string.words_detail_status_label),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
		)
		WordStatusDropdown(
			selected = status,
			onSelect = onStatusChange,
		)
	}
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WordDetailTags(
	tags: List<String>,
	modifier: Modifier = Modifier,
) {
	FlowRow(
		modifier = modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(6.dp),
		verticalArrangement = Arrangement.spacedBy(6.dp),
	) {
		tags.forEachIndexed { index, tag ->
			DetailTagChip(title = tag, paletteIndex = index)
		}
	}
}

@Composable
private fun WordStatusDropdown(
	selected: WordStatus,
	onSelect: (WordStatus) -> Unit,
) {
	var expanded by remember { mutableStateOf(false) }
	val colorScheme = MaterialTheme.colorScheme
	val statuses = listOf(
		WordStatus.New to stringResource(R.string.words_status_new),
		WordStatus.InProgress to stringResource(R.string.words_status_in_progress),
		WordStatus.Learned to stringResource(R.string.words_status_learned),
	)
	val selectedLabel = statuses.first { it.first == selected }.second
	val accent = statusAccentColor(selected)
	val menuShape = RoundedCornerShape(14.dp)

	Box {
		Row(
			modifier = Modifier
				.clip(RoundedCornerShape(percent = 50))
				.background(accent.copy(alpha = 0.14f))
				.clickable(role = Role.Button, onClick = { expanded = true })
				.padding(horizontal = 8.dp, vertical = 4.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(4.dp),
		) {
			Box(
				modifier = Modifier
					.size(6.dp)
					.background(accent, CircleShape),
			)
			Text(
				text = selectedLabel,
				style = MaterialTheme.typography.labelSmall,
				fontWeight = FontWeight.Medium,
				color = accent,
			)
			Icon(
				imageVector = Icons.Filled.KeyboardArrowDown,
				contentDescription = null,
				tint = accent,
				modifier = Modifier.size(14.dp),
			)
		}
		DropdownMenu(
			expanded = expanded,
			onDismissRequest = { expanded = false },
			shape = menuShape,
			containerColor = colorScheme.surface,
			tonalElevation = 0.dp,
			shadowElevation = 10.dp,
			border = BorderStroke(1.dp, colorScheme.outlineVariant),
		) {
			statuses.forEach { (status, label) ->
				val itemAccent = statusAccentColor(status)
				val isSelected = status == selected
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(horizontal = 6.dp, vertical = 2.dp)
						.clip(RoundedCornerShape(10.dp))
						.background(if (isSelected) itemAccent.copy(alpha = 0.12f) else Color.Transparent)
						.clickable(role = Role.Button) {
							expanded = false
							onSelect(status)
						}
						.padding(horizontal = 10.dp, vertical = 10.dp),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.spacedBy(8.dp),
				) {
					Box(
						modifier = Modifier
							.size(8.dp)
							.background(itemAccent, CircleShape),
					)
					Text(
						text = label,
						style = MaterialTheme.typography.bodyMedium,
						fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
						color = if (isSelected) itemAccent else colorScheme.onSurface,
					)
				}
			}
		}
	}
}

@Composable
private fun DetailTagChip(
	title: String,
	paletteIndex: Int,
) {
	val palette = detailTagPalette(paletteIndex)
	Text(
		text = "#$title",
		style = MaterialTheme.typography.labelSmall,
		fontWeight = FontWeight.Medium,
		color = palette,
		modifier = Modifier
			.clip(RoundedCornerShape(percent = 50))
			.background(palette.copy(alpha = 0.14f))
			.padding(horizontal = 8.dp, vertical = 4.dp),
	)
}

@Composable
private fun WordDetailExamples(examples: List<WordExample>) {
	if (examples.isEmpty()) return
	val colorScheme = MaterialTheme.colorScheme
	val shape = RoundedCornerShape(16.dp)
	val translationColor = if (isSystemInDarkTheme()) WordlyColors.DarkOnSurfaceVariant2 else WordlyColors.LightOnSurfaceVariant2
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.clip(shape)
			.background(colorScheme.surfaceVariant.copy(alpha = 0.55f))
			.padding(horizontal = 14.dp, vertical = 12.dp),
		verticalArrangement = Arrangement.spacedBy(12.dp),
	) {
		Text(
			text = stringResource(R.string.words_detail_examples_label),
			style = WordlyTypography.wordDetailExamplesTitle,
			color = colorScheme.onSurfaceVariant,
		)
		examples.forEach { example ->
			Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
				Text(
					text = example.text,
					style = WordlyTypography.wordDetailExample,
					color = colorScheme.onSurface,
				)
				val translation = example.translation
				if (!translation.isNullOrBlank()) {
					Text(
						text = translation,
						style = WordlyTypography.wordDetailExampleTranslation,
						color = translationColor,
					)
				}
			}
		}
	}
}

@Composable
private fun DifficultyBox(
	difficulty: Int,
	maxDifficulty: Int,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme
	val shape = RoundedCornerShape(14.dp)
	Column(
		modifier = modifier
			.clip(shape)
			.border(1.dp, colorScheme.outlineVariant, shape)
			.background(colorScheme.surface)
			.padding(horizontal = 12.dp, vertical = 10.dp),
	) {
		Row {
			Text(
				text = difficulty.toString(),
				style = MaterialTheme.typography.headlineSmall.copy(fontSize = 24.sp),
				fontWeight = FontWeight.Bold,
				color = colorScheme.onSurface,
				modifier = Modifier.alignByBaseline(),
			)
			Text(
				text = stringResource(R.string.words_detail_difficulty_max, maxDifficulty),
				style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
				fontWeight = FontWeight.Medium,
				color = colorScheme.onSurfaceVariant,
				modifier = Modifier
					.alignByBaseline()
					.padding(start = 2.dp),
			)
		}
		Spacer(modifier = Modifier.height(2.dp))
		Text(
			text = stringResource(R.string.words_detail_difficulty_label),
			style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
			color = colorScheme.onSurfaceVariant,
		)
	}
}

@Composable
private fun RepeatDateBox(
	label: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val shape = RoundedCornerShape(14.dp)
	val accent = WordlyColors.Primary
	Column(
		modifier = modifier
			.clip(shape)
			.background(WordlyColors.RepeatDateContainer)
			.border(1.dp, accent, shape)
			.clickable(role = Role.Button, onClick = onClick)
			.padding(horizontal = 12.dp, vertical = 10.dp),
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically,
		) {
			Text(
				text = label,
				style = MaterialTheme.typography.headlineSmall.copy(fontSize = 22.sp),
				fontWeight = FontWeight.Bold,
				color = MaterialTheme.colorScheme.onSurface,
			)
			Icon(
				imageVector = Icons.Outlined.CalendarMonth,
				contentDescription = null,
				tint = accent,
				modifier = Modifier.size(20.dp),
			)
		}
		Spacer(modifier = Modifier.height(2.dp))
		Text(
			text = stringResource(R.string.words_detail_repeat_label),
			style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
			color = accent,
		)
	}
}

@Composable
private fun statusAccentColor(status: WordStatus): Color {
	val dark = isSystemInDarkTheme()
	return when (status) {
		WordStatus.New -> if (dark) WordlyColors.DarkOnSurfaceVariant2 else WordlyColors.LightOnSurfaceVariant2
		WordStatus.InProgress -> if (dark) WordlyColors.DarkWarning else WordlyColors.LightWarning
		WordStatus.Learned -> if (dark) WordlyColors.DarkSuccess else WordlyColors.LightSuccess
	}
}

private fun detailTagPalette(index: Int): Color {
	val colors = listOf(
		WordlyColors.LightSuccess,
		WordlyColors.Primary,
		WordlyColors.LightSecondary,
		WordlyColors.LightWarning,
	)
	return colors[index % colors.size]
}
