package com.nmichail.wordly.android.features.materials.article.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.sp
import com.nmichail.wordly.android.component.wui.theme.WuiBrushes
import com.nmichail.wordly.android.component.wui.theme.isAppInDarkTheme
import com.nmichail.wordly.android.features.materials.article.R
import com.nmichail.wordly.android.features.materials.article.domain.entity.MaterialCategory
import com.nmichail.wordly.android.features.materials.article.domain.entity.MaterialDetail

@Composable
internal fun MaterialDetailHero(
	material: MaterialDetail,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.fillMaxWidth()
			.clip(RoundedCornerShape(24.dp))
			.background(WuiBrushes.MaterialHero)
			.padding(20.dp),
	) {
		MaterialHeroTagsRow(material = material)
		Text(
			text = material.title,
			style = MaterialTheme.typography.headlineSmall,
			fontWeight = FontWeight.Normal,
			color = Color.White,
			modifier = Modifier.padding(top = 16.dp),
		)
		MaterialHeroMetaRow(material = material)
	}
}

@Composable
private fun MaterialHeroTagsRow(material: MaterialDetail) {
	val categoryBackground = if (isAppInDarkTheme()) {
		Color(0xFF16332B)
	} else {
		Color(0xFFA7F3D0)
	}
	val categoryContent = if (isAppInDarkTheme()) {
		Color(0xFF4ADE9A)
	} else {
		Color(0xFF065F46)
	}
	Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
		HeroTag(
			text = materialCategoryLabel(material.category),
			background = categoryBackground,
			contentColor = categoryContent,
		)
		HeroTag(
			text = material.typeLabel,
			background = Color.White.copy(alpha = 0.18f),
			contentColor = Color.White,
		)
	}
}

@Composable
private fun MaterialHeroMetaRow(material: MaterialDetail) {
	Column(
		modifier = Modifier.padding(top = 16.dp),
	) {
		Row(
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(6.dp),
		) {
			Icon(
				imageVector = Icons.Outlined.Schedule,
				contentDescription = null,
				tint = Color.White.copy(alpha = 0.9f),
				modifier = Modifier.size(14.dp),
			)
			Text(
				text = stringResource(
					R.string.materials_detail_reading_minutes,
					material.readingMinutes,
				),
				style = MaterialTheme.typography.bodySmall,
				fontWeight = FontWeight.Normal,
				color = Color.White.copy(alpha = 0.9f),
			)
			Text(
				text = "•",
				style = MaterialTheme.typography.bodySmall,
				fontWeight = FontWeight.Normal,
				color = Color.White.copy(alpha = 0.8f),
			)
			Text(
				text = material.dateLabel,
				style = MaterialTheme.typography.bodySmall,
				fontWeight = FontWeight.Normal,
				color = Color.White.copy(alpha = 0.9f),
			)
		}
		Box(
			modifier = Modifier
				.padding(top = 8.dp)
				.clip(RoundedCornerShape(8.dp))
				.background(Color.White.copy(alpha = 0.2f))
				.padding(horizontal = 10.dp, vertical = 5.dp),
		) {
			Text(
				text = material.level,
				style = MaterialTheme.typography.labelMedium,
				fontWeight = FontWeight.Normal,
				color = Color.White,
			)
		}
	}
}

@Composable
private fun HeroTag(
	text: String,
	background: Color,
	contentColor: Color,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier
			.clip(RoundedCornerShape(percent = 50))
			.background(background)
			.padding(horizontal = 10.dp, vertical = 5.dp),
	) {
		Text(
			text = text,
			fontSize = 11.sp,
			lineHeight = 13.sp,
			fontWeight = FontWeight.Normal,
			color = contentColor,
		)
	}
}

@Composable
private fun materialCategoryLabel(category: MaterialCategory): String =
	when (category) {
		MaterialCategory.Grammar -> stringResource(R.string.materials_category_grammar)
		MaterialCategory.Idioms -> stringResource(R.string.materials_category_idioms)
		MaterialCategory.Conversational -> stringResource(R.string.materials_category_conversational)
		MaterialCategory.Listening -> stringResource(R.string.materials_category_listening)
	}
