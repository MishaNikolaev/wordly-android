package com.nmichail.wordly.android.features.dev.networkselection.presentation

import com.arkivanov.decompose.ComponentContext
import javax.inject.Inject

internal class DefaultNetworkSelectionComponentFactory @Inject constructor(
	private val networkSelectionStoreFactory: NetworkSelectionStoreFactory,
	private val networkSelectionRouter: NetworkSelectionRouter,
) : NetworkSelectionComponent.Factory {

	override fun invoke(
		componentContext: ComponentContext,
		onFinished: () -> Unit,
	): NetworkSelectionComponent =
		DefaultNetworkSelectionComponent(
			componentContext = componentContext,
			networkSelectionStoreFactory = networkSelectionStoreFactory,
			onFinished = onFinished,
			networkSelectionRouter = networkSelectionRouter,
		)
}
