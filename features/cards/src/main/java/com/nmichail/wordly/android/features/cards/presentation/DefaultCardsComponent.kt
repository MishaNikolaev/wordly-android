package com.nmichail.wordly.android.features.cards.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.component.presentation.launchTry
import com.nmichail.wordly.android.core.navigation.asValue
import com.nmichail.wordly.android.features.cards.domain.entity.CardsItem

internal class DefaultCardsComponent(
	componentContext: ComponentContext,
	cardsStoreFactory: CardsStoreFactory,
	private val cardsRouter: CardsRouter,
	private val onCardClick: (CardsItem) -> Unit,
) : ComponentContext by componentContext,
	CardsComponent {

	private val store: CardsStore = instanceKeeper.getStore {
		cardsStoreFactory.create()
	}

	override val model: Value<CardsComponent.State> = store.asValue()

	init {
		launchTry {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					CardsComponent.Label.Close -> cardsRouter.navigateBack()
					is CardsComponent.Label.OpenCard -> onCardClick(label.item)
				}
			}
		} catch {
			// ignored
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
}