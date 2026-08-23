package com.nmichail.wordly.android.features.home.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import com.nmichail.wordly.android.features.home.R
import com.nmichail.wordly.android.component.wui.theme.WuiTypography
import com.nmichail.wordly.android.component.wui.theme.isAppInDarkTheme
import com.nmichail.wordly.android.component.wui.components.button.WuiButton
import com.nmichail.wordly.android.component.wui.components.chip.WuiStatusChip
import com.nmichail.wordly.android.component.wui.components.chip.WuiStatusChipStyle
import androidx.compose.ui.tooling.preview.Preview
import com.nmichail.wordly.android.component.wui.theme.WuiTheme

@Composable
fun DailyReviewCard(
	wordsToReview: Int,
	estimatedMinutes: Int,
	streakDays: Int,
	onStartClick: () -> Unit,
	onStreakClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val shape = MaterialTheme.shapes.extraLarge
	val mosaicRes = if (isAppInDarkTheme()) {
		R.drawable.pixelmosaic_dark
	} else {
		R.drawable.pixelmosaic_light
	}

	Card(
		modifier = modifier
			.fillMaxWidth()
			.height(257.dp),
		shape = shape,
		colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
		elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
	) {
		Box(modifier = Modifier.fillMaxSize()) {
			Image(
				painter = painterResource(mosaicRes),
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
				onStreakClick = onStreakClick,
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
	onStreakClick: () -> Unit,
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(16.dp),
	) {
		WuiStatusChip(
			text = stringResource(R.string.home_daily_review_chip),
			icon = Icons.Outlined.Bolt,
			style = WuiStatusChipStyle.Accent,
		)
		ReviewWordsTitle(wordsToReview = wordsToReview)
		ReviewMetaRow(
			estimatedMinutes = estimatedMinutes,
			streakDays = streakDays,
			onStreakClick = onStreakClick,
		)
		Spacer(modifier = Modifier.weight(1f))
		WuiButton(
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
			style = WuiTypography.dailyReviewCount,
			color = colorScheme.onSurface,
		)
		Column(modifier = Modifier.padding(start = 4.dp)) {
			Text(
				text = pluralStringResource(R.plurals.home_words_to_review_prefix, wordsToReview),
				style = WuiTypography.dailyReviewCountLabel,
				color = colorScheme.onSurface,
			)
			Text(
				text = pluralStringResource(R.plurals.home_words_to_review_suffix, wordsToReview),
				style = WuiTypography.dailyReviewCountLabel,
				color = colorScheme.onSurface,
			)
		}
	}
}

@Composable
private fun ReviewMetaRow(
	estimatedMinutes: Int,
	streakDays: Int,
	onStreakClick: () -> Unit,
) {
	Row(
		modifier = Modifier.padding(top = 10.dp),
		horizontalArrangement = Arrangement.spacedBy(8.dp),
	) {
		WuiStatusChip(
			text = stringResource(R.string.home_daily_review_duration, estimatedMinutes),
			icon = Icons.Outlined.Schedule,
			style = WuiStatusChipStyle.Neutral,
		)
		WuiStatusChip(
			text = pluralStringResource(
				R.plurals.home_daily_review_streak,
				streakDays,
				streakDays,
			),
			icon = Icons.Filled.LocalFireDepartment,
			style = WuiStatusChipStyle.Streak,
			modifier = Modifier.clickable(onClick = onStreakClick),
		)
	}
}

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun DailyReviewCardPreview() {
	WuiTheme {
		DailyReviewCard(
			wordsToReview = 12,
			estimatedMinutes = 5,
			streakDays = 7,
			onStartClick = {},
			onStreakClick = {},
			modifier = Modifier
				.padding(16.dp)
				.height(220.dp),
		)
	}
}