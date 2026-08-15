package com.nmichail.wordly.android.component.wui.components.card

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.wui.components.chip.WuiStatusChip
import com.nmichail.wordly.android.component.wui.components.chip.WuiStatusChipStyle
import com.nmichail.wordly.android.component.wui.theme.WuiTheme
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.nmichail.wordly.android.component.wui.theme.PreviewTheme
import com.nmichail.wordly.android.component.wui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.wui.theme.WuiPreviews
import com.nmichail.wordly.android.component.wui.theme.WuiTypography

@Composable
fun WuiCatalogCard(
	title: String,
	subtitle: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	badge: String? = null,
	image: @Composable () -> Unit = {
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(MaterialTheme.colorScheme.surfaceVariant),
		)
	},
) {
	val colorScheme = MaterialTheme.colorScheme

	WuiAppCard(
		modifier = modifier.fillMaxWidth(),
		onClick = onClick,
		contentPadding = PaddingValues(horizontal = 14.dp, vertical = 12.dp),
	) {
		Row(
			modifier = Modifier.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(12.dp),
		) {
			Box(
				modifier = Modifier
					.size(52.dp)
					.clip(MaterialTheme.shapes.medium),
			) {
				image()
			}
			Column(modifier = Modifier.weight(1f)) {
				Text(
					text = title,
					style = WuiTypography.trainingTileTitle,
					color = colorScheme.onSurface,
				)
				Row(
					modifier = Modifier.padding(top = 4.dp),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.spacedBy(8.dp),
				) {
					Text(
						text = subtitle,
						style = WuiTypography.trainingTileSubtitle,
						color = colorScheme.onSurfaceVariant,
					)
					if (!badge.isNullOrBlank()) {
						WuiStatusChip(
							text = badge,
							style = WuiStatusChipStyle.Accent,
						)
					}
				}
			}
			Icon(
				imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
				contentDescription = null,
				tint = colorScheme.onSurfaceVariant,
				modifier = Modifier.size(22.dp),
			)
		}
	}
}

@WuiPreviews
@Composable
private fun CatalogCardPreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WuiTheme(darkTheme = theme == PreviewTheme.Dark) {
		WuiCatalogCard(
			title = "Наука",
			subtitle = "12 наборов",
			badge = "Новое",
			onClick = {},
		)
	}
}