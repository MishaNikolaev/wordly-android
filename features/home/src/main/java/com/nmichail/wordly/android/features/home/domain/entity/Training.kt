package com.nmichail.wordly.android.features.home.domain.entity

data class Training(
	val id: String,
)

object TrainingId {
	const val Cards = "cards"
	const val Constructor = "constructor"
	const val Listening = "listening"
	const val Songs = "songs"
	const val Movies = "movies"
	const val Books = "books"

	val all: Set<String> = setOf(
		Cards,
		Constructor,
		Listening,
		Songs,
		Movies,
		Books,
	)
}
