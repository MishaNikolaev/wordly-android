package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nmichail.wordly.android.component.ui.theme.WordlyColors

enum class ConstructorWordChipStyle {
	Bank,
	Answer,
}

@Composable
fun ConstructorWordChip(
	text: String,
	style: ConstructorWordChipStyle,
	onClick: (() -> Unit)?,
	modifier: Modifier = Modifier,
) {
	val shape = RoundedCornerShape(12.dp)
	val colorScheme = MaterialTheme.colorScheme
	val dark = isSystemInDarkTheme()
	val clickableModifier = if (onClick != null) {
		Modifier.clickable(onClick = onClick)
	} else {
		Modifier
	}

	Text(
		text = text,
		style = MaterialTheme.typography.labelLarge.copy(
			fontWeight = FontWeight.SemiBold,
			fontSize = 15.sp,
			lineHeight = 18.sp,
		),
		color = when (style) {
			ConstructorWordChipStyle.Bank -> colorScheme.onSurface
			ConstructorWordChipStyle.Answer -> WordlyColors.LightSurface
		},
		modifier = modifier
			.clip(shape)
			.background(
				when (style) {
					ConstructorWordChipStyle.Bank ->
						if (dark) colorScheme.surfaceVariant else colorScheme.surface
					ConstructorWordChipStyle.Answer -> WordlyColors.LightOnSurface
				},
			)
			.border(
				width = 1.dp,
				color = when (style) {
					ConstructorWordChipStyle.Bank -> colorScheme.outline
					ConstructorWordChipStyle.Answer -> WordlyColors.LightOnSurface
				},
				shape = shape,
			)
			.then(clickableModifier)
			.padding(horizontal = 14.dp, vertical = 10.dp),
	)
}
