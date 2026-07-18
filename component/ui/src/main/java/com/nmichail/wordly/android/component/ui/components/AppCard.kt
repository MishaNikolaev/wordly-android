package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppCard(
	modifier: Modifier = Modifier,
	onClick: (() -> Unit)? = null,
	contentPadding: PaddingValues = PaddingValues(16.dp),
	content: @Composable ColumnScope.() -> Unit,
) {
	val shape = MaterialTheme.shapes.extraLarge
	val colors = CardDefaults.cardColors(
		containerColor = MaterialTheme.colorScheme.surface,
	)
	val elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
	val body: @Composable () -> Unit = {
		Column(
			modifier = Modifier.padding(contentPadding),
			content = content,
		)
	}

	if (onClick != null) {
		Card(
			onClick = onClick,
			modifier = modifier,
			shape = shape,
			colors = colors,
			elevation = elevation,
			content = { body() },
		)
	} else {
		Card(
			modifier = modifier,
			shape = shape,
			colors = colors,
			elevation = elevation,
			content = { body() },
		)
	}
}
