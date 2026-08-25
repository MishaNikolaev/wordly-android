package com.nmichail.wordly.android.features.review.presentation

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

internal class DefaultReviewComponent @AssistedInject constructor(
	private val reviewStoreFactory: ReviewStoreFactory,
	@Assisted("componentContext") componentContext: ComponentContext,
	@Assisted("reviewRouter") private val reviewRouter: ReviewRouter,
) : ComponentContext by componentContext,
	ReviewComponent {

	private val store: ReviewStore = instanceKeeper.getStore {
		reviewStoreFactory.create()
	}

	override val model: Value<ReviewStore.State> = store.asValue()

	init {
		componentScope().launch {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					ReviewStore.Label.Close -> reviewRouter.navigateBack()
				}
			}
		}
	}

	override fun handleClose() {
		store.accept(ReviewStore.Intent.Close)
	}

	override fun handleRetry() {
		store.accept(ReviewStore.Intent.Retry)
	}

	override fun handlePlayAudio() {
		store.accept(ReviewStore.Intent.PlayAudio)
	}

	override fun handleSelectOption(optionId: String) {
		store.accept(ReviewStore.Intent.SelectOption(optionId = optionId))
	}

	override fun handleContinue() {
		store.accept(ReviewStore.Intent.Continue)
	}

	override fun handleFinish() {
		store.accept(ReviewStore.Intent.Finish)
	}

	@AssistedFactory
	fun interface Factory : ReviewComponent.Factory {
		override fun invoke(
			@Assisted("componentContext") componentContext: ComponentContext,
			@Assisted("reviewRouter") reviewRouter: ReviewRouter,
		): DefaultReviewComponent
	}
}