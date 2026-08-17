package com.nmichail.wordly.android.features.constructor.presentation.detail

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorSession
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorWord

interface ConstructorPracticeStore :
	Store<ConstructorPracticeStore.Intent, ConstructorPracticeStore.State, ConstructorPracticeStore.Label> {

	sealed interface State {

		data object Initial : State

		data object Loading : State

		sealed interface Content : State {

			val correctCount: Int

			val totalCount: Int

			data class InProgress(
				val session: ConstructorSession,
				val currentIndex: Int,
				val bank: List<ConstructorWord>,
				val answer: List<ConstructorWord>,
				val checkResult: Boolean?,
				override val correctCount: Int,
				override val totalCount: Int,
			) : Content

			data class Finished(
				override val correctCount: Int,
				override val totalCount: Int,
			) : Content
		}

		data object Error : State
	}

	sealed interface Label {

		data object Close : Label
	}

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
