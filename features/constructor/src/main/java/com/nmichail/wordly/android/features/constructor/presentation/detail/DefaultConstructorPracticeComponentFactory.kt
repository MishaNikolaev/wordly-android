package com.nmichail.wordly.android.features.constructor.presentation.detail

import com.arkivanov.decompose.ComponentContext
import javax.inject.Inject

internal class DefaultConstructorPracticeComponentFactory @Inject constructor(
	private val constructorPracticeStoreFactory: ConstructorPracticeStoreFactory,
) : ConstructorPracticeComponent.Factory {

	override fun invoke(
		componentContext: ComponentContext,
		themeId: String,
		constructorPracticeRouter: ConstructorPracticeRouter,
	): ConstructorPracticeComponent =
		DefaultConstructorPracticeComponent(
			componentContext = componentContext,
			themeId = themeId,
			constructorPracticeStoreFactory = constructorPracticeStoreFactory,
			constructorPracticeRouter = constructorPracticeRouter,
		)
}
