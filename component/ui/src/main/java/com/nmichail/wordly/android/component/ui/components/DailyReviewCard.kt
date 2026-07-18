package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.R
import com.nmichail.wordly.android.component.ui.theme.WordlyTypography

private val DailyReviewCardHeight = 257.dp

@Composable
fun DailyReviewCard(
	wordsToReview: Int,
	estimatedMinutes: Int,
	streakDays: Int,
	onStartClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val shape = MaterialTheme.shapes.extraLarge

	Card(
		modifier = modifier
			.fillMaxWidth()
			.height(DailyReviewCardHeight),
		shape = shape,
		colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
		elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
	) {
		Box(modifier = Modifier.fillMaxSize()) {
			Image(
				painter = painterResource(R.drawable.pixelmosaic),
				contentDescription = null,
				modifier = Modifier.fillMaxSize(),
				contentScale = ContentScale.Crop,
				alignment = Alignment.TopCenter,
			)
			DailyReviewBody(
				wordsToReview = wordsToReview,
				estimatedMinutes = estimatedMinutes,
				streakDays = streakDays,
				onStartClick = onStartClick,
			)
		}
	}
}

@Composable
private fun DailyReviewBody(
	wordsToReview: Int,
	estimatedMinutes: Int,
	streakDays: Int,
	onStartClick: () -> Unit,
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp),
	) {
		StatusChip(
			text = stringResource(R.string.home_daily_review_chip),
			icon = Icons.Outlined.Bolt,
			style = StatusChipStyle.Accent,
		)
		ReviewWordsTitle(wordsToReview = wordsToReview)
		ReviewMetaRow(
			estimatedMinutes = estimatedMinutes,
			streakDays = streakDays,
		)
		Spacer(modifier = Modifier.weight(1f))
		Button(
			text = stringResource(R.string.home_daily_review_start),
			onClick = onStartClick,
		)
	}
}

@Composable
private fun ReviewWordsTitle(wordsToReview: Int) {
	val colorScheme = MaterialTheme.colorScheme
	Row(
		modifier = Modifier.padding(top = 12.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(18.dp),
	) {
		Text(
			text = wordsToReview.toString(),
			style = WordlyTypography.dailyReviewCount,
			color = colorScheme.onSurface,
		)
		Column(modifier = Modifier.padding(start = 4.dp)) {
			Text(
				text = pluralStringResource(R.plurals.home_words_to_review_prefix, wordsToReview),
				style = WordlyTypography.dailyReviewCountLabel,
				color = colorScheme.onSurface,
			)
			Text(
				text = stringResource(R.string.home_words_to_review_suffix),
				style = WordlyTypography.dailyReviewCountLabel,
				color = colorScheme.onSurface,
			)
		}
	}
}

@Composable
private fun ReviewMetaRow(
	estimatedMinutes: Int,
	streakDays: Int,
) {
	Row(
		modifier = Modifier.padding(top = 10.dp),
		horizontalArrangement = Arrangement.spacedBy(8.dp),
	) {
		StatusChip(
			text = stringResource(R.string.home_daily_review_duration, estimatedMinutes),
			icon = Icons.Outlined.Schedule,
			style = StatusChipStyle.Neutral,
		)
		StatusChip(
			text = pluralStringResource(
				R.plurals.home_daily_review_streak,
				streakDays,
				streakDays,
			),
			icon = Icons.Filled.LocalFireDepartment,
			style = StatusChipStyle.Streak,
		)
	}
}
