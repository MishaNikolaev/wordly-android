@file:Suppress("MagicNumber")

package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.R
import com.nmichail.wordly.android.component.ui.theme.WordlyTypography

private val WORD_CARD_SHAPE = RoundedCornerShape(16.dp)
private val AUTH_PREVIEW_WORD_CARD_SHAPE = RoundedCornerShape(20.dp)
private val AUTH_PREVIEW_CARD_SHADOW_COLOR = Color.Black.copy(alpha = 0.38f)
private val AUTH_PREVIEW_WORD_CARD_WIDTH = 168.dp
private val AUTH_PREVIEW_WORD_CARD_HEIGHT = 68.dp
private val AUTH_PREVIEW_WORD_CARD_SHADOW_ELEVATION = 20.dp
private val WORD_CARD_WIDTH = 200.dp
private val WORD_CARD_HEIGHT = 108.dp
private val WORD_CARD_SHADOW_ELEVATION = 24.dp

@Composable
fun AuthPreviewWordCard(
	word: String,
	translation: String,
	modifier: Modifier = Modifier,
	rotation: Float = 0f,
) {
	val colorScheme = MaterialTheme.colorScheme

	Box(modifier = modifier.graphicsLayer { rotationZ = rotation }) {
		Column(
			modifier = Modifier
				.size(AUTH_PREVIEW_WORD_CARD_WIDTH, AUTH_PREVIEW_WORD_CARD_HEIGHT)
				.shadow(
					elevation = AUTH_PREVIEW_WORD_CARD_SHADOW_ELEVATION,
					shape = AUTH_PREVIEW_WORD_CARD_SHAPE,
					clip = false,
					ambientColor = AUTH_PREVIEW_CARD_SHADOW_COLOR,
					spotColor = AUTH_PREVIEW_CARD_SHADOW_COLOR,
				)
				.background(colorScheme.surfaceBright, AUTH_PREVIEW_WORD_CARD_SHAPE)
				.padding(horizontal = 16.dp, vertical = 10.dp),
		) {
			Text(
				text = word,
				style = WordlyTypography.authPreviewWord,
				color = colorScheme.onSurface,
			)
			Text(
				text = translation,
				style = WordlyTypography.authPreviewTranslation,
				color = colorScheme.onSurfaceVariant,
				modifier = Modifier.padding(top = 4.dp),
			)
		}
	}
}

@Composable
fun WordCard(
	word: String,
	phonetic: String,
	modifier: Modifier = Modifier,
	rotation: Float = 0f,
	languageDirection: String = stringResource(R.string.auth_word_language_direction),
	onPlayClick: (() -> Unit)? = null,
) {
	val colorScheme = MaterialTheme.colorScheme
	val shadowElevationPx = with(LocalDensity.current) { WORD_CARD_SHADOW_ELEVATION.toPx() }

	Column(
		modifier = modifier
			.padding(12.dp)
			.size(WORD_CARD_WIDTH, WORD_CARD_HEIGHT)
			.graphicsLayer {
				shape = WORD_CARD_SHAPE
				clip = true
				shadowElevation = shadowElevationPx
				rotationZ = rotation
			}
			.background(colorScheme.surfaceBright, WORD_CARD_SHAPE)
			.padding(horizontal = 12.dp, vertical = 12.dp),
	) {
		WordCardTopBar(
			languageDirection = languageDirection,
			iconTint = colorScheme.onSurfaceVariant,
			badgeBackground = colorScheme.primaryContainer,
			badgeText = colorScheme.inversePrimary,
			onPlayClick = onPlayClick,
		)
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.weight(1f),
			contentAlignment = Alignment.Center,
		) {
			WordCardContent(
				word = word,
				phonetic = phonetic,
				wordColor = colorScheme.onSurface,
				phoneticColor = colorScheme.onSurfaceVariant,
			)
		}
	}
}

@Composable
private fun WordCardTopBar(
	languageDirection: String,
	iconTint: Color,
	badgeBackground: Color,
	badgeText: Color,
	onPlayClick: (() -> Unit)?,
) {
	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.SpaceBetween,
		verticalAlignment = Alignment.CenterVertically,
	) {
		Text(
			text = languageDirection,
			style = MaterialTheme.typography.labelSmall,
			fontWeight = FontWeight.Bold,
			color = badgeText,
			modifier = Modifier
				.clip(RoundedCornerShape(8.dp))
				.background(badgeBackground)
				.padding(horizontal = 8.dp, vertical = 4.dp),
		)
		if (onPlayClick != null) {
			IconButton(
				onClick = onPlayClick,
				modifier = Modifier.size(20.dp),
			) {
				WordCardSpeakerIcon(tint = iconTint)
			}
		} else {
			WordCardSpeakerIcon(
				tint = iconTint,
				modifier = Modifier.size(16.dp),
			)
		}
	}
}

@Composable
private fun WordCardContent(
	word: String,
	phonetic: String,
	wordColor: Color,
	phoneticColor: Color,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Text(
			text = word,
			style = WordlyTypography.wordCardWord,
			color = wordColor,
		)
		Text(
			text = phonetic,
			style = WordlyTypography.wordCardPhonetic,
			color = phoneticColor,
			modifier = Modifier.padding(top = 4.dp),
		)
	}
}

@Composable
private fun WordCardSpeakerIcon(
	tint: Color,
	modifier: Modifier = Modifier,
) {
	Icon(
		imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
		contentDescription = stringResource(R.string.auth_word_play_pronunciation),
		tint = tint,
		modifier = modifier,
	)
}