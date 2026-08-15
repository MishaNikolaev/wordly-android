package com.nmichail.wordly.android.features.home.ui.component

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
import com.nmichail.wordly.android.component.wui.theme.WuiTypography
import com.nmichail.wordly.android.component.wui.components.card.WuiAppCard
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.ui.tooling.preview.Preview
import com.nmichail.wordly.android.component.wui.theme.WuiTheme

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

	WuiAppCard(
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
					.size(44.dp)
					.clip(MaterialTheme.shapes.small)
					.background(colorScheme.primaryContainer),
				contentAlignment = Alignment.Center,
			) {
				Icon(
					imageVector = icon,
					contentDescription = null,
					tint = iconTint,
					modifier = Modifier.size(22.dp),
				)
			}
			Column(modifier = Modifier.weight(1f)) {
				Text(
					text = title,
					style = WuiTypography.trainingTileTitle,
					color = colorScheme.onSurface,
				)
				Text(
					text = subtitle,
					style = WuiTypography.trainingTileSubtitle,
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

@Preview(showBackground = true)
@Composable
private fun TrainingListItemPreview() {
	WuiTheme {
		TrainingListItem(
			title = "Карточки",
			subtitle = "8 слов",
			icon = Icons.AutoMirrored.Rounded.MenuBook,
			onClick = {},
			modifier = Modifier.padding(16.dp),
		)
	}
}