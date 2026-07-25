package com.nmichail.wordly.android.features.cards.presentation.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.component.presentation.launchTry
import com.nmichail.wordly.android.core.navigation.asValue

internal class DefaultCardPracticeComponent(
	componentContext: ComponentContext,
	cardId: String,
	cardPracticeStoreFactory: CardPracticeStoreFactory,
	private val cardPracticeRouter: CardPracticeRouter,
) : ComponentContext by componentContext,
	CardPracticeComponent {

	private val store: CardPracticeStore = instanceKeeper.getStore {
		cardPracticeStoreFactory.create(cardId = cardId)
	}

	override val model: Value<CardPracticeComponent.State> = store.asValue()

	init {
		launchTry {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					CardPracticeComponent.Label.Close -> cardPracticeRouter.navigateBack()
				}
			}
		} catch {
			// ignored
		}
	}

	override fun handleClose() {
		store.accept(CardPracticeStore.Intent.Close)
	}

	override fun handleRetry() {
		store.accept(CardPracticeStore.Intent.Retry)
	}

	override fun handlePlayAudio() {
		store.accept(CardPracticeStore.Intent.PlayAudio)
	}

	override fun handleSelectOption(optionId: String) {
		store.accept(CardPracticeStore.Intent.SelectOption(optionId = optionId))
	}

	override fun handleContinue() {
		store.accept(CardPracticeStore.Intent.Continue)
	}

	override fun handleFinish() {
		store.accept(CardPracticeStore.Intent.Finish)
	}
}