package com.nmichail.wordly.android.features.review.presentation

import kotlinx.coroutines.launch
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.features.review.domain.entity.ReviewWord
import com.nmichail.wordly.android.features.review.domain.usecase.GetReviewSessionUseCase
import com.nmichail.wordly.android.features.review.domain.usecase.SubmitReviewAnswerUseCase
import javax.inject.Inject

internal class ReviewStoreFactory @Inject constructor(
	private val getReviewSessionUseCase: GetReviewSessionUseCase,
	private val submitReviewAnswerUseCase: SubmitReviewAnswerUseCase,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): ReviewStore =
		object :
			ReviewStore,
			Store<ReviewStore.Intent, ReviewStore.State, ReviewStore.Label> by storeFactory.create(
				name = "ReviewStore",
				initialState = ReviewStore.State.Loading,
				bootstrapper = SimpleBootstrapper(Action.Load),
				executorFactory = ::ExecutorImpl,
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data object Load : Action
	}

	private sealed interface Msg {

		data object Loading : Msg

		data class SessionLoaded(val words: List<ReviewWord>) : Msg

		data object SetError : Msg

		data class OptionSelected(val optionId: String) : Msg

		data object Submitting : Msg

		data class MoveNext(
			val nextIndex: Int,
			val correctCount: Int,
		) : Msg

		data class Finished(val correctCount: Int) : Msg
	}

	private object ReducerImpl : Reducer<ReviewStore.State, Msg> {

		override fun ReviewStore.State.reduce(msg: Msg): ReviewStore.State =
			when (msg) {
				Msg.Loading -> ReviewStore.State.Loading
				is Msg.SessionLoaded -> ReviewStore.State.Content(
					words = msg.words,
					currentIndex = 0,
					currentWord = msg.words[0],
					totalCount = msg.words.size,
					progressIndex = 1,
					selectedOptionId = null,
					answerRevealed = false,
					correct = false,
					correctCount = 0,
					submitting = false,
					finished = false,
				)
				Msg.SetError -> ReviewStore.State.Error
				is Msg.OptionSelected -> selectOption(state = this, optionId = msg.optionId)
				Msg.Submitting -> setSubmitting(state = this)
				is Msg.MoveNext -> moveToNext(state = this, msg = msg)
				is Msg.Finished -> (this as? ReviewStore.State.Content)?.copy(
					correctCount = msg.correctCount,
					submitting = false,
					finished = true,
				) ?: this
			}

		private fun selectOption(
			state: ReviewStore.State,
			optionId: String,
		): ReviewStore.State {
			if (state !is ReviewStore.State.Content || state.finished) return state
			if (state.answerRevealed || state.submitting) return state
			return state.copy(
				selectedOptionId = optionId,
				answerRevealed = true,
				correct = optionId == state.currentWord.correctOptionId,
			)
		}

		private fun setSubmitting(state: ReviewStore.State): ReviewStore.State {
			if (state !is ReviewStore.State.Content || state.finished) return state
			return state.copy(submitting = true)
		}

		private fun moveToNext(
			state: ReviewStore.State,
			msg: Msg.MoveNext,
		): ReviewStore.State {
			if (state !is ReviewStore.State.Content || state.finished) return state
			return state.copy(
				currentIndex = msg.nextIndex,
				currentWord = state.words[msg.nextIndex],
				progressIndex = msg.nextIndex + 1,
				selectedOptionId = null,
				answerRevealed = false,
				correct = false,
				correctCount = msg.correctCount,
				submitting = false,
			)
		}
	}

	private inner class ExecutorImpl :
		BaseCoroutineExecutor<
			ReviewStore.Intent,
			Action,
			ReviewStore.State,
			Msg,
			ReviewStore.Label,
			>() {

		override fun executeAction(action: Action) {
			when (action) {
				Action.Load -> loadSession()
			}
		}

		override fun executeIntent(intent: ReviewStore.Intent) {
			when (intent) {
				ReviewStore.Intent.Close,
				ReviewStore.Intent.Finish,
				-> publish(ReviewStore.Label.Close)
				ReviewStore.Intent.Retry -> loadSession()
				ReviewStore.Intent.PlayAudio -> Unit
				is ReviewStore.Intent.SelectOption -> {
					dispatch(Msg.OptionSelected(optionId = intent.optionId))
				}
				ReviewStore.Intent.Continue -> submitAndAdvance()
			}
		}

		private fun loadSession() {
			dispatch(Msg.Loading)
			scope.launch {
				try {
					val words = getReviewSessionUseCase()
					if (words.isEmpty()) {
						dispatch(Msg.SetError)
					} else {
						dispatch(Msg.SessionLoaded(words = words))
					}
				} catch (_: Exception) {
					dispatch(Msg.SetError)
				}
			}
		}

		private fun submitAndAdvance() {
			val content = state() as? ReviewStore.State.Content ?: return
			if (content.finished || !content.answerRevealed || content.submitting) return
			val selectedOptionId = content.selectedOptionId ?: return

			dispatch(Msg.Submitting)
			scope.launch {
				try {
					submitReviewAnswerUseCase(
						content.currentWord.id,
						selectedOptionId,
						content.correct,
					)
					val nextCorrectCount = if (content.correct) {
						content.correctCount + 1
					} else {
						content.correctCount
					}
					val nextIndex = content.currentIndex + 1
					if (nextIndex >= content.totalCount) {
						dispatch(Msg.Finished(correctCount = nextCorrectCount))
					} else {
						dispatch(
							Msg.MoveNext(
								nextIndex = nextIndex,
								correctCount = nextCorrectCount,
							),
						)
					}
				} catch (_: Exception) {
					dispatch(Msg.SetError)
				}
			}
		}
	}
}