package com.nmichail.wordly.android.shared.authorization

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.wui.theme.WuiTheme
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.nmichail.wordly.android.component.wui.theme.PreviewTheme
import com.nmichail.wordly.android.component.wui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.wui.theme.WuiTypography

@Composable
fun AuthPhoneticWordCard(
	word: String,
	phonetic: String,
	modifier: Modifier = Modifier,
	rotation: Float = 0f,
	languageDirection: String = stringResource(R.string.auth_word_language_direction),
	onPlayClick: (() -> Unit)? = null,
) {
	val colorScheme = MaterialTheme.colorScheme
	val shadowElevationPx = with(LocalDensity.current) { 24.dp.toPx() }

	Column(
		modifier = modifier
			.padding(12.dp)
			.size(200.dp, 108.dp)
			.graphicsLayer {
				shape = RoundedCornerShape(16.dp)
				clip = true
				shadowElevation = shadowElevationPx
				rotationZ = rotation
			}
			.background(colorScheme.surface, RoundedCornerShape(16.dp))
			.padding(horizontal = 12.dp, vertical = 12.dp),
	) {
		AuthPhoneticWordCardTopBar(
			languageDirection = languageDirection,
			iconTint = colorScheme.onSurfaceVariant,
			chipBackground = colorScheme.primaryContainer,
			chipText = colorScheme.inversePrimary,
			onPlayClick = onPlayClick,
		)
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.weight(1f),
			contentAlignment = Alignment.Center,
		) {
			AuthPhoneticWordCardContent(
				word = word,
				phonetic = phonetic,
				wordColor = colorScheme.onSurface,
				phoneticColor = colorScheme.onSurfaceVariant,
			)
		}
	}
}

@Composable
private fun AuthPhoneticWordCardTopBar(
	languageDirection: String,
	iconTint: Color,
	chipBackground: Color,
	chipText: Color,
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
			color = chipText,
			modifier = Modifier
				.clip(RoundedCornerShape(8.dp))
				.background(chipBackground)
				.padding(horizontal = 8.dp, vertical = 4.dp),
		)
		if (onPlayClick != null) {
			IconButton(
				onClick = onPlayClick,
				modifier = Modifier.size(20.dp),
			) {
				Icon(
					imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
					contentDescription = stringResource(R.string.auth_word_play_pronunciation),
					tint = iconTint,
				)
			}
		}
	}
}

@Composable
private fun AuthPhoneticWordCardContent(
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
			style = WuiTypography.wordCardWord,
			color = wordColor,
		)
		Text(
			text = phonetic,
			style = WuiTypography.wordCardPhonetic,
			color = phoneticColor,
			modifier = Modifier.padding(top = 4.dp),
		)
	}
}

@Preview(showBackground = true)
@Composable
private fun AuthPhoneticWordCardPreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WuiTheme(darkTheme = theme == PreviewTheme.Dark) {
		AuthPhoneticWordCard(word = "resilience", phonetic = "/rɪˈzɪliəns/", onPlayClick = {})
	}
}