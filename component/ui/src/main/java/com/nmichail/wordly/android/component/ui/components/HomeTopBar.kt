package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.theme.WordlyColors
import com.nmichail.wordly.android.component.ui.theme.WordlyTypography

@Composable
fun HomeTopBar(
	title: String,
	streakDays: Int,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.background(MaterialTheme.colorScheme.surface)
			.padding(horizontal = 16.dp, vertical = 12.dp),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically,
	) {
		Text(
			text = title,
			style = WordlyTypography.homeScreenTitle,
			color = MaterialTheme.colorScheme.onSurface,
		)
		StreakChip(streakDays = streakDays)
	}
}

@Composable
private fun StreakChip(
	streakDays: Int,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier
			.background(WordlyColors.StreakContainer, RoundedCornerShape(percent = 50))
			.padding(horizontal = 12.dp, vertical = 8.dp),
		horizontalArrangement = Arrangement.spacedBy(4.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		Icon(
			imageVector = Icons.Filled.LocalFireDepartment,
			contentDescription = null,
			tint = WordlyColors.Streak,
			modifier = Modifier.size(20.dp),
		)
		Text(
			text = streakDays.toString(),
			style = MaterialTheme.typography.labelMedium,
			fontWeight = FontWeight.SemiBold,
			color = WordlyColors.Streak,
		)
	}
}
