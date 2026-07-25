package com.nmichail.wordly.android.features.home.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.component.presentation.launchTry
import com.nmichail.wordly.android.core.navigation.asValue
import com.nmichail.wordly.android.features.home.domain.entity.Training
import com.nmichail.wordly.android.features.news.domain.entity.News

internal class DefaultHomeComponent(
	componentContext: ComponentContext,
	homeStoreFactory: HomeStoreFactory,
	private val homeRouter: HomeRouter,
) : ComponentContext by componentContext,
	HomeComponent {

	private val store: HomeStore = instanceKeeper.getStore {
		homeStoreFactory.create()
	}

	override val model: Value<HomeComponent.State> = store.asValue()

	init {
		launchTry {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					HomeComponent.Label.StartReview -> homeRouter.navigateToReview()
					is HomeComponent.Label.OpenTraining -> {
						when (label.training.id) {
							"cards" -> homeRouter.navigateToCards()
							"constructor" -> homeRouter.navigateToConstructor()
						}
					}
					is HomeComponent.Label.OpenNews -> homeRouter.navigateToNews(news = label.news)
				}
			}
		} catch {
			// ignored
		}
	}

	override fun handleRetry() {
		store.accept(HomeStore.Intent.Retry)
	}

	override fun handleOpenMonth() {
		store.accept(HomeStore.Intent.OpenMonth)
	}

	override fun handleDismissMonth() {
		store.accept(HomeStore.Intent.DismissMonth)
	}

	override fun handlePreviousMonth() {
		store.accept(HomeStore.Intent.PreviousMonth)
	}

	override fun handleNextMonth() {
		store.accept(HomeStore.Intent.NextMonth)
	}

	override fun handleGoToCurrentMonth() {
		store.accept(HomeStore.Intent.GoToCurrentMonth)
	}

	override fun handleStartReview() {
		store.accept(HomeStore.Intent.StartReview)
	}

	override fun handleOpenTraining(training: Training) {
		store.accept(HomeStore.Intent.OpenTraining(training = training))
	}

	override fun handleOpenNews(news: News) {
		store.accept(HomeStore.Intent.OpenNews(news = news))
	}
}