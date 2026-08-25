package com.nmichail.wordly.android.features.books.reader.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.features.books.reader.R

@Composable
internal fun BookReaderTopBar(
	title: String,
	author: String,
	translating: Boolean,
	translated: Boolean,
	onCloseClick: () -> Unit,
	onToggleTranslate: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = 16.dp, vertical = 12.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		BookReaderBackButton(onClick = onCloseClick)
		Column(
			modifier = Modifier
				.weight(1f)
				.padding(horizontal = 12.dp),
			horizontalAlignment = Alignment.CenterHorizontally,
		) {
			Text(
				text = title,
				style = MaterialTheme.typography.titleMedium,
				fontWeight = FontWeight.SemiBold,
				color = MaterialTheme.colorScheme.onBackground,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis,
				textAlign = TextAlign.Center,
			)
			Text(
				text = author,
				style = MaterialTheme.typography.labelMedium,
				color = MaterialTheme.colorScheme.onSurfaceVariant,
				maxLines = 1,
				overflow = TextOverflow.Ellipsis,
				textAlign = TextAlign.Center,
			)
		}
		BookTranslateButton(
			translating = translating,
			translated = translated,
			contentDescription = stringResource(
				if (translated) {
					R.string.book_reader_hide_translation
				} else {
					R.string.book_reader_translate
				},
			),
			onClick = onToggleTranslate,
		)
	}
}

@Composable
private fun BookReaderBackButton(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier
			.size(40.dp)
			.clip(RoundedCornerShape(12.dp))
			.background(MaterialTheme.colorScheme.surfaceVariant)
			.clickable(
				role = Role.Button,
				onClick = onClick,
			),
		contentAlignment = Alignment.Center,
	) {
		Icon(
			imageVector = Icons.AutoMirrored.Filled.ArrowBack,
			contentDescription = stringResource(R.string.book_reader_close),
			tint = MaterialTheme.colorScheme.onBackground,
			modifier = Modifier.size(22.dp),
		)
	}
}