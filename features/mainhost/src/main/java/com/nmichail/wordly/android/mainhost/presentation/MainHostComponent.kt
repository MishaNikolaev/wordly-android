package com.nmichail.wordly.android.mainhost.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.home.presentation.HomeComponent
import com.nmichail.wordly.android.features.home.presentation.NewsDetailComponent
import com.nmichail.wordly.android.features.review.presentation.ReviewComponent

interface MainHostComponent {

	val stack: Value<ChildStack<*, Child>>

	fun handleSelectTab(tab: MainHostTab)

	sealed interface Child {

		data class Home(val component: HomeComponent) : Child

		data object Words : Child

		data object Stats : Child

		data object Profile : Child

		data class Review(val component: ReviewComponent) : Child

		data class NewsDetail(val component: NewsDetailComponent) : Child
	}

	fun interface Factory {

		operator fun invoke(componentContext: ComponentContext): MainHostComponent
	}
}

enum class MainHostTab {
	Home,
	Words,
	Stats,
	Profile,
}
