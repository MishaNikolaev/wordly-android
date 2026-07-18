package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.theme.WordlyTypography

private val TrainingIconContainerSize = 44.dp
private val TrainingIconSize = 22.dp

@Composable
fun TrainingListItem(
	title: String,
	subtitle: String,
	icon: ImageVector,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme
	val iconTint = colorScheme.onPrimaryContainer

	AppCard(
		modifier = modifier.fillMaxWidth(),
		onClick = onClick,
		contentPadding = PaddingValues(0.dp),
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 16.dp, vertical = 14.dp),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(12.dp),
		) {
			Box(
				modifier = Modifier
					.size(TrainingIconContainerSize)
					.clip(MaterialTheme.shapes.small)
					.background(colorScheme.primaryContainer),
				contentAlignment = Alignment.Center,
			) {
				Icon(
					imageVector = icon,
					contentDescription = null,
					tint = iconTint,
					modifier = Modifier.size(TrainingIconSize),
				)
			}
			Column(modifier = Modifier.weight(1f)) {
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
			Icon(
				imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
				contentDescription = null,
				tint = colorScheme.onSurfaceVariant,
				modifier = Modifier.size(24.dp),
			)
		}
	}
}
