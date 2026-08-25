package com.nmichail.wordly.android.features.cards.presentation

import com.nmichail.wordly.android.core.navigation.componentScope
import kotlinx.coroutines.launch
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.core.navigation.asValue
import com.nmichail.wordly.android.features.cards.domain.entity.CardsItem
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

internal class DefaultCardsComponent @AssistedInject constructor(
	private val cardsStoreFactory: CardsStoreFactory,
	@Assisted("componentContext") componentContext: ComponentContext,
	@Assisted("cardsRouter") private val cardsRouter: CardsRouter,
	@Assisted("onCardClick") private val onCardClick: (CardsItem) -> Unit,
) : ComponentContext by componentContext,
	CardsComponent {

	private val store: CardsStore = instanceKeeper.getStore {
		cardsStoreFactory.create()
	}

	override val model: Value<CardsStore.State> = store.asValue()

	init {
		componentScope().launch {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					CardsStore.Label.Close -> cardsRouter.navigateBack()
					is CardsStore.Label.OpenCard -> onCardClick(label.item)
				}
			}
		}
	}

	override fun handleBack() {
		store.accept(CardsStore.Intent.Back)
	}

	override fun handleRetry() {
		store.accept(CardsStore.Intent.Retry)
	}

	override fun handleSearchQueryChange(query: String) {
		store.accept(CardsStore.Intent.ChangeSearchQuery(query = query))
	}

	override fun handleLevelChange(level: String) {
		store.accept(CardsStore.Intent.ChangeLevel(level = level))
	}

	override fun handleCardClick(cardId: String) {
		store.accept(CardsStore.Intent.SelectCard(cardId = cardId))
	}

	@AssistedFactory
	fun interface Factory : CardsComponent.Factory {
		override fun invoke(
			@Assisted("componentContext") componentContext: ComponentContext,
			@Assisted("cardsRouter") cardsRouter: CardsRouter,
			@Assisted("onCardClick") onCardClick: (CardsItem) -> Unit,
		): DefaultCardsComponent
	}
}