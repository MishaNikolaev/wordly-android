package com.nmichail.wordly.android.features.materials.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.component.ui.components.Button
import com.nmichail.wordly.android.component.ui.components.TextLink
import com.nmichail.wordly.android.component.ui.theme.WordlyBrushes
import com.nmichail.wordly.android.component.ui.theme.WordlyColors
import com.nmichail.wordly.android.features.materials.R
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialDetail
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialReaction
import com.nmichail.wordly.android.features.materials.presentation.detail.MaterialDetailComponent

@Composable
fun MaterialDetailContent(
	component: MaterialDetailComponent,
	modifier: Modifier = Modifier,
) {
	val state by component.model.subscribeAsState()

	Box(
		modifier = modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background),
	) {
		when (val current = state) {
			MaterialDetailComponent.State.Loading -> {
				CircularProgressIndicator(
					modifier = Modifier.align(Alignment.Center),
					color = MaterialTheme.colorScheme.primary,
				)
			}
			MaterialDetailComponent.State.Error -> MaterialDetailError(
				onBackClick = component::handleBack,
				onRetryClick = component::handleRetry,
				modifier = Modifier.fillMaxSize(),
			)
			is MaterialDetailComponent.State.Content -> MaterialDetailLoaded(
				state = current,
				onBackClick = component::handleBack,
				onLikeClick = component::handleLike,
				onDislikeClick = component::handleDislike,
			)
		}
	}
}

@Composable
private fun MaterialDetailError(
	onBackClick: () -> Unit,
	onRetryClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.statusBarsPadding()
			.navigationBarsPadding()
			.padding(horizontal = 24.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Text(
			text = stringResource(R.string.materials_detail_error_title),
			style = MaterialTheme.typography.titleMedium,
			color = MaterialTheme.colorScheme.onSurface,
			textAlign = TextAlign.Center,
		)
		Text(
			text = stringResource(R.string.materials_detail_error_subtitle),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(top = 8.dp),
		)
		Button(
			text = stringResource(R.string.materials_retry),
			onClick = onRetryClick,
			modifier = Modifier.padding(top = 24.dp),
		)
		TextLink(
			text = stringResource(R.string.materials_detail_back),
			onClick = onBackClick,
			modifier = Modifier.padding(top = 16.dp),
		)
	}
}

@Composable
private fun MaterialDetailLoaded(
	state: MaterialDetailComponent.State.Content,
	onBackClick: () -> Unit,
	onLikeClick: () -> Unit,
	onDislikeClick: () -> Unit,
) {
	Column(
		modifier = Modifier
			.fillMaxSize()
			.statusBarsPadding()
			.navigationBarsPadding()
			.verticalScroll(rememberScrollState())
			.padding(bottom = 24.dp),
	) {
		MaterialDetailTopBar(onBackClick = onBackClick)
		MaterialDetailHero(
			material = state.material,
			modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
		)
		Text(
			text = stringResource(R.string.materials_detail_description),
			style = MaterialTheme.typography.titleMedium,
			fontWeight = FontWeight.SemiBold,
			color = MaterialTheme.colorScheme.onBackground,
			modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 20.dp),
		)
		Text(
			text = state.material.description,
			style = MaterialTheme.typography.bodyLarge,
			color = MaterialTheme.colorScheme.onSurface,
			modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
		)
		MaterialRatingSection(
			likes = state.material.likes,
			dislikes = state.material.dislikes,
			selectedReaction = state.selectedReaction,
			onLikeClick = onLikeClick,
			onDislikeClick = onDislikeClick,
			modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
		)
	}
}

@Composable
private fun MaterialDetailTopBar(
	onBackClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = 4.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		IconButton(onClick = onBackClick) {
			Icon(
				imageVector = Icons.AutoMirrored.Filled.ArrowBack,
				contentDescription = stringResource(R.string.materials_detail_back),
				tint = MaterialTheme.colorScheme.onBackground,
			)
		}
	}
}

@Composable
private fun MaterialDetailHero(
	material: MaterialDetail,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(24.dp))
			.background(WordlyBrushes.MaterialHero)
			.padding(20.dp),
	) {
		MaterialHeroTagsRow(material = material)
		Text(
			text = material.title,
			style = MaterialTheme.typography.headlineSmall,
			fontWeight = FontWeight.Normal,
			color = Color.White,
			modifier = Modifier.padding(top = 16.dp),
		)
		MaterialHeroMetaRow(material = material)
	}
}

@Composable
private fun MaterialHeroTagsRow(material: MaterialDetail) {
	Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
		HeroTag(
			text = materialCategoryLabel(material.category),
			background = WordlyColors.CategoryTagContainer,
			contentColor = WordlyColors.OnCategoryTag,
		)
		HeroTag(
			text = material.typeLabel,
			background = Color.White.copy(alpha = 0.18f),
			contentColor = Color.White,
		)
	}
}

@Composable
private fun MaterialHeroMetaRow(material: MaterialDetail) {
	Column(
		modifier = Modifier.padding(top = 16.dp),
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(6.dp),
		) {
			Icon(
				imageVector = Icons.Outlined.Schedule,
				contentDescription = null,
				tint = Color.White.copy(alpha = 0.9f),
				modifier = Modifier.size(14.dp),
			)
			Text(
				text = stringResource(
					R.string.materials_detail_reading_minutes,
					material.readingMinutes,
				),
				style = MaterialTheme.typography.bodySmall,
				fontWeight = FontWeight.Normal,
				color = Color.White.copy(alpha = 0.9f),
			)
			Text(
				text = "•",
				style = MaterialTheme.typography.bodySmall,
				fontWeight = FontWeight.Normal,
				color = Color.White.copy(alpha = 0.8f),
			)
			Text(
				text = material.dateLabel,
				style = MaterialTheme.typography.bodySmall,
				fontWeight = FontWeight.Normal,
				color = Color.White.copy(alpha = 0.9f),
			)
		}
		Box(
			modifier = Modifier
				.padding(top = 8.dp)
				.clip(RoundedCornerShape(8.dp))
				.background(Color.White.copy(alpha = 0.2f))
				.padding(horizontal = 10.dp, vertical = 5.dp),
		) {
			Text(
				text = material.level,
				style = MaterialTheme.typography.labelMedium,
				fontWeight = FontWeight.Normal,
				color = Color.White,
			)
		}
	}
}

@Composable
private fun HeroTag(
	text: String,
	background: Color,
	contentColor: Color,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier
			.clip(RoundedCornerShape(percent = 50))
			.background(background)
			.padding(horizontal = 10.dp, vertical = 5.dp),
	) {
		Text(
			text = text,
			fontSize = 11.sp,
			lineHeight = 13.sp,
			fontWeight = FontWeight.Normal,
			color = contentColor,
		)
	}
}

@Composable
private fun MaterialRatingSection(
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
		Icon(
			imageVector = if (selected) selectedIcon else unselectedIcon,
			contentDescription = contentDescription,
			tint = contentColor,
			modifier = Modifier.size(20.dp),
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
