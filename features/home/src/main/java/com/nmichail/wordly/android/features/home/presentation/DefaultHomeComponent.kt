package com.nmichail.wordly.android.features.home.presentation

import com.nmichail.wordly.android.core.navigation.componentScope
import kotlinx.coroutines.launch
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.core.navigation.asValue
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

internal class DefaultHomeComponent @AssistedInject constructor(
	private val homeStoreFactory: HomeStoreFactory,
	@Assisted("componentContext") componentContext: ComponentContext,
	@Assisted("homeRouter") private val homeRouter: HomeRouter,
) : ComponentContext by componentContext,
	HomeComponent {

	private val store: HomeStore = instanceKeeper.getStore {
		homeStoreFactory.create()
	}

	override val model: Value<HomeStore.State> = store.asValue()

	init {
		componentScope().launch {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					HomeStore.Label.StartReview -> homeRouter.navigateToReview()
					HomeStore.Label.OpenCards -> homeRouter.navigateToCards()
					HomeStore.Label.OpenConstructor -> homeRouter.navigateToConstructor()
					HomeStore.Label.OpenBooks -> homeRouter.navigateToBooks()
					HomeStore.Label.OpenMovies -> homeRouter.navigateToMovies()
					HomeStore.Label.OpenRecap -> homeRouter.navigateToRecap()
				}
			}
		}
	}

	override fun handleRetry() {
		store.accept(HomeStore.Intent.Retry)
	}

	override fun handleRefresh() {
		store.accept(HomeStore.Intent.Refresh)
	}

	override fun handleStartReview() {
		store.accept(HomeStore.Intent.StartReview)
	}

	override fun handleOpenCards() {
		store.accept(HomeStore.Intent.OpenCards)
	}

	override fun handleOpenConstructor() {
		store.accept(HomeStore.Intent.OpenConstructor)
	}

	override fun handleOpenBooks() {
		store.accept(HomeStore.Intent.OpenBooks)
	}

	override fun handleOpenMovies() {
		store.accept(HomeStore.Intent.OpenMovies)
	}

	override fun handleOpenRecap() {
		store.accept(HomeStore.Intent.OpenRecap)
	}

	@AssistedFactory
	fun interface Factory : HomeComponent.Factory {
		override fun invoke(
			@Assisted("componentContext") componentContext: ComponentContext,
			@Assisted("homeRouter") homeRouter: HomeRouter,
		): DefaultHomeComponent
	}
}
