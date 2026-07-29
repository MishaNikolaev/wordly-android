package com.nmichail.wordly.android.mainhost.presentation

import com.arkivanov.decompose.ComponentContext
import com.nmichail.wordly.android.features.books.presentation.BooksComponent
import com.nmichail.wordly.android.features.books.presentation.detail.BookReaderComponent
import com.nmichail.wordly.android.features.cards.presentation.CardsComponent
import com.nmichail.wordly.android.features.cards.presentation.detail.CardPracticeComponent
import com.nmichail.wordly.android.features.constructor.presentation.ConstructorComponent
import com.nmichail.wordly.android.features.constructor.presentation.detail.ConstructorPracticeComponent
import com.nmichail.wordly.android.features.home.presentation.HomeComponent
import com.nmichail.wordly.android.features.news.presentation.NewsDetailComponent
import com.nmichail.wordly.android.features.review.presentation.ReviewComponent
import com.nmichail.wordly.android.features.words.presentation.WordsComponent
import javax.inject.Inject

internal class DefaultMainHostComponentFactory @Inject constructor(
	private val homeComponentFactory: HomeComponent.Factory,
	private val wordsComponentFactory: WordsComponent.Factory,
	private val reviewComponentFactory: ReviewComponent.Factory,
	private val cardsComponentFactory: CardsComponent.Factory,
	private val cardPracticeComponentFactory: CardPracticeComponent.Factory,
	private val constructorComponentFactory: ConstructorComponent.Factory,
	private val constructorPracticeComponentFactory: ConstructorPracticeComponent.Factory,
	private val booksComponentFactory: BooksComponent.Factory,
	private val bookReaderComponentFactory: BookReaderComponent.Factory,
	private val newsDetailComponentFactory: NewsDetailComponent.Factory,
) : MainHostComponent.Factory {

	override fun invoke(componentContext: ComponentContext): MainHostComponent =
		DefaultMainHostComponent(
			componentContext = componentContext,
			homeComponentFactory = homeComponentFactory,
			wordsComponentFactory = wordsComponentFactory,
			reviewComponentFactory = reviewComponentFactory,
			cardsComponentFactory = cardsComponentFactory,
			cardPracticeComponentFactory = cardPracticeComponentFactory,
			constructorComponentFactory = constructorComponentFactory,
			constructorPracticeComponentFactory = constructorPracticeComponentFactory,
			booksComponentFactory = booksComponentFactory,
			bookReaderComponentFactory = bookReaderComponentFactory,
			newsDetailComponentFactory = newsDetailComponentFactory,
		)
}
