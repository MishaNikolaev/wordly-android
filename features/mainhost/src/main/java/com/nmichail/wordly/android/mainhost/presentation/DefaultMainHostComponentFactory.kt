package com.nmichail.wordly.android.mainhost.presentation

import com.arkivanov.decompose.ComponentContext
import com.nmichail.wordly.android.features.cards.presentation.CardsComponent
import com.nmichail.wordly.android.features.cards.presentation.detail.CardPracticeComponent
import com.nmichail.wordly.android.features.home.presentation.HomeComponent
import com.nmichail.wordly.android.features.news.presentation.NewsDetailComponent
import com.nmichail.wordly.android.features.review.presentation.ReviewComponent
import javax.inject.Inject

internal class DefaultMainHostComponentFactory @Inject constructor(
	private val homeComponentFactory: HomeComponent.Factory,
	private val reviewComponentFactory: ReviewComponent.Factory,
	private val cardsComponentFactory: CardsComponent.Factory,
	private val cardPracticeComponentFactory: CardPracticeComponent.Factory,
	private val newsDetailComponentFactory: NewsDetailComponent.Factory,
) : MainHostComponent.Factory {

	override fun invoke(componentContext: ComponentContext): MainHostComponent =
		DefaultMainHostComponent(
			componentContext = componentContext,
			homeComponentFactory = homeComponentFactory,
			reviewComponentFactory = reviewComponentFactory,
			cardsComponentFactory = cardsComponentFactory,
			cardPracticeComponentFactory = cardPracticeComponentFactory,
			newsDetailComponentFactory = newsDetailComponentFactory,
		)
}
