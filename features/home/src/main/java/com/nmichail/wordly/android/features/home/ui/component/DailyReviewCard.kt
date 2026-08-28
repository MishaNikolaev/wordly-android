package com.nmichail.wordly.android.features.home.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.wui.theme.WuiBrushes
import com.nmichail.wordly.android.component.wui.theme.WuiTheme
import com.nmichail.wordly.android.component.wui.theme.WuiTypography
import com.nmichail.wordly.android.component.wui.theme.isAppInDarkTheme
import com.nmichail.wordly.android.features.home.R
import com.nmichail.wordly.android.component.wui.R as WuiR

@Composable
fun DailyReviewCard(
	wordsToReview: Int,
	estimatedMinutes: Int,
	streakDays: Int,
	onStartClick: () -> Unit,
	onStreakClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val shape = RoundedCornerShape(24.dp)
	val colorScheme = MaterialTheme.colorScheme
	val gradient = if (isAppInDarkTheme()) {
		WuiBrushes.DailyReviewDark
	} else {
		WuiBrushes.DailyReviewLight
	}

	Card(
		modifier = modifier
			.fillMaxWidth()
			.clickable(role = Role.Button, onClick = onStartClick),
		shape = shape,
		border = BorderStroke(1.dp, colorScheme.outlineVariant),
		colors = CardDefaults.cardColors(containerColor = Color.Transparent),
		elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
	) {
		DailyReviewCardContent(
			wordsToReview = wordsToReview,
			estimatedMinutes = estimatedMinutes,
			streakDays = streakDays,
			onStreakClick = onStreakClick,
			shape = shape,
			gradient = gradient,
		)
	}
}

@Composable
private fun DailyReviewCardContent(
	wordsToReview: Int,
	estimatedMinutes: Int,
	streakDays: Int,
	onStreakClick: () -> Unit,
	shape: RoundedCornerShape,
	gradient: Brush,
) {
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.background(brush = gradient, shape = shape)
			.padding(19.dp),
	) {
		Image(
			painter = painterResource(WuiR.drawable.mascot),
			contentDescription = null,
			modifier = Modifier
				.align(Alignment.CenterEnd)
				.size(width = 99.dp, height = 93.dp),
		)
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(end = 100.dp),
			verticalArrangement = Arrangement.spacedBy(12.dp),
		) {
			TitleChip()
			ReviewWordsTitle(wordsToReview = wordsToReview)
			ReviewMetaRow(
				estimatedMinutes = estimatedMinutes,
				streakDays = streakDays,
				onStreakClick = onStreakClick,
			)
		}
	}
}

@Composable
private fun TitleChip() {
	val colorScheme = MaterialTheme.colorScheme
	Text(
		text = stringResource(R.string.home_daily_review_chip),
		style = MaterialTheme.typography.labelSmall,
		fontWeight = FontWeight.Bold,
		color = colorScheme.onPrimary,
		modifier = Modifier
			.background(colorScheme.primary, RoundedCornerShape(percent = 50))
			.padding(horizontal = 16.dp, vertical = 6.dp),
	)
}

@Composable
private fun ReviewWordsTitle(wordsToReview: Int) {
	val colorScheme = MaterialTheme.colorScheme
	Row(
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(12.dp),
	) {
		Text(
			text = wordsToReview.toString(),
			style = WuiTypography.dailyReviewCount,
			color = colorScheme.onSurface,
		)
		Column {
			Text(
				text = pluralStringResource(R.plurals.home_words_to_review_line1, wordsToReview),
				style = WuiTypography.dailyReviewCountLabel,
				color = colorScheme.onSurface,
			)
			Text(
				text = stringResource(R.string.home_words_to_review_line2),
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
	val colorScheme = MaterialTheme.colorScheme
	val darkTheme = isAppInDarkTheme()
	val chipBackground = if (darkTheme) {
		colorScheme.surface.copy(alpha = DARK_FROSTED_CHIP_ALPHA)
	} else {
		Color.White.copy(alpha = LIGHT_FROSTED_CHIP_ALPHA)
	}
	val contentColor = if (darkTheme) {
		colorScheme.onSurface
	} else {
		colorScheme.onPrimaryContainer
	}

	Row(
		horizontalArrangement = Arrangement.spacedBy(8.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		FrostedChip(
			text = stringResource(R.string.home_daily_review_duration, estimatedMinutes),
			icon = Icons.Outlined.Schedule,
			background = chipBackground,
			contentColor = contentColor,
		)
		if (streakDays > 0) {
			FrostedChip(
				text = pluralStringResource(
					R.plurals.home_daily_review_streak,
					streakDays,
					streakDays,
				),
				icon = Icons.Filled.LocalFireDepartment,
				background = chipBackground,
				contentColor = contentColor,
				modifier = Modifier.clickable(role = Role.Button, onClick = onStreakClick),
			)
		}
	}
}

@Composable
private fun FrostedChip(
	text: String,
	icon: ImageVector,
	background: Color,
	contentColor: Color,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier
			.background(background, RoundedCornerShape(percent = 50))
			.padding(horizontal = 10.dp, vertical = 6.dp),
		horizontalArrangement = Arrangement.spacedBy(4.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		Icon(
			imageVector = icon,
			contentDescription = null,
			tint = contentColor,
			modifier = Modifier.size(14.dp),
		)
		Text(
			text = text,
			style = MaterialTheme.typography.labelSmall,
			fontWeight = FontWeight.SemiBold,
			color = contentColor,
		)
	}
}

private const val LIGHT_FROSTED_CHIP_ALPHA = 0.7f
private const val DARK_FROSTED_CHIP_ALPHA = 0.35f

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun DailyReviewCardPreview() {
	WuiTheme {
		DailyReviewCard(
			wordsToReview = 12,
			estimatedMinutes = 4,
			streakDays = 7,
			onStartClick = {},
			onStreakClick = {},
			modifier = Modifier.padding(16.dp),
		)
	}
}
