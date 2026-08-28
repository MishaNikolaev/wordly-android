package com.nmichail.wordly.android.features.movies.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.features.movies.R
import com.nmichail.wordly.android.features.movies.presentation.MoviesComponent

@Composable
fun MoviesContent(
	component: MoviesComponent,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.statusBarsPadding(),
	) {
		IconButton(
			onClick = component::handleBack,
			modifier = Modifier
				.align(Alignment.TopStart)
				.padding(4.dp),
		) {
			Icon(
				imageVector = Icons.AutoMirrored.Filled.ArrowBack,
				contentDescription = stringResource(R.string.movies_back),
				tint = MaterialTheme.colorScheme.onBackground,
			)
		}
		Text(
			text = stringResource(R.string.movies_title),
			style = MaterialTheme.typography.headlineMedium,
			color = MaterialTheme.colorScheme.onBackground,
			modifier = Modifier.align(Alignment.Center),
		)
	}
}
