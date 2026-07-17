package com.nmichail.wordly.android.features.home.domain.entity

data class Month(
	val title: String,
	val days: List<MonthDay?>,
	val activeDays: Int,
	val completionPercent: Int,
)
