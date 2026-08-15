package com.nmichail.wordly.android.features.constructor.presentation.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value

interface ConstructorPracticeComponent {

	val model: Value<ConstructorPracticeStore.State>

	fun handleClose()

	fun handleRetry()

	fun handlePlaceWord(wordId: String)

	fun handleRemoveWord(wordId: String)

	fun handleMoveAnswerWord(fromIndex: Int, toIndex: Int)

	fun handleCheck()

	fun handleContinue()

	fun handleFinish()

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			themeId: String,
			constructorPracticeRouter: ConstructorPracticeRouter,
		): ConstructorPracticeComponent
	}
}
