package com.nmichail.wordly.android.features.words.data.dto

data class DictionaryMeaningDto(
	val partOfSpeech: String?,
	val definitions: List<DictionaryDefinitionDto>?,
)