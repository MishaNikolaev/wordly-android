package com.nmichail.wordly.android.features.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.component.ui.R as ComponentR
import com.nmichail.wordly.android.features.home.presentation.NewsDetailComponent

@Composable
fun NewsDetailContent(
	component: NewsDetailComponent,
	modifier: Modifier = Modifier,
) {
	val state by component.model.subscribeAsState()
	val colorScheme = MaterialTheme.colorScheme

	Column(
		modifier = modifier
			.fillMaxSize()
			.background(colorScheme.background),
	) {
		IconButton(
			onClick = component::handleBack,
			modifier = Modifier.padding(start = 4.dp, top = 4.dp),
		) {
			Icon(
				imageVector = Icons.AutoMirrored.Filled.ArrowBack,
				contentDescription = stringResource(ComponentR.string.news_detail_back),
				tint = colorScheme.onSurface,
			)
		}
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(rememberScrollState())
				.padding(horizontal = 20.dp)
				.padding(bottom = 32.dp),
		) {
			Text(
				text = state.title,
				style = MaterialTheme.typography.headlineSmall,
				fontWeight = FontWeight.Bold,
				color = colorScheme.onSurface,
			)
			Spacer(modifier = Modifier.height(8.dp))
			Text(
				text = state.publishedAt,
				style = MaterialTheme.typography.labelMedium,
				color = colorScheme.onSurfaceVariant,
			)
			Spacer(modifier = Modifier.height(8.dp))
			Text(
				text = state.subtitle,
				style = MaterialTheme.typography.bodyLarge,
				color = colorScheme.onSurfaceVariant,
			)
			Spacer(modifier = Modifier.height(20.dp))
			Text(
				text = state.body,
				style = MaterialTheme.typography.bodyLarge,
				color = colorScheme.onSurface,
				modifier = Modifier.fillMaxWidth(),
			)
		}
	}
}
