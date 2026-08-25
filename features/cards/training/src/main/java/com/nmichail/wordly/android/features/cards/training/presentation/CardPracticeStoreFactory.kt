package com.nmichail.wordly.android.features.cards.training.presentation

import kotlinx.coroutines.launch
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.features.cards.training.domain.entity.CardPracticeWord
import com.nmichail.wordly.android.features.cards.training.domain.usecase.GetCardSessionUseCase
import javax.inject.Inject

internal class CardPracticeStoreFactory @Inject constructor(
	private val getCardSessionUseCase: GetCardSessionUseCase,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(cardId: String): CardPracticeStore =
		object :
			CardPracticeStore,
			Store<CardPracticeStore.Intent, CardPracticeStore.State, CardPracticeStore.Label>
			by storeFactory.create(
				name = "CardPracticeStore",
				initialState = CardPracticeStore.State.Initial,
				bootstrapper = SimpleBootstrapper(Action.Init(cardId = cardId)),
				executorFactory = { ExecutorImpl(cardId = cardId) },
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data class Init(val cardId: String) : Action
	}

	private sealed interface Msg {

		data object Loading : Msg

		data class SessionLoaded(val words: List<CardPracticeWord>) : Msg

		data object SetError : Msg

		data class OptionSelected(val optionId: String) : Msg

		data object Continue : Msg
	}

	private object ReducerImpl : Reducer<CardPracticeStore.State, Msg> {

		override fun CardPracticeStore.State.reduce(msg: Msg): CardPracticeStore.State =
			when (msg) {
				Msg.Loading -> CardPracticeStore.State.Loading
				is Msg.SessionLoaded -> sessionLoaded(words = msg.words)
				Msg.SetError -> CardPracticeStore.State.Error
				is Msg.OptionSelected -> selectOption(optionId = msg.optionId)
				Msg.Continue -> continueToNext()
			}

		private fun sessionLoaded(
			words: List<CardPracticeWord>,
		): CardPracticeStore.State.Content.InProgress =
			CardPracticeStore.State.Content.InProgress(
				words = words,
				currentIndex = 0,
				currentWord = words[0],
				totalCount = words.size,
				progressIndex = 1,
				selectedOptionId = null,
				answerRevealed = false,
				correct = false,
				correctCount = 0,
			)

		private fun CardPracticeStore.State.inProgress():
			CardPracticeStore.State.Content.InProgress? =
			this as? CardPracticeStore.State.Content.InProgress

		private fun CardPracticeStore.State.selectOption(
			optionId: String,
		): CardPracticeStore.State {
			val content = inProgress() ?: return this
			if (content.answerRevealed) return this
			return content.copy(
				selectedOptionId = optionId,
				answerRevealed = true,
				correct = optionId == content.currentWord.correctOptionId,
			)
		}

		private fun CardPracticeStore.State.continueToNext(): CardPracticeStore.State {
			val content = inProgress() ?: return this
			if (!content.answerRevealed) return this
			val nextCorrectCount = if (content.correct) {
				content.correctCount + 1
			} else {
				content.correctCount
			}
			val nextIndex = content.currentIndex + 1
			if (nextIndex >= content.totalCount) {
				return CardPracticeStore.State.Content.Finished(
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
			)
		}
	}

	private inner class ExecutorImpl(
		private val cardId: String,
	) : BaseCoroutineExecutor<
		CardPracticeStore.Intent,
		Action,
			CardPracticeStore.State,
		Msg,
			CardPracticeStore.Label,
		>() {

		override fun executeAction(action: Action) {
			when (action) {
				is Action.Init -> loadSession(cardId = action.cardId)
			}
		}

		override fun executeIntent(intent: CardPracticeStore.Intent) {
			when (intent) {
				CardPracticeStore.Intent.Close,
				CardPracticeStore.Intent.Finish,
				-> publish(CardPracticeStore.Label.Close)
				CardPracticeStore.Intent.Retry -> loadSession(cardId = cardId)
				CardPracticeStore.Intent.PlayAudio -> Unit
				is CardPracticeStore.Intent.SelectOption -> {
					dispatch(Msg.OptionSelected(optionId = intent.optionId))
				}
				CardPracticeStore.Intent.Continue -> dispatch(Msg.Continue)
			}
		}

		private fun loadSession(cardId: String) {
			dispatch(Msg.Loading)
			scope.launch {
				try {
					val words = getCardSessionUseCase(cardId).map { word ->
						word.withShuffledOptions(seed = "$cardId:${word.id}")
					}
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
	}
}
