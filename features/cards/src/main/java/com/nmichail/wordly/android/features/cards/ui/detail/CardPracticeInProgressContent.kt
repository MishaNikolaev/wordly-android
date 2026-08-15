package com.nmichail.wordly.android.features.cards.ui.detail

import com.nmichail.wordly.android.component.wui.theme.Wui
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
import com.nmichail.wordly.android.component.wui.components.button.WuiButton
import com.nmichail.wordly.android.component.wui.theme.WuiTypography
import com.nmichail.wordly.android.features.cards.R
import com.nmichail.wordly.android.features.cards.domain.entity.CardPracticeWord
import com.nmichail.wordly.android.features.cards.presentation.detail.CardPracticeComponent
import com.nmichail.wordly.android.shared.practice.PracticeAnswerFeedback
import com.nmichail.wordly.android.shared.practice.PracticeOption
import com.nmichail.wordly.android.shared.practice.PracticeOptions

@Composable
internal fun CardPracticeInProgressContent(
	state: CardPracticeComponent.State.InProgress,
	component: CardPracticeComponent,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.statusBarsPadding()
			.navigationBarsPadding(),
	) {
		CardPracticeTopBar(
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
			CardPracticeQuestionCard(
				word = state.currentWord,
				onPlayAudioClick = component::handlePlayAudio,
			)
			Spacer(modifier = Modifier.height(18.dp))
			PracticeOptions(
				options = state.currentWord.options.map { PracticeOption(id = it.id, text = it.text) },
				correctOptionId = state.currentWord.correctOptionId,
				selectedOptionId = state.selectedOptionId,
				answerRevealed = state.answerRevealed,
				enabled = !state.answerRevealed,
				onOptionClick = component::handleSelectOption,
			)
			if (state.answerRevealed) {
				val correctAnswerText = state.currentWord.options
					.firstOrNull { it.id == state.currentWord.correctOptionId }
					?.text
				Spacer(modifier = Modifier.height(16.dp))
				PracticeAnswerFeedback(
					correct = state.correct,
					correctText = stringResource(R.string.card_practice_correct),
					incorrectText = stringResource(R.string.card_practice_incorrect),
					correctAnswerText = correctAnswerText.takeUnless { state.correct },
				)
			}
		}
		if (state.answerRevealed) {
			WuiButton(
				text = stringResource(R.string.card_practice_continue),
				onClick = component::handleContinue,
				modifier = Modifier
					.fillMaxWidth()
					.padding(horizontal = 18.dp)
					.padding(top = 12.dp, bottom = 8.dp),
			)
		}
	}
}

@Composable
private fun CardPracticeTopBar(
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
				contentDescription = stringResource(R.string.card_practice_close),
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
			text = stringResource(R.string.card_practice_progress_format, currentIndex, totalCount),
			style = WuiTypography.mono.copy(
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
private fun CardPracticeQuestionCard(
	word: CardPracticeWord,
	onPlayAudioClick: () -> Unit,
) {
	val colorScheme = MaterialTheme.colorScheme
	val extended = Wui.colors
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
		CardPracticeTaskChip(
			background = taskChipBackground,
			modifier = Modifier.align(Alignment.Start),
		)
		Spacer(modifier = Modifier.height(14.dp))
		Text(
			text = word.word,
			style = WuiTypography.wordCardWord.copy(
				fontSize = 38.sp,
				lineHeight = 40.sp,
				letterSpacing = (-0.76).sp,
			),
			color = MaterialTheme.colorScheme.onSurface,
			textAlign = TextAlign.Center,
		)
		Spacer(modifier = Modifier.height(6.dp))
		CardPracticePhoneticRow(
			phonetic = word.phonetic,
			onPlayAudioClick = onPlayAudioClick,
		)
	}
}

@Composable
private fun CardPracticeTaskChip(
	background: Color,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier
			.background(background, RoundedCornerShape(8.dp))
			.padding(horizontal = 11.dp, vertical = 7.dp),
	) {
		Text(
			text = stringResource(R.string.card_practice_task_choose_translation),
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
private fun CardPracticePhoneticRow(
	phonetic: String,
	onPlayAudioClick: () -> Unit,
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(8.dp),
	) {
		Text(
			text = phonetic,
			style = WuiTypography.wordCardPhonetic.copy(
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
				contentDescription = stringResource(R.string.card_practice_play_audio),
				tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
				modifier = Modifier.size(18.dp),
			)
		}
	}
}