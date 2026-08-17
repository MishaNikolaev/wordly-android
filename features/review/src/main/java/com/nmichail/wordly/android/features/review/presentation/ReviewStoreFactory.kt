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
				initialState = ReviewStore.State.Initial,
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

		data object AnswerSubmitted : Msg
	}

	private object ReducerImpl : Reducer<ReviewStore.State, Msg> {

		override fun ReviewStore.State.reduce(msg: Msg): ReviewStore.State =
			when (msg) {
				Msg.Loading -> ReviewStore.State.Loading
				is Msg.SessionLoaded -> sessionLoaded(words = msg.words)
				Msg.SetError -> ReviewStore.State.Error
				is Msg.OptionSelected -> selectOption(optionId = msg.optionId)
				Msg.Submitting -> setSubmitting()
				Msg.AnswerSubmitted -> advanceAfterSubmit()
			}

		private fun sessionLoaded(words: List<ReviewWord>): ReviewStore.State.Content.InProgress =
			ReviewStore.State.Content.InProgress(
				words = words,
				currentIndex = 0,
				currentWord = words[0],
				totalCount = words.size,
				progressIndex = 1,
				selectedOptionId = null,
				answerRevealed = false,
				correct = false,
				correctCount = 0,
				submitting = false,
			)

		private fun ReviewStore.State.inProgress(): ReviewStore.State.Content.InProgress? =
			this as? ReviewStore.State.Content.InProgress

		private fun ReviewStore.State.selectOption(optionId: String): ReviewStore.State {
			val content = inProgress() ?: return this
			if (content.answerRevealed || content.submitting) return this
			return content.copy(
				selectedOptionId = optionId,
				answerRevealed = true,
				correct = optionId == content.currentWord.correctOptionId,
			)
		}

		private fun ReviewStore.State.setSubmitting(): ReviewStore.State {
			val content = inProgress() ?: return this
			return content.copy(submitting = true)
		}

		private fun ReviewStore.State.advanceAfterSubmit(): ReviewStore.State {
			val content = inProgress() ?: return this
			val nextCorrectCount = if (content.correct) {
				content.correctCount + 1
			} else {
				content.correctCount
			}
			val nextIndex = content.currentIndex + 1
			if (nextIndex >= content.totalCount) {
				return ReviewStore.State.Content.Finished(
					correctCount = nextCorrectCount,
					totalCount = content.totalCount,
				)
			}
			return content.copy(
				currentIndex = nextIndex,
				currentWord = content.words[nextIndex],
				progressIndex = nextIndex + 1,
				selectedOptionId = null,
				answerRevealed = false,
				correct = false,
				correctCount = nextCorrectCount,
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
			val content = state() as? ReviewStore.State.Content.InProgress ?: return
			if (!content.answerRevealed || content.submitting) return
			val selectedOptionId = content.selectedOptionId ?: return

			dispatch(Msg.Submitting)
			scope.launch {
				try {
					submitReviewAnswerUseCase(
						content.currentWord.id,
						selectedOptionId,
						content.correct,
					)
					dispatch(Msg.AnswerSubmitted)
				} catch (_: Exception) {
					dispatch(Msg.SetError)
				}
			}
		}
	}
}
