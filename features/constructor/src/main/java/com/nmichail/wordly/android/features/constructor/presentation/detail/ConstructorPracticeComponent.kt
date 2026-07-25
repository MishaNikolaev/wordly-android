package com.nmichail.wordly.android.features.constructor.presentation.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorSession
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorWord

interface ConstructorPracticeComponent {

	val model: Value<State>

	fun handleClose()

	fun handleRetry()

	fun handlePlaceWord(wordId: String)

	fun handleRemoveWord(wordId: String)

	fun handleMoveAnswerWord(fromIndex: Int, toIndex: Int)

	fun handleCheck()

	fun handleContinue()

	fun handleFinish()

	sealed interface State {

		data object Loading : State

		data object Error : State

		data class InProgress(
			val session: ConstructorSession,
			val currentIndex: Int,
			val bank: List<ConstructorWord>,
			val answer: List<ConstructorWord>,
			val checkResult: Boolean?,
			val correctCount: Int,
		) : State

		data class Finished(
			val totalCount: Int,
			val correctCount: Int,
		) : State
	}

	sealed interface Label {

		data object Close : Label
	}

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			themeId: String,
			constructorPracticeRouter: ConstructorPracticeRouter,
		): ConstructorPracticeComponent
	}
}
