@file:Suppress("MagicNumber")

package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.R
import com.nmichail.wordly.android.component.ui.theme.WordlyColors

private val HeroScrim = Color.Black.copy(alpha = 0.35f)

private val HeroGradient = Brush.verticalGradient(
	colors = listOf(
		Color(0xFFB45ADE),
		WordlyColors.Primary,
		Color(0xFF6B1F8A),
	),
)

@Composable
fun NewsDetailHero(
	publishedAt: String,
	readingMinutesLabel: String,
	modifier: Modifier = Modifier,
	imageUrl: String? = null,
) {
	Box(
		modifier = modifier
			.fillMaxWidth()
			.height(220.dp),
	) {
		HeroBackground(imageUrl = imageUrl)
		HeroMetaChips(
			publishedAt = publishedAt,
			readingMinutesLabel = readingMinutesLabel,
			modifier = Modifier
				.align(Alignment.BottomStart)
				.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
		)
	}
}

@Composable
fun NewsDetailTopBar(
	onBackClick: () -> Unit,
	onBookmarkClick: () -> Unit,
	modifier: Modifier = Modifier,
	isBookmarked: Boolean = false,
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.statusBarsPadding()
			.padding(horizontal = 8.dp)
			.padding(top = 2.dp),
		horizontalArrangement = Arrangement.SpaceBetween,
	) {
		HeroIconButton(onClick = onBackClick) {
			Icon(
				imageVector = Icons.AutoMirrored.Filled.ArrowBack,
				contentDescription = stringResource(R.string.news_detail_back),
				tint = Color.White,
				modifier = Modifier.size(22.dp),
			)
		}
		HeroIconButton(onClick = onBookmarkClick) {
			Icon(
				imageVector = if (isBookmarked) {
					Icons.Filled.Bookmark
				} else {
					Icons.Outlined.BookmarkBorder
				},
				contentDescription = stringResource(R.string.news_detail_bookmark),
				tint = Color.White,
				modifier = Modifier.size(22.dp),
			)
		}
	}
}

@Composable
private fun HeroBackground(imageUrl: String?) {
	if (imageUrl.isNullOrBlank()) {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(HeroGradient),
		)
		Box(modifier = Modifier.fillMaxSize()) {
			Image(
				painter = painterResource(R.drawable.chat_bubble),
				contentDescription = null,
				modifier = Modifier
					.align(Alignment.CenterEnd)
					.padding(end = 64.dp)
					.size(40.dp),
				contentScale = ContentScale.Fit,
			)
		}
	} else {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(MaterialTheme.colorScheme.surfaceVariant),
		)
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(Color.Black.copy(alpha = 0.25f)),
		)
	}
}

@Composable
private fun HeroMetaChips(
	publishedAt: String,
	readingMinutesLabel: String,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier,
		horizontalArrangement = Arrangement.spacedBy(8.dp),
	) {
		StatusChip(
			text = readingMinutesLabel,
			icon = Icons.Outlined.Schedule,
			style = StatusChipStyle.OnMedia,
		)
		StatusChip(
			text = publishedAt,
			icon = Icons.Outlined.CalendarToday,
			style = StatusChipStyle.OnMedia,
		)
	}
}

@Composable
private fun HeroIconButton(
	onClick: () -> Unit,
	content: @Composable () -> Unit,
) {
	IconButton(
		onClick = onClick,
		modifier = Modifier
			.size(40.dp)
			.clip(CircleShape)
			.background(HeroScrim),
	) {
		content()
	}
}