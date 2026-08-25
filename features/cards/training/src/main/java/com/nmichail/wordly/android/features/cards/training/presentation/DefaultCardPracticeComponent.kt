package com.nmichail.wordly.android.features.cards.training.presentation

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

internal class DefaultCardPracticeComponent @AssistedInject constructor(
	private val cardPracticeStoreFactory: CardPracticeStoreFactory,
	@Assisted("componentContext") componentContext: ComponentContext,
	@Assisted("cardId") cardId: String,
	@Assisted("cardPracticeRouter") private val cardPracticeRouter: CardPracticeRouter,
) : ComponentContext by componentContext,
	CardPracticeComponent {

	private val store: CardPracticeStore = instanceKeeper.getStore {
		cardPracticeStoreFactory.create(cardId = cardId)
	}

	override val model: Value<CardPracticeStore.State> = store.asValue()

	init {
		componentScope().launch {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					CardPracticeStore.Label.Close -> cardPracticeRouter.navigateBack()
				}
			}
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

	@AssistedFactory
	fun interface Factory : CardPracticeComponent.Factory {
		override fun invoke(
			@Assisted("componentContext") componentContext: ComponentContext,
			@Assisted("cardId") cardId: String,
			@Assisted("cardPracticeRouter") cardPracticeRouter: CardPracticeRouter,
		): DefaultCardPracticeComponent
	}
}