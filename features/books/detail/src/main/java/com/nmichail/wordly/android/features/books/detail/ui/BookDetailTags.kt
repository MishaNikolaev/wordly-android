package com.nmichail.wordly.android.features.books.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun BookDetailTagsRow(
	genre: String?,
	category: String?,
	level: String?,
	modifier: Modifier = Modifier,
) {
	val tags = buildList {
		genre?.takeIf { it.isNotBlank() }?.let { add(TagKind.Genre to it) }
		category?.takeIf { it.isNotBlank() }?.let { add(TagKind.Age to it) }
		level?.takeIf { it.isNotBlank() }?.let { add(TagKind.Level to it) }
	}
	if (tags.isEmpty()) return

	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		tags.forEach { (kind, text) ->
			when (kind) {
				TagKind.Genre -> BookDetailMetaChip(text = text)
				TagKind.Age -> BookDetailAgeChip(text = text)
				TagKind.Level -> BookDetailLevelChip(text = text)
			}
		}
	}
}

private enum class TagKind {
	Genre,
	Age,
	Level,
}

@Composable
private fun BookDetailMetaChip(
	text: String,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme
	val shape = RoundedCornerShape(percent = 50)

	Text(
		text = text,
		style = MaterialTheme.typography.labelSmall,
		color = colorScheme.onSurface,
		modifier = modifier
			.clip(shape)
			.background(colorScheme.surfaceVariant)
			.padding(horizontal = 10.dp, vertical = 5.dp),
	)
}

@Composable
private fun BookDetailAgeChip(
	text: String,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme
	val shape = RoundedCornerShape(6.dp)
	val contentColor = colorScheme.onSurfaceVariant

	Text(
		text = text,
		style = MaterialTheme.typography.labelSmall,
		fontWeight = FontWeight.SemiBold,
		color = contentColor,
		modifier = modifier
			.border(
				width = 1.dp,
				color = contentColor,
				shape = shape,
			)
			.padding(horizontal = 8.dp, vertical = 4.dp),
	)
}

@Composable
private fun BookDetailLevelChip(
	text: String,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme
	val shape = RoundedCornerShape(percent = 50)

	Text(
		text = text,
		style = MaterialTheme.typography.labelSmall,
		color = colorScheme.onPrimaryContainer,
		modifier = modifier
			.clip(shape)
			.background(colorScheme.primaryContainer)
			.padding(horizontal = 10.dp, vertical = 5.dp),
	)
}