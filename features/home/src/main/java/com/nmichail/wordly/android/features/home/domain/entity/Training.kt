package com.nmichail.wordly.android.features.home.domain.entity

sealed interface TrainingType {

	data object Cards : TrainingType

	data object Constructor : TrainingType

	data object Books : TrainingType
}

data class Training(
	val type: TrainingType,
	val title: String,
	val subtitle: String,
)
