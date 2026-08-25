package com.nmichail.wordly.android.features.books.detail.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.features.books.domain.entity.BooksItem
import com.nmichail.wordly.android.shared.catalog.CatalogRemoteImage

@Composable
internal fun BookDetailSimilarCard(
	item: BooksItem,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme
	Column(
		modifier = modifier
			.clickable(
				role = Role.Button,
				onClick = onClick,
			),
		verticalArrangement = Arrangement.spacedBy(8.dp),
	) {
		val shape = RoundedCornerShape(6.dp)
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.shadow(
					elevation = 4.dp,
					shape = shape,
					clip = false,
				)
				.clip(shape)
				.aspectRatio(COVER_ASPECT_RATIO)
				.background(colorScheme.surfaceContainerHigh),
		) {
			CatalogRemoteImage(
				url = item.imageUrl,
				modifier = Modifier.fillMaxSize(),
			)
		}
		Text(
			text = item.title,
			style = MaterialTheme.typography.titleSmall,
			color = colorScheme.onBackground,
			maxLines = 2,
			overflow = TextOverflow.Ellipsis,
		)
		Text(
			text = item.author,
			style = MaterialTheme.typography.bodySmall,
			color = colorScheme.onSurfaceVariant,
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
		)
	}
}

private const val COVER_ASPECT_RATIO = 2f / 3f