package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.R

@Composable
fun SignInAuthHeader(
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.fillMaxWidth()
			.padding(bottom = 10.dp),
		horizontalAlignment = Alignment.Start,
	) {
		Logo()
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.padding(24.dp),
			contentAlignment = Alignment.Center,
		) {
			WordCard(
				word = stringResource(R.string.auth_preview_resilience_word),
				phonetic = stringResource(R.string.auth_preview_resilience_phonetic),
				rotation = -6f,
			)
		}
	}
}