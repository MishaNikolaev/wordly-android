package com.nmichail.wordly.android.features.constructor.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.nmichail.wordly.android.component.wui.components.button.WuiButton
import com.nmichail.wordly.android.features.constructor.ui.component.ConstructorAnswerBoard
import com.nmichail.wordly.android.features.constructor.ui.component.ConstructorWordChip
import com.nmichail.wordly.android.features.constructor.ui.component.ConstructorWordChipStyle
import com.nmichail.wordly.android.shared.practice.PracticeAnswerFeedback
import com.nmichail.wordly.android.component.wui.theme.WuiTypography
import com.nmichail.wordly.android.features.constructor.R
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorWord
import com.nmichail.wordly.android.features.constructor.presentation.detail.ConstructorPracticeComponent
import kotlin.math.roundToInt

private const val DRAG_CHIP_SCALE = 1.05f
private const val DRAG_CHIP_ALPHA = 0.92f

@Composable
internal fun ConstructorPracticeInProgressContent(
	state: ConstructorPracticeComponent.State.InProgress,
	component: ConstructorPracticeComponent,
	modifier: Modifier = Modifier,
) {
	val phrase = state.session.phrases[state.currentIndex]
	val totalCount = state.session.phrases.size

	Column(
		modifier = modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.statusBarsPadding()
			.navigationBarsPadding(),
	) {
		ConstructorPracticeTopBar(
			currentIndex = state.currentIndex + 1,
			totalCount = totalCount,
			onCloseClick = component::handleClose,
		)
		ConstructorPracticeScrollBody(
			state = state,
			question = phrase.question,
			author = phrase.author,
			correctPhraseText = phrase.correctOrder
				.mapNotNull { wordId -> phrase.words.firstOrNull { it.id == wordId }?.text }
				.joinToString(" "),
			component = component,
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth(),
		)
		ConstructorPracticeBottomAction(
			answerRevealed = state.checkResult != null,
			canCheck = state.answer.isNotEmpty(),
			onCheckClick = component::handleCheck,
			onContinueClick = component::handleContinue,
		)
	}
}

@Composable
private fun ConstructorPracticeScrollBody(
	state: ConstructorPracticeComponent.State.InProgress,
	question: String,
	author: String?,
	correctPhraseText: String,
	component: ConstructorPracticeComponent,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.verticalScroll(rememberScrollState())
			.padding(horizontal = 18.dp)
			.padding(top = 18.dp, bottom = 12.dp),
	) {
		ConstructorPracticeQuestion(
			themeTitle = state.session.themeTitle,
			question = question,
			author = author,
		)
		Spacer(modifier = Modifier.height(20.dp))
		ConstructorAnswerBoard(correct = state.checkResult) {
			ConstructorAnswerWords(
				words = state.answer,
				enabled = state.checkResult == null,
				onWordClick = component::handleRemoveWord,
				onMoveWord = component::handleMoveAnswerWord,
			)
		}
		state.checkResult?.let { correct ->
			Spacer(modifier = Modifier.height(14.dp))
			PracticeAnswerFeedback(
				correct = correct,
				correctText = stringResource(R.string.constructor_practice_correct),
				incorrectText = stringResource(R.string.constructor_practice_incorrect),
				correctAnswerText = correctPhraseText.takeUnless { correct },
			)
		}
		Spacer(modifier = Modifier.height(24.dp))
		ConstructorBankWords(
			words = state.bank,
			enabled = state.checkResult == null,
			onWordClick = component::handlePlaceWord,
		)
	}
}

@Composable
private fun ConstructorPracticeQuestion(
	themeTitle: String,
	question: String,
	author: String?,
) {
	Text(
		text = themeTitle,
		style = MaterialTheme.typography.labelLarge.copy(
			fontWeight = FontWeight.SemiBold,
			fontSize = 13.sp,
		),
		color = MaterialTheme.colorScheme.primary,
	)
	Spacer(modifier = Modifier.height(10.dp))
	Text(
		text = question,
		style = MaterialTheme.typography.headlineSmall.copy(
			fontWeight = FontWeight.Bold,
			fontSize = 26.sp,
			lineHeight = 32.sp,
		),
		color = MaterialTheme.colorScheme.onBackground,
	)
	author?.let {
		Spacer(modifier = Modifier.height(8.dp))
		Text(
			text = stringResource(R.string.constructor_practice_author_format, it),
			style = MaterialTheme.typography.bodyLarge,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
		)
	}
}

