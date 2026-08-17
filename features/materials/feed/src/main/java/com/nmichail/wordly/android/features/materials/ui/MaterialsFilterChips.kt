package com.nmichail.wordly.android.features.materials.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nmichail.wordly.android.component.wui.theme.Wui
import com.nmichail.wordly.android.features.materials.feed.R
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialFilter

@Composable
internal fun MaterialsFilterChips(
	selected: MaterialFilter,
	onSelect: (MaterialFilter) -> Unit,
	modifier: Modifier = Modifier,
) {
	val filters = listOf(
		MaterialFilter.All to stringResource(R.string.materials_filter_all),
		MaterialFilter.Grammar to stringResource(R.string.materials_filter_grammar),
		MaterialFilter.Idioms to stringResource(R.string.materials_filter_idioms),
		MaterialFilter.Conversational to stringResource(R.string.materials_filter_conversational),
		MaterialFilter.Listening to stringResource(R.string.materials_filter_listening),
	)

	LazyRow(
		modifier = modifier.fillMaxWidth(),
		contentPadding = PaddingValues(horizontal = 16.dp),
		horizontalArrangement = Arrangement.spacedBy(6.dp),
	) {
		items(filters) { (filter, title) ->
			MaterialsFilterChip(
				text = title,
				selected = filter == selected,
				onClick = { onSelect(filter) },
			)
		}
	}
}

@Composable
private fun MaterialsFilterChip(
	text: String,
	selected: Boolean,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme
	val background = if (selected) {
		Wui.colors.primaryMuted
	} else {
		colorScheme.surfaceVariant.copy(alpha = 0.55f)
	}
	val content = colorScheme.onSurface

	Box(
		modifier = modifier
			.height(32.dp)
			.defaultMinSize(minWidth = 58.dp)
			.clip(RoundedCornerShape(percent = 50))
			.background(background)
			.clickable(role = Role.Button, onClick = onClick)
			.padding(horizontal = 12.dp),
		contentAlignment = Alignment.Center,
	) {
		Text(
			text = text,
			fontSize = 14.sp,
			lineHeight = 18.sp,
			fontWeight = FontWeight.Medium,
			color = content,
			maxLines = 1,
		)
	}
}
