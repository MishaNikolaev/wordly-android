package com.nmichail.wordly.android.component.ui.components

import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import com.nmichail.wordly.android.component.ui.R
import com.nmichail.wordly.android.component.ui.theme.WordlyTypography

@Composable
fun CatalogCard(
	title: String,
	subtitle: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	badge: String? = null,
	imageUrl: String? = null,
) {
	val colorScheme = MaterialTheme.colorScheme

	AppCard(
		modifier = modifier.fillMaxWidth(),
		onClick = onClick,
		contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(12.dp),
		) {
			CatalogCardImage(imageUrl = imageUrl)
			Column(modifier = Modifier.weight(1f)) {
				Text(
					text = title,
					style = WordlyTypography.trainingTileTitle,
					color = colorScheme.onSurface,
				)
				Row(
					modifier = Modifier.padding(top = 4.dp),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.spacedBy(8.dp),
				) {
					Text(
						text = subtitle,
						style = WordlyTypography.trainingTileSubtitle,
						color = colorScheme.onSurfaceVariant,
					)
					if (!badge.isNullOrBlank()) {
						StatusChip(
							text = badge,
							style = StatusChipStyle.Accent,
						)
					}
				}
			}
			Icon(
				imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
				contentDescription = null,
				tint = colorScheme.onSurfaceVariant,
				modifier = Modifier.size(22.dp),
			)
		}
	}
}

@Composable
private fun CatalogCardImage(
	imageUrl: String?,
	modifier: Modifier = Modifier,
) {
	val imageModifier = modifier
		.size(52.dp)
		.clip(MaterialTheme.shapes.medium)

	if (imageUrl.isNullOrBlank()) {
		Image(
			painter = painterResource(R.drawable.frame),
			contentDescription = null,
			modifier = imageModifier,
			contentScale = ContentScale.Crop,
		)
	} else {
		// TODO заменить на Glide
		AndroidView(
			factory = { context ->
				ImageView(context).apply {
					scaleType = ImageView.ScaleType.CENTER_CROP
				}
			},
			modifier = imageModifier,
			update = { imageView ->
				Glide.with(imageView)
					.load(imageUrl)
					.placeholder(R.drawable.frame)
					.error(R.drawable.frame)
					.into(imageView)
			},
		)
	}
}