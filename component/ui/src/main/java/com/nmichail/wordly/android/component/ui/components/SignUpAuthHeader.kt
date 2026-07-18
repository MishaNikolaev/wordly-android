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

private val AUTH_PREVIEW_CARDS_WIDTH = 296.dp
private val AUTH_PREVIEW_CARDS_HEIGHT = 164.dp
private val AUTH_PREVIEW_SERENDIPITY_OFFSET_X = (-10).dp
private val AUTH_PREVIEW_DEPLOY_CARD_OFFSET_X = 22.dp
private val AUTH_PREVIEW_DEPLOY_CARD_OFFSET_Y = (-14).dp
private val AUTH_PREVIEW_RESILIENCE_CARD_OFFSET_Y = (-16).dp

@Composable
fun SignUpAuthHeader(
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
				.padding(top = 20.dp)
				.graphicsLayer { clip = false },
			contentAlignment = Alignment.TopCenter,
		) {
			Box(
				modifier = Modifier
					.width(AUTH_PREVIEW_CARDS_WIDTH)
					.height(AUTH_PREVIEW_CARDS_HEIGHT)
					.padding(vertical = 8.dp),
			) {
				AuthPreviewWordCard(
					word = stringResource(R.string.auth_preview_serendipity_word),
					translation = stringResource(R.string.auth_preview_serendipity_translation),
					modifier = Modifier
						.align(Alignment.TopStart)
						.offset(x = AUTH_PREVIEW_SERENDIPITY_OFFSET_X),
					rotation = 10f,
				)
				AuthPreviewWordCard(
					word = stringResource(R.string.auth_preview_deploy_word),
					translation = stringResource(R.string.auth_preview_deploy_translation),
					modifier = Modifier
						.align(Alignment.TopEnd)
						.offset(
							x = AUTH_PREVIEW_DEPLOY_CARD_OFFSET_X,
							y = AUTH_PREVIEW_DEPLOY_CARD_OFFSET_Y,
						),
					rotation = -14f,
				)
				AuthPreviewWordCard(
					word = stringResource(R.string.auth_preview_resilience_word),
					translation = stringResource(R.string.auth_preview_resilience_translation),
					modifier = Modifier
						.align(Alignment.BottomCenter)
						.offset(y = AUTH_PREVIEW_RESILIENCE_CARD_OFFSET_Y),
					rotation = 0f,
				)
			}
		}
	}
}