@Composable
private fun ConstructorPracticeTopBar(
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
				contentDescription = stringResource(R.string.constructor_practice_close),
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
			text = stringResource(
				R.string.constructor_practice_progress_format,
				currentIndex,
				totalCount,
			),
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ConstructorAnswerWords(
	words: List<ConstructorWord>,
	enabled: Boolean,
	onWordClick: (String) -> Unit,
	onMoveWord: (fromIndex: Int, toIndex: Int) -> Unit,
) {
	if (words.isEmpty()) {
		return
	}

	var draggingIndex by remember { mutableIntStateOf(-1) }
	var dragOffsetX by remember { mutableFloatStateOf(0f) }
	var dragOffsetY by remember { mutableFloatStateOf(0f) }
	var rowWidthPx by remember { mutableIntStateOf(1) }
	val density = LocalDensity.current
	val approxChipWidthPx = with(density) { 88.dp.toPx() }

	FlowRow(
		modifier = Modifier
			.fillMaxWidth()
			.onSizeChanged { rowWidthPx = it.width.coerceAtLeast(1) },
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp),
	) {
		words.forEachIndexed { index, word ->
			ConstructorAnswerWordChip(
				word = word,
				index = index,
				enabled = enabled,
				isDragging = draggingIndex == index,
				dragOffsetX = dragOffsetX,
				dragOffsetY = dragOffsetY,
				onWordClick = onWordClick,
				onDragStart = {
					draggingIndex = index
					dragOffsetX = 0f
					dragOffsetY = 0f
				},
				onDrag = { dx, dy ->
					dragOffsetX += dx
					dragOffsetY += dy
				},
				onDragEnd = {
					val chipsPerRow = (rowWidthPx / approxChipWidthPx)
						.roundToInt()
						.coerceAtLeast(1)
					val dx = (dragOffsetX / approxChipWidthPx).roundToInt()
					val dy = (dragOffsetY / approxChipWidthPx).roundToInt()
					val target = (index + dx + dy * chipsPerRow)
						.coerceIn(0, words.lastIndex)
					if (target != index) {
						onMoveWord(index, target)
					}
					draggingIndex = -1
					dragOffsetX = 0f
					dragOffsetY = 0f
				},
				onDragCancel = {
					draggingIndex = -1
					dragOffsetX = 0f
					dragOffsetY = 0f
				},
			)
		}
	}
}

@Composable
private fun ConstructorAnswerWordChip(
	word: ConstructorWord,
	index: Int,
	enabled: Boolean,
	isDragging: Boolean,
	dragOffsetX: Float,
	dragOffsetY: Float,
	onWordClick: (String) -> Unit,
	onDragStart: () -> Unit,
	onDrag: (dx: Float, dy: Float) -> Unit,
	onDragEnd: () -> Unit,
	onDragCancel: () -> Unit,
) {
	ConstructorWordChip(
		text = word.text,
		style = ConstructorWordChipStyle.Answer,
		onClick = if (enabled && !isDragging) {
			{ onWordClick(word.id) }
		} else {
			null
		},
		modifier = Modifier
			.zIndex(if (isDragging) 1f else 0f)
			.graphicsLayer {
				if (isDragging) {
					translationX = dragOffsetX
					translationY = dragOffsetY
					scaleX = DRAG_CHIP_SCALE
					scaleY = DRAG_CHIP_SCALE
					alpha = DRAG_CHIP_ALPHA
				}
			}
			.then(
				if (enabled) {
					Modifier.pointerInput(index) {
						detectDragGesturesAfterLongPress(
							onDragStart = { onDragStart() },
							onDragCancel = onDragCancel,
							onDragEnd = onDragEnd,
							onDrag = { change, dragAmount ->
								change.consume()
								onDrag(dragAmount.x, dragAmount.y)
							},
						)
					}
				} else {
					Modifier
				},
			),
	)
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ConstructorBankWords(
	words: List<ConstructorWord>,
	enabled: Boolean,
	onWordClick: (String) -> Unit,
) {
	FlowRow(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalArrangement = Arrangement.spacedBy(8.dp),
	) {
		words.forEach { word ->
			ConstructorWordChip(
				text = word.text,
				style = ConstructorWordChipStyle.Bank,
				onClick = if (enabled) {
					{ onWordClick(word.id) }
				} else {
					null
				},
			)
		}
	}
}

@Composable
private fun ConstructorPracticeBottomAction(
	answerRevealed: Boolean,
	canCheck: Boolean,
	onCheckClick: () -> Unit,
	onContinueClick: () -> Unit,
) {
	val text = if (answerRevealed) {
		stringResource(R.string.constructor_practice_continue)
	} else {
		stringResource(R.string.constructor_practice_check)
	}
	WuiButton(
		text = text,
		onClick = if (answerRevealed) onContinueClick else onCheckClick,
		enabled = answerRevealed || canCheck,
		modifier = Modifier
			.fillMaxWidth()
			.padding(horizontal = 18.dp)
			.padding(top = 8.dp, bottom = 8.dp),
	)
}