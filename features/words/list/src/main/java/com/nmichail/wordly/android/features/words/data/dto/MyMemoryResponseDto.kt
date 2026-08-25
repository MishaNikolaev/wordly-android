package com.nmichail.wordly.android.features.words.data.dto

data class MyMemoryResponseDto(
	val responseData: MyMemoryResponseDataDto?,
)

data class MyMemoryResponseDataDto(
	val translatedText: String?,
)
