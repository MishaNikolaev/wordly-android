package com.nmichail.wordly.android.features.home.presentation

import com.arkivanov.decompose.ComponentContext
import javax.inject.Inject

internal class DefaultHomeComponentFactory @Inject constructor(
	private val homeStoreFactory: HomeStoreFactory,
) : HomeComponent.Factory {

	override fun invoke(
		componentContext: ComponentContext,
		homeRouter: HomeRouter,
	): HomeComponent =
		DefaultHomeComponent(
			componentContext = componentContext,
			homeStoreFactory = homeStoreFactory,
			homeRouter = homeRouter,
		)
}