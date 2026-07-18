package com.nmichail.wordly.android.features.review.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.features.review.domain.entity.ReviewOption
import com.nmichail.wordly.android.features.review.domain.entity.ReviewQuestion
import javax.inject.Inject

internal class ReviewStoreFactory @Inject constructor() {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): ReviewStore =
		object :
			ReviewStore,
			Store<ReviewStore.Intent, ReviewComponent.State, ReviewComponent.Label> by storeFactory.create(
				name = "ReviewStore",
				initialState = ReviewComponent.State(
					question = InitialQuestion,
					selectedOptionId = null,
				),
				executorFactory = ::ExecutorImpl,
				reducer = ReducerImpl,
			) {}

	private sealed interface Msg {

		data class OptionSelected(val optionId: String) : Msg
	}

	private object ReducerImpl : Reducer<ReviewComponent.State, Msg> {

		override fun ReviewComponent.State.reduce(msg: Msg): ReviewComponent.State =
			when (msg) {
				is Msg.OptionSelected -> copy(selectedOptionId = msg.optionId)
			}
	}

	private class ExecutorImpl :
		BaseCoroutineExecutor<
			ReviewStore.Intent,
			Nothing,
			ReviewComponent.State,
			Msg,
			ReviewComponent.Label,
			>() {

		override fun executeIntent(intent: ReviewStore.Intent) {
			when (intent) {
				ReviewStore.Intent.Close -> publish(ReviewComponent.Label.Close)
				ReviewStore.Intent.PlayAudio -> Unit
				is ReviewStore.Intent.SelectOption -> {
					dispatch(Msg.OptionSelected(optionId = intent.optionId))
				}
			}
		}
	}
}

private val InitialQuestion = ReviewQuestion(
	word = "recall",
	phonetic = "/rɪˈkɔːl/",
	taskLabel = "Выбери перевод",
	options = listOf(
		ReviewOption(id = "1", text = "стойкость, упругость"),
		ReviewOption(id = "2", text = "вспоминать; отзыв"),
		ReviewOption(id = "3", text = "использовать; рычаг"),
		ReviewOption(id = "4", text = "обыденный, скучный"),
	),
	currentIndex = 1,
	totalCount = 4,
)
