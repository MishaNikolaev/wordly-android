package com.nmichail.wordly.android.features.materials.article.data.mapper

import com.nmichail.wordly.android.features.materials.article.data.dto.MaterialDetailDto
import com.nmichail.wordly.android.features.materials.article.domain.entity.MaterialCategory
import com.nmichail.wordly.android.features.materials.article.domain.entity.MaterialDetail
import com.nmichail.wordly.android.features.materials.article.domain.entity.MaterialReadStatus

fun MaterialDetailDto.toDomain(): MaterialDetail =
	MaterialDetail(
		id = id,
		category = category.toCategory(),
		typeLabel = typeLabel,
		title = title,
		description = description,
		readingMinutes = readingMinutes,
		dateLabel = dateLabel,
		level = level,
		likes = likes,
		dislikes = dislikes,
		status = status.toReadStatus(),
		photoUrl = photoUrl,
	)

private fun String.toCategory(): MaterialCategory =
	when (uppercase()) {
		"GRAMMAR" -> MaterialCategory.Grammar
		"IDIOMS" -> MaterialCategory.Idioms
		"CONVERSATIONAL" -> MaterialCategory.Conversational
		"LISTENING" -> MaterialCategory.Listening
		else -> MaterialCategory.Grammar
	}

private fun String.toReadStatus(): MaterialReadStatus =
	when (uppercase()) {
		"READ" -> MaterialReadStatus.Read
		else -> MaterialReadStatus.New
	}
