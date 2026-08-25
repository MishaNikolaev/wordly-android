@file:Suppress("MagicNumber")

package com.nmichail.wordly.android.features.materials.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.features.materials.ui.component.MaterialsCardBackground
import com.nmichail.wordly.android.features.materials.ui.component.materialsCardBackgroundStyleFor
import com.nmichail.wordly.android.shared.catalog.CatalogRemoteImage
import com.nmichail.wordly.android.component.wui.theme.Wui
import com.nmichail.wordly.android.features.materials.feed.R
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialCategory
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialItem
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialReadStatus

private val StatusReadAccent = Color(0xFF9CA3AF)
private val CardShape = RoundedCornerShape(10.dp)
private val CardMinHeight = 220.dp

@Composable
internal fun MaterialListItem(
	item: MaterialItem,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme

	Column(
		modifier = modifier
			.fillMaxWidth()
			.defaultMinSize(minHeight = CardMinHeight)
			.clip(CardShape)
			.background(colorScheme.surface)
			.clickable(onClick = onClick)
	) {
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.height(84.dp)
				.clip(RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)),
		) {
			if (item.photoUrl.isNullOrBlank()) {
				MaterialsCardBackground(
					style = materialsCardBackgroundStyleFor(item.id),
					modifier = Modifier.fillMaxSize(),
				)
			} else {
				CatalogRemoteImage(
					url = item.photoUrl,
					modifier = Modifier.fillMaxSize(),
				)
			}
		}
		MaterialListItemText(
			item = item,
			modifier = Modifier
				.fillMaxWidth()
				.padding(horizontal = 10.dp, vertical = 10.dp),
		)
	}
}

@Composable
private fun MaterialCategoryChip(text: String) {
	val primary = MaterialTheme.colorScheme.primary
	Box(
		modifier = Modifier
			.clip(RoundedCornerShape(4.dp))
			.background(Wui.colors.primaryMuted)
			.padding(horizontal = 6.dp, vertical = 2.dp),
	) {
		Text(
			text = text,
			style = MaterialTheme.typography.labelSmall,
			fontWeight = FontWeight.Medium,
			color = primary,
		)
	}
}

@Composable
private fun MaterialListItemText(
	item: MaterialItem,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme

	Column(
		modifier = modifier.fillMaxHeight(),
		verticalArrangement = Arrangement.SpaceBetween,
	) {
		MaterialCategoryChip(text = materialCategoryLabel(item.category))
		Column(modifier = Modifier.padding(top = 10.dp)) {
			Text(
				text = item.title,
				style = MaterialTheme.typography.titleLarge,
				fontWeight = FontWeight.Medium,
				color = colorScheme.onSurface,
				maxLines = 2,
			)
			Text(
				text = item.description,
				style = MaterialTheme.typography.bodySmall,
				color = colorScheme.onSurfaceVariant,
				modifier = Modifier.padding(top = 5.dp),
				maxLines = 2,
			)
		}
		MaterialStatusRow(
			status = item.status,
			modifier = Modifier.padding(top = 6.dp),
		)
	}
}

@Composable
private fun MaterialStatusRow(
	status: MaterialReadStatus,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier,
		verticalAlignment = Alignment.CenterVertically,
	) {
		Box(
			modifier = Modifier
				.size(8.dp)
				.background(materialStatusColor(status), CircleShape),
		)
		Text(
			text = materialStatusLabel(status),
			style = MaterialTheme.typography.bodySmall,
			color = materialStatusColor(status),
			modifier = Modifier.padding(start = 8.dp),
		)
	}
}

@Composable
internal fun materialCategoryLabel(category: MaterialCategory): String =
	when (category) {
		MaterialCategory.Grammar -> stringResource(R.string.materials_category_grammar)
		MaterialCategory.Idioms -> stringResource(R.string.materials_category_idioms)
		MaterialCategory.Conversational -> stringResource(R.string.materials_category_conversational)
		MaterialCategory.Listening -> stringResource(R.string.materials_category_listening)
	}

@Composable
private fun materialStatusLabel(status: MaterialReadStatus): String =
	when (status) {
		MaterialReadStatus.New -> stringResource(R.string.materials_status_new)
		MaterialReadStatus.Read -> stringResource(R.string.materials_status_read)
	}

@Composable
private fun materialStatusColor(status: MaterialReadStatus): Color =
	when (status) {
		MaterialReadStatus.New -> MaterialTheme.colorScheme.primary
		MaterialReadStatus.Read -> StatusReadAccent
	}
