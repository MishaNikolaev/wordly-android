package com.nmichail.wordly.android.shared.converters.dateconverter

enum class DatePattern(val pattern: String) {
	DATE("yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS'Z'"),
	YEAR("yyyy"),
	MONTH("MM"),
	DAY("dd"),
	TIME("HH:mm"),
}