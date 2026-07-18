package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.theme.WordlyTypography

private val TrainingTileHeight = 154.dp
private val TrainingTileIconSize = 54.dp

@Composable
fun TrainingTile(
	title: String,
	subtitle: String,
	icon: ImageVector,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val shape = MaterialTheme.shapes.extraLarge
	val colorScheme = MaterialTheme.colorScheme

	Box(
		modifier = modifier
			.fillMaxWidth()
			.height(TrainingTileHeight)
			.shadow(elevation = 1.dp, shape = shape, clip = false)
			.clip(shape)
			.background(colorScheme.surface)
			.clickable(onClick = onClick)
			.padding(16.dp),
	) {
		Icon(
			imageVector = icon,
			contentDescription = null,
			tint = colorScheme.primary,
			modifier = Modifier
				.align(Alignment.TopEnd)
				.size(TrainingTileIconSize),
		)
		Column(
			modifier = Modifier.align(Alignment.BottomStart),
		) {
			Text(
				text = title,
				style = WordlyTypography.trainingTileTitle,
				color = colorScheme.onSurface,
			)
			Text(
				text = subtitle,
				style = WordlyTypography.trainingTileSubtitle,
				color = colorScheme.onSurfaceVariant,
				modifier = Modifier.padding(top = 2.dp),
			)
		}
	}
}
