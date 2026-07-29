package com.nmichail.wordly.android.features.words.data.dto

data class DictionaryEntryDto(
	val word: String?,
	val phonetic: String?,
	val phonetics: List<DictionaryPhoneticDto>?,
	val meanings: List<DictionaryMeaningDto>?,
)