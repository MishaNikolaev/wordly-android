package com.nmichail.wordly.android.features.cards.presentation.detail

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.features.cards.domain.entity.CardPracticeWord
import com.nmichail.wordly.android.features.cards.domain.usecase.GetCardSessionUseCase
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
				initialState = CardPracticeStore.State.Loading,
				bootstrapper = SimpleBootstrapper(Action.Load(cardId = cardId)),
				executorFactory = { ExecutorImpl(cardId = cardId) },
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data class Load(val cardId: String) : Action
	}

	private sealed interface Msg {

		data object Loading : Msg

		data class SessionLoaded(val words: List<CardPracticeWord>) : Msg

		data object SetError : Msg

		data class OptionSelected(val optionId: String) : Msg

		data class MoveNext(
			val nextIndex: Int,
			val correctCount: Int,
		) : Msg

		data class Finished(val correctCount: Int) : Msg
	}

	private object ReducerImpl : Reducer<CardPracticeStore.State, Msg> {

		override fun CardPracticeStore.State.reduce(msg: Msg): CardPracticeStore.State =
			when (msg) {
				Msg.Loading -> CardPracticeStore.State.Loading
				is Msg.SessionLoaded -> CardPracticeStore.State.Content(
					words = msg.words,
					currentIndex = 0,
					currentWord = msg.words[0],
					totalCount = msg.words.size,
					progressIndex = 1,
					selectedOptionId = null,
					answerRevealed = false,
					correct = false,
					correctCount = 0,
					finished = false,
				)
				Msg.SetError -> CardPracticeStore.State.Error
				is Msg.OptionSelected -> selectOption(state = this, optionId = msg.optionId)
				is Msg.MoveNext -> moveToNext(state = this, msg = msg)
				is Msg.Finished -> (this as? CardPracticeStore.State.Content)?.copy(
					correctCount = msg.correctCount,
					finished = true,
				) ?: this
			}

		private fun selectOption(
			state: CardPracticeStore.State,
			optionId: String,
		): CardPracticeStore.State {
			if (state !is CardPracticeStore.State.Content || state.finished) return state
			if (state.answerRevealed) return state
			return state.copy(
				selectedOptionId = optionId,
				answerRevealed = true,
				correct = optionId == state.currentWord.correctOptionId,
			)
		}

		private fun moveToNext(
			state: CardPracticeStore.State,
			msg: Msg.MoveNext,
		): CardPracticeStore.State {
			if (state !is CardPracticeStore.State.Content || state.finished) return state
			return state.copy(
				currentIndex = msg.nextIndex,
				currentWord = state.words[msg.nextIndex],
				progressIndex = msg.nextIndex + 1,
				selectedOptionId = null,
				answerRevealed = false,
				correct = false,
				correctCount = msg.correctCount,
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
				is Action.Load -> loadSession(cardId = action.cardId)
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
				CardPracticeStore.Intent.Continue -> handleContinue()
			}
		}

		private fun loadSession(cardId: String) {
			dispatch(Msg.Loading)
			launchTry {
				val words = getCardSessionUseCase(cardId)
				if (words.isEmpty()) {
					dispatch(Msg.SetError)
				} else {
					dispatch(Msg.SessionLoaded(words = words))
				}
			} catch {
				dispatch(Msg.SetError)
			}
		}

		private fun handleContinue() {
			val content = state() as? CardPracticeStore.State.Content ?: return
			if (content.finished || !content.answerRevealed) return

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
		}
	}
}