package com.nmichail.wordly.android.features.materials.data.dto

data class MaterialDetailDto(
	val id: String,
	val category: String,
	val typeLabel: String,
	val title: String,
	val description: String,
	val readingMinutes: Int,
	val dateLabel: String,
	val level: String,
	val likes: Int,
	val dislikes: Int,
	val status: String,
)