package com.nmichail.wordly.android.features.materials.article.domain.entity

data class MaterialDetail(
	val id: String,
	val category: MaterialCategory,
	val typeLabel: String,
	val title: String,
	val description: String,
	val readingMinutes: Int,
	val dateLabel: String,
	val level: String,
	val likes: Int,
	val dislikes: Int,
	val status: MaterialReadStatus,
)