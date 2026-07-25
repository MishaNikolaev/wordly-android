package com.nmichail.wordly.android.mainhost.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.cards.presentation.CardsComponent
import com.nmichail.wordly.android.features.cards.presentation.CardsRouter
import com.nmichail.wordly.android.features.cards.presentation.detail.CardPracticeComponent
import com.nmichail.wordly.android.features.cards.presentation.detail.CardPracticeRouter
import com.nmichail.wordly.android.features.home.presentation.HomeComponent
import com.nmichail.wordly.android.features.home.presentation.HomeRouter
import com.nmichail.wordly.android.features.news.domain.entity.News
import com.nmichail.wordly.android.features.news.presentation.NewsDetailComponent
import com.nmichail.wordly.android.features.news.presentation.NewsDetailRouter
import com.nmichail.wordly.android.features.review.presentation.ReviewComponent
import com.nmichail.wordly.android.features.review.presentation.ReviewRouter
import kotlinx.serialization.Serializable

internal class DefaultMainHostComponent(
	componentContext: ComponentContext,
	private val homeComponentFactory: HomeComponent.Factory,
	private val reviewComponentFactory: ReviewComponent.Factory,
	private val cardsComponentFactory: CardsComponent.Factory,
	private val cardPracticeComponentFactory: CardPracticeComponent.Factory,
	private val newsDetailComponentFactory: NewsDetailComponent.Factory,
) : MainHostComponent, ComponentContext by componentContext {

	private val navigation = StackNavigation<MainHostConfig>()

	override val stack: Value<ChildStack<*, MainHostComponent.Child>> = childStack(
		source = navigation,
		serializer = MainHostConfig.serializer(),
		initialStack = { listOf(MainHostConfig.Home) },
		handleBackButton = true,
		childFactory = ::child,
	)

	override fun handleSelectTab(tab: MainHostTab) {
		navigation.bringToFront(tab.toConfig())
	}

	@OptIn(DelicateDecomposeApi::class)
	private fun child(
		config: MainHostConfig,
		componentContext: ComponentContext,
	): MainHostComponent.Child =
		when (config) {
			MainHostConfig.Home -> homeChild(componentContext)
			MainHostConfig.Words -> MainHostComponent.Child.Words
			MainHostConfig.Stats -> MainHostComponent.Child.Stats
			MainHostConfig.Profile -> MainHostComponent.Child.Profile
			MainHostConfig.Review -> reviewChild(componentContext)
			MainHostConfig.Cards -> cardsChild(componentContext)
			is MainHostConfig.CardPractice -> cardPracticeChild(config.cardId, componentContext)
			is MainHostConfig.NewsDetail -> newsDetailChild(config.newsId, componentContext)
		}

	@OptIn(DelicateDecomposeApi::class)
	private fun homeChild(componentContext: ComponentContext): MainHostComponent.Child {
		val homeRouter = object : HomeRouter {
			override fun navigateToReview() {
				navigation.push(MainHostConfig.Review)
			}

			override fun navigateToCards() {
				navigation.push(MainHostConfig.Cards)
			}

			override fun navigateToNews(news: News) {
				navigation.push(MainHostConfig.NewsDetail(newsId = news.id))
			}
		}
		return MainHostComponent.Child.Home(
			component = homeComponentFactory(
				componentContext = componentContext,
				homeRouter = homeRouter,
			),
		)
	}

	private fun reviewChild(componentContext: ComponentContext): MainHostComponent.Child {
		val reviewRouter = object : ReviewRouter {
			override fun navigateBack() {
				navigation.pop()
			}
		}
		return MainHostComponent.Child.Review(
			component = reviewComponentFactory(
				componentContext = componentContext,
				reviewRouter = reviewRouter,
			),
		)
	}

	@OptIn(DelicateDecomposeApi::class)
	private fun cardsChild(componentContext: ComponentContext): MainHostComponent.Child {
		val cardsRouter = object : CardsRouter {
			override fun navigateBack() {
				navigation.pop()
			}
		}
		return MainHostComponent.Child.Cards(
			component = cardsComponentFactory(
				componentContext = componentContext,
				cardsRouter = cardsRouter,
				onCardClick = { item ->
					navigation.push(MainHostConfig.CardPractice(cardId = item.id))
				},
			),
		)
	}

	private fun cardPracticeChild(
		cardId: String,
		componentContext: ComponentContext,
	): MainHostComponent.Child {
		val cardPracticeRouter = object : CardPracticeRouter {
			override fun navigateBack() {
				navigation.pop()
			}
		}
		return MainHostComponent.Child.CardPractice(
			component = cardPracticeComponentFactory(
				componentContext = componentContext,
				cardId = cardId,
				cardPracticeRouter = cardPracticeRouter,
			),
		)
	}

	private fun newsDetailChild(
		newsId: String,
		componentContext: ComponentContext,
	): MainHostComponent.Child {
		val newsDetailRouter = object : NewsDetailRouter {
			override fun navigateBack() {
				navigation.pop()
			}
		}
		return MainHostComponent.Child.NewsDetail(
			component = newsDetailComponentFactory(
				componentContext = componentContext,
				newsId = newsId,
				newsDetailRouter = newsDetailRouter,
			),
		)
	}
}

@Serializable
private sealed interface MainHostConfig {

	@Serializable
	data object Home : MainHostConfig

	@Serializable
	data object Words : MainHostConfig

	@Serializable
	data object Stats : MainHostConfig

	@Serializable
	data object Profile : MainHostConfig

	@Serializable
	data object Review : MainHostConfig

	@Serializable
	data object Cards : MainHostConfig

	@Serializable
	data class CardPractice(
		val cardId: String,
	) : MainHostConfig

	@Serializable
	data class NewsDetail(
		val newsId: String,
	) : MainHostConfig
}

private fun MainHostTab.toConfig(): MainHostConfig =
	when (this) {
		MainHostTab.Home -> MainHostConfig.Home
		MainHostTab.Words -> MainHostConfig.Words
		MainHostTab.Stats -> MainHostConfig.Stats
		MainHostTab.Profile -> MainHostConfig.Profile
	}

fun MainHostComponent.Child.toTab(): MainHostTab? =
	when (this) {
		is MainHostComponent.Child.Home -> MainHostTab.Home
		MainHostComponent.Child.Words -> MainHostTab.Words
		MainHostComponent.Child.Stats -> MainHostTab.Stats
		MainHostComponent.Child.Profile -> MainHostTab.Profile
		is MainHostComponent.Child.Review -> null
		is MainHostComponent.Child.Cards -> null
		is MainHostComponent.Child.CardPractice -> null
		is MainHostComponent.Child.NewsDetail -> null
	}
