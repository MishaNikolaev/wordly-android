package com.nmichail.wordly.android.features.words.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nmichail.wordly.android.component.ui.theme.WordlyColors
import com.nmichail.wordly.android.component.ui.theme.WordlyTypography
import com.nmichail.wordly.android.features.words.R
import com.nmichail.wordly.android.features.words.domain.entity.WordItem
import com.nmichail.wordly.android.features.words.domain.entity.WordStatus

@Composable
internal fun WordListItem(
	item: WordItem,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme

	Row(
		modifier = modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(16.dp))
			.background(colorScheme.surface)
			.clickable(onClick = onClick)
			.padding(horizontal = 14.dp, vertical = 14.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		Box(
			modifier = Modifier
				.size(8.dp)
				.background(wordStatusColor(item.status), CircleShape),
		)
		Column(
			modifier = Modifier
				.weight(1f)
				.padding(start = 12.dp, end = 8.dp),
		) {
			Row(verticalAlignment = Alignment.CenterVertically) {
				Text(
					text = item.word,
					style = MaterialTheme.typography.titleMedium,
					fontWeight = FontWeight.SemiBold,
					color = colorScheme.onSurface,
				)
				val phonetic = item.phonetic
				if (!phonetic.isNullOrBlank()) {
					Text(
						text = phonetic,
						style = WordlyTypography.mono.copy(
							fontSize = 12.sp,
							lineHeight = 16.sp,
						),
						color = colorScheme.onSurfaceVariant,
						modifier = Modifier.padding(start = 8.dp),
					)
				}
			}
			val translation = item.translation
			if (!translation.isNullOrBlank()) {
				Text(
					text = translation,
					style = MaterialTheme.typography.bodyMedium,
					color = colorScheme.onSurfaceVariant,
					modifier = Modifier.padding(top = 4.dp),
				)
			}
		}
		Text(
			text = wordStatusLabel(item.status),
			style = MaterialTheme.typography.labelMedium,
			color = colorScheme.onSurfaceVariant,
		)
	}
}

@Composable
private fun wordStatusLabel(status: WordStatus): String =
	when (status) {
		WordStatus.New -> stringResource(R.string.words_status_new)
		WordStatus.InProgress -> stringResource(R.string.words_status_in_progress)
		WordStatus.Learned -> stringResource(R.string.words_status_learned)
	}

@Composable
private fun wordStatusColor(status: WordStatus): Color {
	val dark = isSystemInDarkTheme()
	return when (status) {
		WordStatus.New -> if (dark) WordlyColors.DarkOnSurfaceVariant2 else WordlyColors.LightOnSurfaceVariant2
		WordStatus.InProgress -> if (dark) WordlyColors.DarkWarning else WordlyColors.LightWarning
		WordStatus.Learned -> if (dark) WordlyColors.DarkSuccess else WordlyColors.LightSuccess
	}
}
