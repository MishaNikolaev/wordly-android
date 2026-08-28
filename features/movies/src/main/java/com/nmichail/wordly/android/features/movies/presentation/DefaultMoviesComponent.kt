package com.nmichail.wordly.android.features.movies.presentation

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

internal class DefaultMoviesComponent @AssistedInject constructor(
	private val moviesStoreFactory: MoviesStoreFactory,
	@Assisted("componentContext") componentContext: ComponentContext,
	@Assisted("moviesRouter") private val moviesRouter: MoviesRouter,
) : ComponentContext by componentContext,
	MoviesComponent {

	private val store: MoviesStore = instanceKeeper.getStore {
		moviesStoreFactory.create()
	}

	override val model: Value<MoviesStore.State> = store.asValue()

	init {
		componentScope().launch {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					MoviesStore.Label.Close -> moviesRouter.navigateBack()
				}
			}
		}
	}

	override fun handleBack() {
		store.accept(MoviesStore.Intent.Back)
	}

	@AssistedFactory
	fun interface Factory : MoviesComponent.Factory {
		override fun invoke(
			@Assisted("componentContext") componentContext: ComponentContext,
			@Assisted("moviesRouter") moviesRouter: MoviesRouter,
		): DefaultMoviesComponent
	}
}
