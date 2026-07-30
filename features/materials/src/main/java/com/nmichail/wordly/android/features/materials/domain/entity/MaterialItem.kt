package com.nmichail.wordly.android.features.materials.domain.entity

data class MaterialItem(
	val id: String,
	val category: MaterialCategory,
	val title: String,
	val description: String,
	val status: MaterialReadStatus,
)