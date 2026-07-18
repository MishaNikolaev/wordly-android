package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.R

private val AvatarSize = 36.dp
private val AvatarShape = RoundedCornerShape(8.dp)

@Composable
fun NewsAuthorRow(
	author: String,
	meta: String,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme
	val logoRes = if (isSystemInDarkTheme()) {
		R.drawable.logo_dark
	} else {
		R.drawable.logo_light
	}

	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.spacedBy(10.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		Image(
			painter = painterResource(logoRes),
			contentDescription = stringResource(R.string.logo_app_name),
			modifier = Modifier
				.size(AvatarSize)
				.clip(AvatarShape),
			contentScale = ContentScale.Crop,
		)
		Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
			Text(
				text = author,
				style = MaterialTheme.typography.bodyMedium,
				fontWeight = FontWeight.SemiBold,
				color = colorScheme.onSurface,
			)
			Text(
				text = meta,
				style = MaterialTheme.typography.labelMedium,
				color = colorScheme.onSurfaceVariant,
			)
		}
	}
}
