package com.nmichail.wordly.android.features.words.ui.dialog

import com.nmichail.wordly.android.component.wui.theme.Wui
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.features.words.list.R
import com.nmichail.wordly.android.features.words.domain.entity.WordTag

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun AddWordTags(
	tags: List<WordTag>,
	selectedTagIds: Set<String>,
	onToggleTag: (String) -> Unit,
) {
	FlowRow(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.spacedBy(6.dp),
		verticalArrangement = Arrangement.spacedBy(6.dp),
	) {
		tags.forEachIndexed { index, tag ->
			TagChip(
				tag = tag,
				selected = tag.id in selectedTagIds,
				paletteIndex = index,
				onClick = { onToggleTag(tag.id) },
			)
		}
	}
}

@Composable
internal fun AddToDictionaryButton(
	onClick: () -> Unit,
	enabled: Boolean,
	loading: Boolean,
) {
	val colorScheme = MaterialTheme.colorScheme
	Button(
		onClick = onClick,
		modifier = Modifier
			.fillMaxWidth()
			.height(48.dp),
		enabled = enabled && !loading,
		shape = MaterialTheme.shapes.small,
		colors = ButtonDefaults.buttonColors(
			containerColor = colorScheme.primary,
			contentColor = colorScheme.onPrimary,
			disabledContainerColor = colorScheme.primary.copy(alpha = 0.4f),
			disabledContentColor = colorScheme.onPrimary.copy(alpha = 0.6f),
		),
	) {
		if (loading) {
			CircularProgressIndicator(
				modifier = Modifier.size(22.dp),
				color = colorScheme.onPrimary,
				strokeWidth = 2.dp,
			)
		} else {
			Text(
				text = stringResource(R.string.words_add_confirm),
				style = MaterialTheme.typography.labelLarge,
				fontWeight = FontWeight.SemiBold,
			)
		}
	}
}

@Composable
private fun TagChip(
	tag: WordTag,
	selected: Boolean,
	paletteIndex: Int,
	onClick: () -> Unit,
) {
	val palette = tagPalette(paletteIndex)
	val background = if (selected) palette.copy(alpha = 0.22f) else palette.copy(alpha = 0.12f)
	val content = palette
	val border = if (selected) palette else Color.Transparent

	Text(
		text = "#${tag.title}",
		style = MaterialTheme.typography.labelSmall,
		fontWeight = FontWeight.Medium,
		color = content,
		modifier = Modifier
			.clip(RoundedCornerShape(percent = 50))
			.background(background)
			.border(1.dp, border, RoundedCornerShape(percent = 50))
			.clickable(role = Role.Button, onClick = onClick)
			.padding(horizontal = 8.dp, vertical = 4.dp),
	)
}

@Composable
private fun tagPalette(index: Int): Color {
	val colorScheme = MaterialTheme.colorScheme
	val extended = Wui.colors
	val colors = listOf(
		colorScheme.primary,
		colorScheme.secondary,
		extended.success,
		extended.warning,
	)
	return colors[index % colors.size]
}