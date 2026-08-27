package com.nmichail.wordly.android.features.home.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nmichail.wordly.android.component.wui.R as WuiR
import com.nmichail.wordly.android.features.home.R

@Composable
internal fun TrainingFilterChip(
	text: String,
	selected: Boolean,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme
	val background = if (selected) colorScheme.onSurface else colorScheme.surfaceVariant.copy(alpha = 0.55f)
	val contentColor = if (selected) colorScheme.surface else colorScheme.onSurface

	Box(
		modifier = modifier
			.height(40.dp)
			.defaultMinSize(minWidth = 58.dp)
			.clip(RoundedCornerShape(percent = 50))
			.background(background)
			.clickable(role = Role.Button, onClick = onClick)
			.padding(horizontal = 16.dp),
		contentAlignment = Alignment.Center,
	) {
		Text(
			text = text,
			fontSize = 14.sp,
			lineHeight = 18.sp,
			fontWeight = FontWeight.SemiBold,
			color = contentColor,
			maxLines = 1,
		)
	}
}

@Composable
internal fun TrainingGridTab(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme

	Box(
		modifier = modifier
			.size(40.dp)
			.clip(RoundedCornerShape(12.dp))
			.background(colorScheme.onSurface)
			.clickable(role = Role.Button, onClick = onClick),
		contentAlignment = Alignment.Center,
	) {
		Icon(
			painter = painterResource(WuiR.drawable.ic_training_grid),
			contentDescription = stringResource(R.string.home_training_grid_content_description),
			tint = colorScheme.surface,
			modifier = Modifier.size(18.dp),
		)
	}
}

internal fun HomeTrainingTab.titleRes(): Int =
	when (this) {
		HomeTrainingTab.Cards -> R.string.home_training_cards_title
		HomeTrainingTab.Constructor -> R.string.home_training_constructor_title
		HomeTrainingTab.Listening -> R.string.home_training_listening_title
		HomeTrainingTab.Books -> R.string.home_training_books_title
	}

internal fun HomeTrainingFilter.titleRes(): Int =
	when (this) {
		HomeTrainingFilter.Cards -> R.string.home_training_cards_title
		HomeTrainingFilter.Constructor -> R.string.home_training_constructor_title
		HomeTrainingFilter.Listening -> R.string.home_training_listening_title
		HomeTrainingFilter.Songs -> R.string.home_training_songs_title
		HomeTrainingFilter.Movies -> R.string.home_training_movies_title
		HomeTrainingFilter.Books -> R.string.home_training_books_title
	}

internal fun HomeTrainingFilter.matches(selectedTab: HomeTrainingTab?): Boolean =
	when (this) {
		HomeTrainingFilter.Cards -> selectedTab == HomeTrainingTab.Cards
		HomeTrainingFilter.Constructor -> selectedTab == HomeTrainingTab.Constructor
		HomeTrainingFilter.Listening -> selectedTab == HomeTrainingTab.Listening
		HomeTrainingFilter.Books -> selectedTab == HomeTrainingTab.Books
		HomeTrainingFilter.Songs,
		HomeTrainingFilter.Movies,
		-> false
	}
