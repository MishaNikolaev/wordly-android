package com.nmichail.wordly.android.features.books.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.features.books.domain.entity.BooksItem
import com.nmichail.wordly.android.shared.catalog.CatalogRemoteImage

@Composable
fun BooksListItem(
	item: BooksItem,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.clickable(
				role = Role.Button,
				onClick = onClick,
			)
			.padding(vertical = 10.dp),
		horizontalArrangement = Arrangement.spacedBy(14.dp),
	) {
		BooksListCover(imageUrl = item.imageUrl)
		Column(modifier = Modifier.weight(1f)) {
			BooksListTextBlock(
				title = item.title,
				author = item.author,
			)
			Spacer(modifier = Modifier.height(12.dp))
			BooksListMetaRow(
				genre = item.genre,
				category = item.category,
				level = item.badge,
			)
		}
	}
}

@Composable
private fun BooksListCover(
	imageUrl: String?,
	modifier: Modifier = Modifier,
) {
	val shape = RoundedCornerShape(6.dp)
	Box(
		modifier = modifier
			.width(88.dp)
			.aspectRatio(COVER_ASPECT_RATIO)
			.shadow(
				elevation = 6.dp,
				shape = shape,
				clip = false,
			)
			.clip(shape)
			.background(MaterialTheme.colorScheme.surfaceContainerHigh),
	) {
		CatalogRemoteImage(
			url = imageUrl,
			modifier = Modifier.fillMaxSize(),
		)
	}
}

@Composable
private fun BooksListTextBlock(
	title: String,
	author: String,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme
	Column(modifier = modifier) {
		Text(
			text = title,
			style = MaterialTheme.typography.titleMedium,
			color = colorScheme.onBackground,
			maxLines = 3,
			overflow = TextOverflow.Ellipsis,
		)
		Text(
			text = author,
			style = MaterialTheme.typography.bodyMedium,
			color = colorScheme.onSurfaceVariant,
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			modifier = Modifier.padding(top = 6.dp),
		)
	}
}

@Composable
private fun BooksListMetaRow(
	genre: String?,
	category: String?,
	level: String?,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier,
		verticalArrangement = Arrangement.spacedBy(8.dp),
	) {
		val hasPrimaryTags = !genre.isNullOrBlank() || !category.isNullOrBlank()
		if (hasPrimaryTags) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.spacedBy(8.dp),
			) {
				if (!genre.isNullOrBlank()) {
					BooksMetaChip(text = genre)
				}
				if (!category.isNullOrBlank()) {
					BooksAgeChip(text = category)
				}
			}
		}
		if (!level.isNullOrBlank()) {
			BooksLevelChip(text = level)
		}
	}
}

@Composable
private fun BooksMetaChip(
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
private fun BooksAgeChip(
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
private fun BooksLevelChip(
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

private const val COVER_ASPECT_RATIO = 2f / 3f