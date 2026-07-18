package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.R
import com.nmichail.wordly.android.component.ui.theme.WordlyTypography

private val NewsImageHeight = 140.dp

@Composable
fun NewsCard(
	title: String,
	subtitle: String,
	publishedAt: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme

	AppCard(
		modifier = modifier.fillMaxWidth(),
		onClick = onClick,
		contentPadding = PaddingValues(0.dp),
	) {
		Column(modifier = Modifier.fillMaxWidth()) {
			Image(
				painter = painterResource(R.drawable.frame),
				contentDescription = null,
				modifier = Modifier
					.fillMaxWidth()
					.height(NewsImageHeight),
				contentScale = ContentScale.Crop,
			)
			Text(
				text = title,
				style = WordlyTypography.trainingTileTitle,
				color = colorScheme.onSurface,
				modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 14.dp),
			)
			Text(
				text = subtitle,
				style = WordlyTypography.trainingTileSubtitle,
				color = colorScheme.onSurfaceVariant,
				modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 4.dp),
			)
			Text(
				text = publishedAt,
				style = MaterialTheme.typography.labelSmall,
				color = colorScheme.onSurfaceVariant,
				modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
			)
		}
	}
}
