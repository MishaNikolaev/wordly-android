package com.nmichail.wordly.android.features.review.presentation

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
			Store<ReviewStore.Intent, ReviewComponent.State, ReviewComponent.Label> by storeFactory.create(
				name = "ReviewStore",
				initialState = ReviewComponent.State.Loading,
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

		data class Finished(
			val totalCount: Int,
			val correctCount: Int,
		) : Msg
	}

	private object ReducerImpl : Reducer<ReviewComponent.State, Msg> {

		override fun ReviewComponent.State.reduce(msg: Msg): ReviewComponent.State =
			when (msg) {
				is Msg.Loading -> ReviewComponent.State.Loading
				is Msg.SessionLoaded -> createInProgress(words = msg.words)
				is Msg.SetError -> ReviewComponent.State.Error
				is Msg.OptionSelected -> selectOption(state = this, optionId = msg.optionId)
				is Msg.Submitting -> setSubmitting(state = this)
				is Msg.MoveNext -> moveToNext(state = this, msg = msg)
				is Msg.Finished -> ReviewComponent.State.Finished(
					totalCount = msg.totalCount,
					correctCount = msg.correctCount,
				)
			}

		private fun createInProgress(words: List<ReviewWord>): ReviewComponent.State {
			val currentIndex = 0
			return ReviewComponent.State.InProgress(
				words = words,
				currentIndex = currentIndex,
				currentWord = words[currentIndex],
				totalCount = words.size,
				progressIndex = currentIndex + 1,
				selectedOptionId = null,
				isAnswerRevealed = false,
				isCorrect = false,
				correctCount = 0,
				isSubmitting = false,
			)
		}

		private fun selectOption(
			state: ReviewComponent.State,
			optionId: String,
		): ReviewComponent.State {
			if (state !is ReviewComponent.State.InProgress) return state
			if (state.isAnswerRevealed || state.isSubmitting) return state
			return state.copy(
				selectedOptionId = optionId,
				isAnswerRevealed = true,
				isCorrect = optionId == state.currentWord.correctOptionId,
			)
		}

		private fun setSubmitting(state: ReviewComponent.State): ReviewComponent.State {
			if (state !is ReviewComponent.State.InProgress) return state
			return state.copy(isSubmitting = true)
		}

		private fun moveToNext(
			state: ReviewComponent.State,
			msg: Msg.MoveNext,
		): ReviewComponent.State {
			if (state !is ReviewComponent.State.InProgress) return state
			return state.copy(
				currentIndex = msg.nextIndex,
				currentWord = state.words[msg.nextIndex],
				progressIndex = msg.nextIndex + 1,
				selectedOptionId = null,
				isAnswerRevealed = false,
				isCorrect = false,
				correctCount = msg.correctCount,
				isSubmitting = false,
			)
		}
	}

	private inner class ExecutorImpl :
		BaseCoroutineExecutor<
			ReviewStore.Intent,
			Action,
			ReviewComponent.State,
			Msg,
			ReviewComponent.Label,
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
				-> publish(ReviewComponent.Label.Close)
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
			launchTry {
				val words = getReviewSessionUseCase()
				if (words.isEmpty()) {
					dispatch(Msg.SetError)
				} else {
					dispatch(Msg.SessionLoaded(words = words))
				}
			} catch {
				dispatch(Msg.SetError)
			}
		}

		private fun submitAndAdvance() {
			val progress = state() as? ReviewComponent.State.InProgress ?: return
			if (!progress.isAnswerRevealed || progress.isSubmitting) return
			val selectedOptionId = progress.selectedOptionId ?: return

			dispatch(Msg.Submitting)
			launchTry {
				submitReviewAnswerUseCase(
					progress.currentWord.id,
					selectedOptionId,
					progress.isCorrect,
				)
				val nextCorrectCount = if (progress.isCorrect) {
					progress.correctCount + 1
				} else {
					progress.correctCount
				}
				val nextIndex = progress.currentIndex + 1
				if (nextIndex >= progress.totalCount) {
					dispatch(
						Msg.Finished(
							totalCount = progress.totalCount,
							correctCount = nextCorrectCount,
						),
					)
				} else {
					dispatch(
						Msg.MoveNext(
							nextIndex = nextIndex,
							correctCount = nextCorrectCount,
						),
					)
				}
			} catch {
				dispatch(Msg.SetError)
			}
		}
	}
}