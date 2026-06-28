package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.R
import com.nmichail.wordly.android.component.ui.theme.WordlyColors

@Composable
fun Logo(
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier,
		verticalAlignment = Alignment.CenterVertically,
	) {
		Box(
			modifier = Modifier
				.size(40.dp)
				.clip(RoundedCornerShape(12.dp))
				.background(WordlyColors.OnPrimary),
			contentAlignment = Alignment.Center,
		) {
			Text(
				text = stringResource(R.string.logo_icon_letter),
				style = MaterialTheme.typography.titleLarge,
				fontWeight = FontWeight.Bold,
				color = WordlyColors.Primary,
			)
		}
		Text(
			text = stringResource(R.string.logo_app_name),
			style = MaterialTheme.typography.titleLarge,
			fontWeight = FontWeight.Bold,
			color = WordlyColors.OnPrimary,
			modifier = Modifier.padding(start = 12.dp),
		)
	}
}