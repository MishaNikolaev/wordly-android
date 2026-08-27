package com.nmichail.wordly.android.features.recap.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.core.navigation.asValue
import com.nmichail.wordly.android.core.navigation.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

internal class DefaultRecapComponent @AssistedInject constructor(
	private val recapStoreFactory: RecapStoreFactory,
	@Assisted("componentContext") componentContext: ComponentContext,
	@Assisted("recapRouter") private val recapRouter: RecapRouter,
) : ComponentContext by componentContext,
	RecapComponent {

	private val store: RecapStore = instanceKeeper.getStore {
		recapStoreFactory.create()
	}

	override val model: Value<RecapStore.State> = store.asValue()

	init {
		componentScope().launch {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					RecapStore.Label.Close -> recapRouter.navigateBack()
				}
			}
		}
	}

	override fun handleBack() {
		store.accept(RecapStore.Intent.Back)
	}

	@AssistedFactory
	fun interface Factory : RecapComponent.Factory {
		override fun invoke(
			@Assisted("componentContext") componentContext: ComponentContext,
			@Assisted("recapRouter") recapRouter: RecapRouter,
		): DefaultRecapComponent
	}
}
