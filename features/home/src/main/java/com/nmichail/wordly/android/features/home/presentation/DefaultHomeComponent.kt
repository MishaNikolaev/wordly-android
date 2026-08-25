package com.nmichail.wordly.android.features.home.presentation

import com.nmichail.wordly.android.core.navigation.componentScope
import kotlinx.coroutines.launch
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.core.navigation.asValue
import com.nmichail.wordly.android.features.home.domain.entity.Training
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
				}
			}
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

	@AssistedFactory
	fun interface Factory : HomeComponent.Factory {
		override fun invoke(
			@Assisted("componentContext") componentContext: ComponentContext,
			@Assisted("homeRouter") homeRouter: HomeRouter,
		): DefaultHomeComponent
	}
}