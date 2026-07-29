package com.nmichail.wordly.android.features.words.data.mapper

import com.nmichail.wordly.android.features.words.data.dto.DictionaryEntryDto
import com.nmichail.wordly.android.features.words.domain.entity.WordExample
import com.nmichail.wordly.android.features.words.domain.entity.WordLookup

fun List<DictionaryEntryDto>.toWordLookup(query: String): WordLookup {
	val entry = firstOrNull()
	val definitions = entry?.meanings
		.orEmpty()
		.flatMap { meaning -> meaning.definitions.orEmpty() }
	val definitionTexts = definitions
		.mapNotNull { it.definition?.trim()?.takeIf(String::isNotEmpty) }
	val examples = definitions
		.mapNotNull { it.example?.trim()?.takeIf(String::isNotEmpty) }
		.distinct()
		.map { WordExample(text = it, translation = null) }
	val phonetic = entry?.phonetic?.takeIf(String::isNotBlank)
		?: entry?.phonetics
			.orEmpty()
			.mapNotNull { it.text?.takeIf(String::isNotBlank) }
			.firstOrNull()

	return WordLookup(
		word = entry?.word?.takeIf(String::isNotBlank) ?: query.trim(),
		phonetic = phonetic,
		// TODO: Free Dictionary API не отправляет перевод на русский, нужно решить эту проблему
		translation = null,
		definition = definitionTexts.joinToString(separator = "; ").ifBlank { null },
		examples = examples,
		difficulty = 2,
	)
}