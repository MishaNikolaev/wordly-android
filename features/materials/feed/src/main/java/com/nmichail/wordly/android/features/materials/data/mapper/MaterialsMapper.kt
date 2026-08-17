package com.nmichail.wordly.android.features.materials.data.mapper

import com.nmichail.wordly.android.features.materials.data.dto.MaterialItemDto
import com.nmichail.wordly.android.features.materials.data.dto.MaterialsCatalogDto
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialCategory
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialItem
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialReadStatus
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialsCatalog

fun MaterialsCatalogDto.toDomain(): MaterialsCatalog =
	MaterialsCatalog(
		title = title,
		items = items.map(MaterialItemDto::toDomain),
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
