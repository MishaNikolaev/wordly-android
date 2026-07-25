package com.nmichail.wordly.android.features.constructor.presentation.detail

import com.arkivanov.mvikotlin.core.store.Store

internal interface ConstructorPracticeStore :
	Store<ConstructorPracticeStore.Intent, ConstructorPracticeComponent.State, ConstructorPracticeComponent.Label> {

	sealed interface Intent {

		data object Close : Intent

		data object Retry : Intent

		data class PlaceWord(val wordId: String) : Intent

		data class RemoveWord(val wordId: String) : Intent

		data class MoveAnswerWord(
			val fromIndex: Int,
			val toIndex: Int,
		) : Intent

		data object Check : Intent

		data object Continue : Intent

		data object Finish : Intent
	}
}