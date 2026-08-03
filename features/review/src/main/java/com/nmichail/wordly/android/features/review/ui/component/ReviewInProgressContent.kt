package com.nmichail.wordly.android.features.review.ui.component

import com.nmichail.wordly.android.component.ui.theme.WordlyTheme
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nmichail.wordly.android.component.ui.components.button.CustomButton
import com.nmichail.wordly.android.component.ui.theme.WordlyTypography
import com.nmichail.wordly.android.features.review.R
import com.nmichail.wordly.android.features.review.domain.entity.ReviewWord
import com.nmichail.wordly.android.features.review.presentation.ReviewComponent
import com.nmichail.wordly.android.shared.practice.PracticeOption
import com.nmichail.wordly.android.shared.practice.PracticeOptions

@Composable
internal fun ReviewInProgressContent(
	state: ReviewComponent.State.InProgress,
	component: ReviewComponent,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.statusBarsPadding()
			.navigationBarsPadding(),
	) {
		ReviewTopBar(
			currentIndex = state.progressIndex,
			totalCount = state.totalCount,
			onCloseClick = component::handleClose,
		)
		Column(
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth()
				.verticalScroll(rememberScrollState())
				.padding(horizontal = 18.dp)
				.padding(top = 14.dp),
		) {
			ReviewQuestionCard(
				word = state.currentWord,
				onPlayAudioClick = component::handlePlayAudio,
			)
			Spacer(modifier = Modifier.height(18.dp))
			PracticeOptions(
				options = state.currentWord.options.map { PracticeOption(id = it.id, text = it.text) },
				correctOptionId = state.currentWord.correctOptionId,
				selectedOptionId = state.selectedOptionId,
				isAnswerRevealed = state.isAnswerRevealed,
				enabled = !state.isAnswerRevealed && !state.isSubmitting,
				onOptionClick = component::handleSelectOption,
			)
		}
		if (state.isAnswerRevealed) {
			CustomButton(
				text = stringResource(R.string.review_continue),
				onClick = component::handleContinue,
				loading = state.isSubmitting,
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 18.dp)
					.padding(top = 12.dp, bottom = 8.dp),
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
				.clip(RoundedCornerShape(12.dp))
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
	word: ReviewWord,
	onPlayAudioClick: () -> Unit,
) {
	val colorScheme = MaterialTheme.colorScheme
	val extended = WordlyTheme.colors
	val borderColor = extended.outlineSoft
	val taskChipBackground = colorScheme.primaryContainer
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.shadow(
				elevation = 8.dp,
				shape = RoundedCornerShape(24.dp),
				ambientColor = extended.softShadow,
				spotColor = extended.softShadow,
			)
			.clip(RoundedCornerShape(24.dp))
			.background(MaterialTheme.colorScheme.surface)
			.border(1.dp, borderColor, RoundedCornerShape(24.dp))
			.padding(horizontal = 23.dp)
			.padding(top = 21.dp, bottom = 27.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		ReviewTaskChip(
			background = taskChipBackground,
			modifier = Modifier.align(Alignment.Start),
		)
		Spacer(modifier = Modifier.height(14.dp))
		Text(
			text = word.word,
			style = WordlyTypography.wordCardWord.copy(
				fontSize = 38.sp,
				lineHeight = 40.sp,
				letterSpacing = (-0.76).sp,
			),
			color = MaterialTheme.colorScheme.onSurface,
			textAlign = TextAlign.Center,
		)
		Spacer(modifier = Modifier.height(6.dp))
		ReviewPhoneticRow(
			phonetic = word.phonetic,
			onPlayAudioClick = onPlayAudioClick,
		)
	}
}

@Composable
private fun ReviewTaskChip(
	background: Color,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier
			.background(background, RoundedCornerShape(8.dp))
			.padding(horizontal = 11.dp, vertical = 7.dp),
	) {
		Text(
			text = stringResource(R.string.review_task_choose_translation),
			style = MaterialTheme.typography.labelMedium.copy(
				fontWeight = FontWeight.SemiBold,
				fontSize = 13.sp,
				lineHeight = 13.sp,
			),
			color = MaterialTheme.colorScheme.onPrimaryContainer,
		)
	}
}

@Composable
private fun ReviewPhoneticRow(
	phonetic: String,
	onPlayAudioClick: () -> Unit,
) {
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
			color = MaterialTheme.colorScheme.onSurfaceVariant,
		)
		IconButton(
			onClick = onPlayAudioClick,
			modifier = Modifier.size(24.dp),
		) {
			Icon(
				imageVector = Icons.Rounded.VolumeUp,
				contentDescription = stringResource(R.string.review_play_audio_content_description),
				tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
				modifier = Modifier.size(18.dp),
			)
		}
	}
}