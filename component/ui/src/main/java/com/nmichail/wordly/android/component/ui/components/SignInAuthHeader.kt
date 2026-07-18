@file:Suppress("MagicNumber")

package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.R

private val AUTH_CARDS_WIDTH = 360.dp
private val AUTH_CARDS_HEIGHT = 180.dp
private val RESILIENCE_CARD_OFFSET_Y = 24.dp
private val DEPLOY_CARD_OFFSET_X = 148.dp
private val DEPLOY_CARD_OFFSET_Y = (-44).dp

@Composable
fun SignInAuthHeader(
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = 20.dp)
			.padding(top = 12.dp, bottom = 28.dp),
		horizontalAlignment = Alignment.Start,
	) {
		Logo()
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 28.dp)
				.graphicsLayer { clip = false },
			contentAlignment = Alignment.Center,
		) {
			Box(
				modifier = Modifier
					.width(AUTH_CARDS_WIDTH)
					.height(AUTH_CARDS_HEIGHT),
			) {
				WordCard(
					word = stringResource(R.string.auth_preview_resilience_word),
					phonetic = stringResource(R.string.auth_preview_resilience_phonetic),
					rotation = 8f,
					modifier = Modifier
						.align(Alignment.TopStart)
						.offset(y = RESILIENCE_CARD_OFFSET_Y),
				)
				WordCard(
					word = stringResource(R.string.auth_preview_deploy_word),
					phonetic = stringResource(R.string.auth_preview_deploy_phonetic),
					rotation = -6f,
					modifier = Modifier
						.align(Alignment.TopStart)
						.offset(x = DEPLOY_CARD_OFFSET_X, y = DEPLOY_CARD_OFFSET_Y),
				)
			}
		}
	}
}
