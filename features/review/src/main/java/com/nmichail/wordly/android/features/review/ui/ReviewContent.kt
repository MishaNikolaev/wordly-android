package com.nmichail.wordly.android.features.review.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.component.ui.theme.WordlyColors
import com.nmichail.wordly.android.component.ui.theme.WordlyTypography
import com.nmichail.wordly.android.features.review.R
import com.nmichail.wordly.android.features.review.domain.entity.ReviewOption
import com.nmichail.wordly.android.features.review.domain.entity.ReviewQuestion
import com.nmichail.wordly.android.features.review.presentation.ReviewComponent

private val CloseButtonShape = RoundedCornerShape(12.dp)
private val QuestionCardShape = RoundedCornerShape(24.dp)
private val OptionShape = RoundedCornerShape(16.dp)
private val TaskChipShape = RoundedCornerShape(8.dp)

@Composable
fun ReviewContent(
	component: ReviewComponent,
	modifier: Modifier = Modifier,
) {
	val state by component.model.subscribeAsState()

	Column(
		modifier = modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background),
	) {
		ReviewTopBar(
			currentIndex = state.question.currentIndex,
			totalCount = state.question.totalCount,
			onCloseClick = component::handleClose,
		)
		Column(
			modifier = Modifier
				.fillMaxSize()
				.padding(horizontal = 18.dp)
				.padding(top = 14.dp, bottom = 18.dp),
		) {
			ReviewQuestionCard(
				question = state.question,
				onPlayAudioClick = component::handlePlayAudio,
			)
			Spacer(modifier = Modifier.height(18.dp))
			ReviewOptions(
				options = state.question.options,
				selectedOptionId = state.selectedOptionId,
				onOptionClick = component::handleSelectOption,
			)
		}
	}
}

@Composable
private fun ReviewTopBar(
	currentIndex: Int,
	totalCount: Int,
	onCloseClick: () -> Unit,
) {
	val colorScheme = MaterialTheme.colorScheme
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.background(colorScheme.surface)
			.padding(horizontal = 16.dp, vertical = 10.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(14.dp),
	) {
		Box(
			modifier = Modifier
				.size(40.dp)
				.clip(CloseButtonShape)
				.background(colorScheme.surfaceVariant)
				.clickable(onClick = onCloseClick),
			contentAlignment = Alignment.Center,
		) {
			Icon(
				imageVector = Icons.Rounded.Close,
				contentDescription = stringResource(R.string.review_close_content_description),
				tint = colorScheme.onSurface,
				modifier = Modifier.size(22.dp),
			)
		}
		LinearProgressIndicator(
			progress = { currentIndex.toFloat() / totalCount.toFloat() },
			modifier = Modifier
				.weight(1f)
				.height(8.dp)
				.clip(RoundedCornerShape(percent = 50)),
			color = colorScheme.onSurfaceVariant.copy(alpha = 0.45f),
			trackColor = colorScheme.surfaceVariant,
			strokeCap = StrokeCap.Round,
		)
		Text(
			text = stringResource(R.string.review_progress_format, currentIndex, totalCount),
			style = WordlyTypography.mono.copy(
				fontWeight = FontWeight.Bold,
				fontSize = 14.sp,
				lineHeight = 14.sp,
			),
			color = colorScheme.onSurfaceVariant,
			modifier = Modifier.widthIn(min = 42.dp),
			textAlign = TextAlign.End,
		)
	}
}

