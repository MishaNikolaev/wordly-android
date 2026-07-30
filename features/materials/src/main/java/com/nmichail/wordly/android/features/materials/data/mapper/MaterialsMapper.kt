package com.nmichail.wordly.android.features.materials.data.mapper

import com.nmichail.wordly.android.features.materials.data.dto.MaterialDetailDto
import com.nmichail.wordly.android.features.materials.data.dto.MaterialItemDto
import com.nmichail.wordly.android.features.materials.data.dto.MaterialsCatalogDto
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialCategory
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialDetail
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialItem
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialReadStatus
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialsCatalog

fun MaterialsCatalogDto.toDomain(): MaterialsCatalog =
	MaterialsCatalog(
		title = title,
		items = items.map(MaterialItemDto::toDomain),
	)

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
	)

private fun MaterialItemDto.toDomain(): MaterialItem =
	MaterialItem(
		id = id,
		category = category.toCategory(),
		title = title,
		description = description,
		status = status.toReadStatus(),
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