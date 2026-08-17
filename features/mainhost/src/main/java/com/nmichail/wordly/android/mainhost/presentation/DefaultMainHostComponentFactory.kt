package com.nmichail.wordly.android.mainhost.presentation

import com.arkivanov.decompose.ComponentContext
import com.nmichail.wordly.android.features.books.presentation.BooksComponent
import com.nmichail.wordly.android.features.books.reader.presentation.BookReaderComponent
import com.nmichail.wordly.android.features.cards.presentation.CardsComponent
import com.nmichail.wordly.android.features.cards.training.presentation.CardPracticeComponent
import com.nmichail.wordly.android.features.constructor.presentation.ConstructorComponent
import com.nmichail.wordly.android.features.constructor.practice.presentation.ConstructorPracticeComponent
import com.nmichail.wordly.android.features.home.presentation.HomeComponent
import com.nmichail.wordly.android.features.materials.presentation.MaterialsComponent
import com.nmichail.wordly.android.features.materials.article.presentation.MaterialDetailComponent
import com.nmichail.wordly.android.features.profile.presentation.ProfileComponent
import com.nmichail.wordly.android.features.profile.presentation.edit.ProfileEditComponent
import com.nmichail.wordly.android.features.profile.presentation.reminder.ReminderTimesComponent
import com.nmichail.wordly.android.features.review.presentation.ReviewComponent
import com.nmichail.wordly.android.features.words.presentation.WordsComponent
import javax.inject.Inject

internal class DefaultMainHostComponentFactory @Inject constructor(
	private val homeComponentFactory: HomeComponent.Factory,
	private val wordsComponentFactory: WordsComponent.Factory,
	private val materialsComponentFactory: MaterialsComponent.Factory,
	private val materialDetailComponentFactory: MaterialDetailComponent.Factory,
	private val profileComponentFactory: ProfileComponent.Factory,
	private val profileEditComponentFactory: ProfileEditComponent.Factory,
	private val reminderTimesComponentFactory: ReminderTimesComponent.Factory,
	private val reviewComponentFactory: ReviewComponent.Factory,
	private val cardsComponentFactory: CardsComponent.Factory,
	private val cardPracticeComponentFactory: CardPracticeComponent.Factory,
	private val constructorComponentFactory: ConstructorComponent.Factory,
	private val constructorPracticeComponentFactory: ConstructorPracticeComponent.Factory,
	private val booksComponentFactory: BooksComponent.Factory,
	private val bookReaderComponentFactory: BookReaderComponent.Factory,
) : MainHostComponent.Factory {

	override fun invoke(componentContext: ComponentContext): MainHostComponent =
		DefaultMainHostComponent(
			componentContext = componentContext,
			homeComponentFactory = homeComponentFactory,
			wordsComponentFactory = wordsComponentFactory,
			materialsComponentFactory = materialsComponentFactory,
			materialDetailComponentFactory = materialDetailComponentFactory,
			profileComponentFactory = profileComponentFactory,
			profileEditComponentFactory = profileEditComponentFactory,
			reminderTimesComponentFactory = reminderTimesComponentFactory,
			reviewComponentFactory = reviewComponentFactory,
			cardsComponentFactory = cardsComponentFactory,
			cardPracticeComponentFactory = cardPracticeComponentFactory,
			constructorComponentFactory = constructorComponentFactory,
			constructorPracticeComponentFactory = constructorPracticeComponentFactory,
			booksComponentFactory = booksComponentFactory,
			bookReaderComponentFactory = bookReaderComponentFactory,
		)
}