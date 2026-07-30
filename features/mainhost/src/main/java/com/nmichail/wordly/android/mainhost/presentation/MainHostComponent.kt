package com.nmichail.wordly.android.mainhost.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.books.presentation.BooksComponent
import com.nmichail.wordly.android.features.books.presentation.detail.BookReaderComponent
import com.nmichail.wordly.android.features.cards.presentation.CardsComponent
import com.nmichail.wordly.android.features.cards.presentation.detail.CardPracticeComponent
import com.nmichail.wordly.android.features.constructor.presentation.ConstructorComponent
import com.nmichail.wordly.android.features.constructor.presentation.detail.ConstructorPracticeComponent
import com.nmichail.wordly.android.features.home.presentation.HomeComponent
import com.nmichail.wordly.android.features.materials.presentation.MaterialsComponent
import com.nmichail.wordly.android.features.materials.presentation.detail.MaterialDetailComponent
import com.nmichail.wordly.android.features.review.presentation.ReviewComponent
import com.nmichail.wordly.android.features.words.presentation.WordsComponent

interface MainHostComponent {

	val stack: Value<ChildStack<*, Child>>

	fun handleSelectTab(tab: MainHostTab)

	sealed interface Child {

		data class Home(val component: HomeComponent) : Child

		data class Words(val component: WordsComponent) : Child

		data class Materials(val component: MaterialsComponent) : Child

		data class MaterialDetail(val component: MaterialDetailComponent) : Child

		data object Profile : Child

		data class Review(val component: ReviewComponent) : Child

		data class Cards(val component: CardsComponent) : Child

		data class CardPractice(val component: CardPracticeComponent) : Child

		data class Constructor(val component: ConstructorComponent) : Child

		data class ConstructorPractice(val component: ConstructorPracticeComponent) : Child

		data class Books(val component: BooksComponent) : Child

		data class BookReader(val component: BookReaderComponent) : Child
	}

	fun interface Factory {

		operator fun invoke(componentContext: ComponentContext): MainHostComponent
	}
}

enum class MainHostTab {
	Home,
	Words,
	Materials,
	Profile,
}
