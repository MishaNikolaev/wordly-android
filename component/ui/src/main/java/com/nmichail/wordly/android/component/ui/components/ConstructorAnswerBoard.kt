package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.theme.WordlyColors

@Composable
fun ConstructorAnswerBoard(
	isCorrect: Boolean?,
	modifier: Modifier = Modifier,
	content: @Composable () -> Unit,
) {
	val dark = isSystemInDarkTheme()
	val borderColor = answerBoardBorderColor(isCorrect = isCorrect, dark = dark)
	val background = answerBoardBackground(isCorrect = isCorrect, dark = dark)
	val shape = RoundedCornerShape(16.dp)
	@Suppress("MagicNumber")
	val dash = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)

	Box(
		modifier = modifier
			.fillMaxWidth()
			.heightIn(min = 96.dp)
			.clip(shape)
			.background(background)
			.drawBehind {
				drawRoundRect(
					color = borderColor,
					style = Stroke(width = 2.dp.toPx(), pathEffect = dash),
					cornerRadius = CornerRadius(16.dp.toPx()),
				)
			}
			.border(0.dp, Color.Transparent, shape)
			.padding(14.dp),
	) {
		content()
	}
}

private fun answerBoardBorderColor(
	isCorrect: Boolean?,
	dark: Boolean,
): Color =
	when (isCorrect) {
		true -> if (dark) WordlyColors.DarkSuccess else WordlyColors.LightSuccess
		false -> if (dark) WordlyColors.DarkError else WordlyColors.LightError
		null -> if (dark) WordlyColors.DarkWarning else WordlyColors.LightWarning
	}

private fun answerBoardBackground(
	isCorrect: Boolean?,
	dark: Boolean,
): Color =
	when (isCorrect) {
		true -> if (dark) WordlyColors.DarkSuccessContainer else WordlyColors.LightSuccessContainer
		false -> if (dark) WordlyColors.DarkErrorContainer else WordlyColors.LightErrorContainer
		null -> if (dark) WordlyColors.DarkWarningContainer else WordlyColors.LightWarningContainer
	}