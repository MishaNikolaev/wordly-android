package com.nmichail.wordly.android.features.stats.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nmichail.wordly.android.features.stats.R

@Composable
fun StatsContent(
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier.fillMaxSize(),
		contentAlignment = Alignment.Center,
	) {
		Text(
			text = stringResource(R.string.stats_screen_title),
			style = MaterialTheme.typography.headlineMedium,
		)
	}
}
