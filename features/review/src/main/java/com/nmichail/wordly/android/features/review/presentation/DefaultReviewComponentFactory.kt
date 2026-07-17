package com.nmichail.wordly.android.features.review.presentation

import com.arkivanov.decompose.ComponentContext
import javax.inject.Inject

internal class DefaultReviewComponentFactory @Inject constructor(
	private val reviewStoreFactory: ReviewStoreFactory,
) : ReviewComponent.Factory {

	override fun invoke(
		componentContext: ComponentContext,
		reviewRouter: ReviewRouter,
	): ReviewComponent =
		DefaultReviewComponent(
			componentContext = componentContext,
			reviewStoreFactory = reviewStoreFactory,
			reviewRouter = reviewRouter,
		)
}
