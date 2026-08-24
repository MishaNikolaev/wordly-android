package com.nmichail.wordly.android.features.books.reader.domain

internal const val FREE_WORD_PREFIX = "w:"

internal fun normalizeLookupWord(raw: String): String =
	raw
		.trim()
		.lowercase()
		.trim('\'', '’')
