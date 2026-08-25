package com.nmichail.wordly.android.features.constructor.practice.presentation

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

internal class DefaultConstructorPracticeComponent @AssistedInject constructor(
	private val constructorPracticeStoreFactory: ConstructorPracticeStoreFactory,
	@Assisted("componentContext") componentContext: ComponentContext,
	@Assisted("themeId") themeId: String,
	@Assisted("constructorPracticeRouter") private val constructorPracticeRouter: ConstructorPracticeRouter,
) : ComponentContext by componentContext,
    ConstructorPracticeComponent {

    private val store: ConstructorPracticeStore = instanceKeeper.getStore {
        constructorPracticeStoreFactory.create(themeId = themeId)
    }

    override val model: Value<ConstructorPracticeStore.State> = store.asValue()

    init {
        componentScope().launch {
            for (label in store.labelsChannel(lifecycle)) {
                when (label) {
                    ConstructorPracticeStore.Label.Close -> constructorPracticeRouter.navigateBack()
                }
            }
        }
    }

    override fun handleClose() {
        store.accept(ConstructorPracticeStore.Intent.Close)
    }

    override fun handleRetry() {
        store.accept(ConstructorPracticeStore.Intent.Retry)
    }

    override fun handlePlaceWord(wordId: String) {
        store.accept(ConstructorPracticeStore.Intent.PlaceWord(wordId = wordId))
    }

    override fun handleRemoveWord(wordId: String) {
        store.accept(ConstructorPracticeStore.Intent.RemoveWord(wordId = wordId))
    }

    override fun handleMoveAnswerWord(fromIndex: Int, toIndex: Int) {
        store.accept(
            ConstructorPracticeStore.Intent.MoveAnswerWord(
                fromIndex = fromIndex,
                toIndex = toIndex,
            ),
        )
    }

    override fun handleCheck() {
        store.accept(ConstructorPracticeStore.Intent.Check)
    }

    override fun handleContinue() {
        store.accept(ConstructorPracticeStore.Intent.Continue)
    }

    override fun handleFinish() {
        store.accept(ConstructorPracticeStore.Intent.Finish)
    }

	@AssistedFactory
	fun interface Factory : ConstructorPracticeComponent.Factory {
		override fun invoke(
			@Assisted("componentContext") componentContext: ComponentContext,
			@Assisted("themeId") themeId: String,
			@Assisted("constructorPracticeRouter") constructorPracticeRouter: ConstructorPracticeRouter,
		): DefaultConstructorPracticeComponent
	}
}