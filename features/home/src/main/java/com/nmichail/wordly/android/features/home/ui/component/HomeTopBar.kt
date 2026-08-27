package com.nmichail.wordly.android.features.home.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.nmichail.wordly.android.component.wui.theme.WuiTheme

@Composable
fun HomeTopBar(
	title: String,
	streakDays: Int,
	onStreakClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.background(MaterialTheme.colorScheme.background)
			.padding(horizontal = 16.dp, vertical = 12.dp),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically,
	) {
		Text(
			text = title,
			style = MaterialTheme.typography.titleLarge,
			fontWeight = FontWeight.SemiBold,
			color = MaterialTheme.colorScheme.onSurface,
			modifier = Modifier
				.weight(1f)
				.padding(end = 12.dp),
		)
		if (streakDays > 0) {
			StreakChip(
				streakDays = streakDays,
				onClick = onStreakClick,
			)
		}
	}
}

@Composable
private fun StreakChip(
	streakDays: Int,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val primary = MaterialTheme.colorScheme.primary
	Row(
		modifier = modifier
			.clip(CircleShape)
			.background(color = primary.copy(alpha = 0.12f), shape = CircleShape)
			.clickable(role = Role.Button, onClick = onClick)
			.padding(horizontal = 12.dp, vertical = 8.dp),
		horizontalArrangement = Arrangement.spacedBy(4.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		Icon(
			imageVector = Icons.Filled.LocalFireDepartment,
			contentDescription = null,
			tint = primary,
			modifier = Modifier.size(20.dp),
		)
		Text(
			text = streakDays.toString(),
			style = MaterialTheme.typography.labelMedium,
			fontWeight = FontWeight.SemiBold,
			color = primary,
		)
	}
}

@Preview(showBackground = true)
@Composable
private fun HomeTopBarPreview() {
	WuiTheme {
		HomeTopBar(
			title = "Доброе утро, Alex",
			streakDays = 5,
			onStreakClick = {},
		)
	}
}