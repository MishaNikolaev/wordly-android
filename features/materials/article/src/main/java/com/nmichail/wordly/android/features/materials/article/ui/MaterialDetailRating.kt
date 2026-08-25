package com.nmichail.wordly.android.features.materials.article.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.wui.components.icon.WuiAnimatedToggleIcon
import com.nmichail.wordly.android.features.materials.article.R
import com.nmichail.wordly.android.features.materials.article.domain.entity.MaterialReaction

@Composable
internal fun MaterialRatingSection(
	likes: Int,
	dislikes: Int,
	selectedReaction: MaterialReaction?,
	onLikeClick: () -> Unit,
	onDislikeClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(modifier = modifier.fillMaxWidth()) {
		Text(
			text = stringResource(R.string.materials_detail_rate_title),
			style = MaterialTheme.typography.titleMedium,
			fontWeight = FontWeight.SemiBold,
			color = MaterialTheme.colorScheme.onBackground,
		)
		Text(
			text = stringResource(R.string.materials_detail_rate_subtitle),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			modifier = Modifier.padding(top = 6.dp),
		)
		Row(
			modifier = Modifier.padding(top = 16.dp),
			horizontalArrangement = Arrangement.spacedBy(12.dp),
		) {
			ReactionButton(
				selected = selectedReaction == MaterialReaction.Like,
				count = likes + if (selectedReaction == MaterialReaction.Like) 1 else 0,
				selectedIcon = Icons.Filled.ThumbUp,
				unselectedIcon = Icons.Outlined.ThumbUp,
				contentDescription = stringResource(R.string.materials_detail_like),
				onClick = onLikeClick,
				modifier = Modifier.weight(1f),
			)
			ReactionButton(
				selected = selectedReaction == MaterialReaction.Dislike,
				count = dislikes + if (selectedReaction == MaterialReaction.Dislike) 1 else 0,
				selectedIcon = Icons.Filled.ThumbDown,
				unselectedIcon = Icons.Outlined.ThumbDown,
				contentDescription = stringResource(R.string.materials_detail_dislike),
				onClick = onDislikeClick,
				modifier = Modifier.weight(1f),
			)
		}
	}
}

@Composable
private fun ReactionButton(
	selected: Boolean,
	count: Int,
	selectedIcon: ImageVector,
	unselectedIcon: ImageVector,
	contentDescription: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme
	val background = if (selected) {
		colorScheme.primaryContainer
	} else {
		colorScheme.surface
	}
	val contentColor = if (selected) {
		colorScheme.onPrimaryContainer
	} else {
		colorScheme.onSurface
	}

	Row(
		modifier = modifier
			.height(48.dp)
			.clip(RoundedCornerShape(16.dp))
			.background(background)
			.clickable(role = Role.Button, onClick = onClick)
			.padding(horizontal = 16.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.Center,
	) {
		WuiAnimatedToggleIcon(
			checked = selected,
			checkedIcon = selectedIcon,
			uncheckedIcon = unselectedIcon,
			checkedTint = contentColor,
			uncheckedTint = contentColor,
			contentDescription = contentDescription,
			iconSize = 20.dp,
		)
		Text(
			text = count.toString(),
			style = MaterialTheme.typography.titleSmall,
			fontWeight = FontWeight.SemiBold,
			color = contentColor,
			modifier = Modifier.padding(start = 8.dp),
		)
	}
}
