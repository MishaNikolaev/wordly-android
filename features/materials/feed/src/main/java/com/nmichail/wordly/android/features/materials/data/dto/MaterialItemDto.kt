package com.nmichail.wordly.android.features.materials.data.dto

data class MaterialItemDto(
	val id: String,
	val category: String,
	val title: String,
	val description: String,
	val photoUrl: String?,
	val status: String,
)