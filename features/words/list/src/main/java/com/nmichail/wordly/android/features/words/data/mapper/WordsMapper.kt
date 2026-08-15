package com.nmichail.wordly.android.features.words.data.mapper

import com.nmichail.wordly.android.features.words.data.dto.AddWordBody
import com.nmichail.wordly.android.features.words.data.dto.WordExampleDto
import com.nmichail.wordly.android.features.words.data.dto.WordItemDto
import com.nmichail.wordly.android.features.words.data.dto.WordTagDto
import com.nmichail.wordly.android.features.words.data.dto.WordsCatalogDto
import com.nmichail.wordly.android.features.words.domain.entity.NewWord
import com.nmichail.wordly.android.features.words.domain.entity.WordExample
import com.nmichail.wordly.android.features.words.domain.entity.WordItem
import com.nmichail.wordly.android.features.words.domain.entity.WordStatus
import com.nmichail.wordly.android.features.words.domain.entity.WordTag
import com.nmichail.wordly.android.features.words.domain.entity.WordsCatalog

fun WordsCatalogDto.toDomain(): WordsCatalog =
	WordsCatalog(
		title = title,
		searchPlaceholder = searchPlaceholder,
		words = words.map(WordItemDto::toDomain),
		tags = tags.map(WordTagDto::toDomain),
	)

fun NewWord.toBody(): AddWordBody =
	AddWordBody(
		word = word,
		phonetic = phonetic,
		translation = translation,
		definition = definition,
		examples = examples.map(WordExample::toDto),
		tagIds = tagIds,
		difficulty = difficulty,
	)

private fun WordItemDto.toDomain(): WordItem =
	WordItem(
		id = id,
		word = word,
		phonetic = phonetic,
		translation = translation,
		definition = definition,
		status = status.toWordStatus(),
		tags = tags,
		examples = examples.map(WordExampleDto::toDomain),
		difficulty = difficulty,
		repeatEpochDay = repeatEpochDay,
	)

private fun WordTagDto.toDomain(): WordTag =
	WordTag(
		id = id,
		title = title,
	)

private fun WordExampleDto.toDomain(): WordExample =
	WordExample(
		text = text,
		translation = translation,
	)

private fun WordExample.toDto(): WordExampleDto =
	WordExampleDto(
		text = text,
		translation = translation,
	)

private fun String.toWordStatus(): WordStatus =
	when (uppercase()) {
		"NEW" -> WordStatus.New
		"IN_PROGRESS" -> WordStatus.InProgress
		"LEARNED" -> WordStatus.Learned
		else -> WordStatus.New
	}