@Composable
private fun ReviewQuestionCard(
	question: ReviewQuestion,
	onPlayAudioClick: () -> Unit,
) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.shadow(
				elevation = 8.dp,
				shape = QuestionCardShape,
				ambientColor = WordlyColors.OnPrimary.copy(alpha = 0.08f),
				spotColor = WordlyColors.OnPrimary.copy(alpha = 0.08f),
			)
			.clip(QuestionCardShape)
			.background(MaterialTheme.colorScheme.surface)
			.border(1.dp, ColorBorder, QuestionCardShape)
			.padding(horizontal = 23.dp)
			.padding(top = 21.dp, bottom = 27.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		ReviewTaskChip(
			label = question.taskLabel,
			modifier = Modifier.align(Alignment.Start),
		)
		Spacer(modifier = Modifier.height(14.dp))
		ReviewWord(word = question.word)
		Spacer(modifier = Modifier.height(6.dp))
		ReviewPhoneticRow(
			phonetic = question.phonetic,
			onPlayAudioClick = onPlayAudioClick,
		)
	}
}

@Composable
private fun ReviewTaskChip(
	label: String,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier
			.background(WordlyColors.LightPrimaryContainer, TaskChipShape)
			.padding(horizontal = 11.dp, vertical = 7.dp),
	) {
		Text(
			text = label,
			style = MaterialTheme.typography.labelMedium.copy(
				fontWeight = FontWeight.SemiBold,
				fontSize = 13.sp,
				lineHeight = 13.sp,
			),
			color = WordlyColors.ReviewAccent,
		)
	}
}

@Composable
private fun ReviewWord(word: String) {
	Text(
		text = word,
		style = WordlyTypography.wordCardWord.copy(
			fontSize = 38.sp,
			lineHeight = 40.sp,
			letterSpacing = (-0.76).sp,
		),
		color = MaterialTheme.colorScheme.onSurface,
		textAlign = TextAlign.Center,
	)
}

@Composable
private fun ReviewPhoneticRow(
	phonetic: String,
	onPlayAudioClick: () -> Unit,
) {
	val colorScheme = MaterialTheme.colorScheme
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(8.dp),
	) {
		Text(
			text = phonetic,
			style = WordlyTypography.wordCardPhonetic.copy(
				fontWeight = FontWeight.Medium,
				fontSize = 15.sp,
				lineHeight = 15.sp,
			),
			color = colorScheme.onSurfaceVariant,
		)
		IconButton(
			onClick = onPlayAudioClick,
			modifier = Modifier.size(24.dp),
		) {
			Icon(
				imageVector = Icons.Rounded.VolumeUp,
				contentDescription = stringResource(R.string.review_play_audio_content_description),
				tint = colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
				modifier = Modifier.size(18.dp),
			)
		}
	}
}

@Composable
private fun ReviewOptions(
	options: List<ReviewOption>,
	selectedOptionId: String?,
	onOptionClick: (String) -> Unit,
) {
	Column(
		modifier = Modifier.fillMaxWidth(),
		verticalArrangement = Arrangement.spacedBy(10.dp),
	) {
		options.forEach { option ->
			ReviewOptionButton(
				text = option.text,
				selected = option.id == selectedOptionId,
				onClick = { onOptionClick(option.id) },
			)
		}
	}
}

@Composable
private fun ReviewOptionButton(
	text: String,
	selected: Boolean,
	onClick: () -> Unit,
) {
	val colorScheme = MaterialTheme.colorScheme
	val borderColor = if (selected) {
		colorScheme.primary
	} else {
		colorScheme.outline
	}
	val background = if (selected) {
		WordlyColors.LightPrimaryContainer
	} else {
		colorScheme.surface
	}

	Box(
		modifier = Modifier
			.fillMaxWidth()
			.clip(OptionShape)
			.background(background)
			.border(1.dp, borderColor, OptionShape)
			.clickable(onClick = onClick)
			.padding(horizontal = 19.dp, vertical = 16.dp),
		contentAlignment = Alignment.CenterStart,
	) {
		Text(
			text = text,
			style = MaterialTheme.typography.labelLarge.copy(
				fontWeight = FontWeight.SemiBold,
				fontSize = 16.sp,
				lineHeight = 19.2.sp,
			),
			color = colorScheme.onSurface,
		)
	}
}

private val ColorBorder = WordlyColors.LightOutline.copy(alpha = 0.55f)
