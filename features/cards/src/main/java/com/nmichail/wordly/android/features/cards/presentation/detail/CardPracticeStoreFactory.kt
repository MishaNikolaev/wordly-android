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
			Store<CardPracticeStore.Intent, CardPracticeComponent.State, CardPracticeComponent.Label>
			by storeFactory.create(
				name = "CardPracticeStore",
				initialState = CardPracticeComponent.State.Loading,
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

		data class Finished(
			val totalCount: Int,
			val correctCount: Int,
		) : Msg
	}

	private object ReducerImpl : Reducer<CardPracticeComponent.State, Msg> {

		override fun CardPracticeComponent.State.reduce(msg: Msg): CardPracticeComponent.State =
			when (msg) {
				Msg.Loading -> CardPracticeComponent.State.Loading
				is Msg.SessionLoaded -> createInProgress(words = msg.words)
				Msg.SetError -> CardPracticeComponent.State.Error
				is Msg.OptionSelected -> selectOption(state = this, optionId = msg.optionId)
				is Msg.MoveNext -> moveToNext(state = this, msg = msg)
				is Msg.Finished -> CardPracticeComponent.State.Finished(
					totalCount = msg.totalCount,
					correctCount = msg.correctCount,
				)
			}

		private fun createInProgress(words: List<CardPracticeWord>): CardPracticeComponent.State {
			val currentIndex = 0
			return CardPracticeComponent.State.InProgress(
				words = words,
				currentIndex = currentIndex,
				currentWord = words[currentIndex],
				totalCount = words.size,
				progressIndex = currentIndex + 1,
				selectedOptionId = null,
				answerRevealed = false,
				correct = false,
				correctCount = 0,
			)
		}

		private fun selectOption(
			state: CardPracticeComponent.State,
			optionId: String,
		): CardPracticeComponent.State {
			if (state !is CardPracticeComponent.State.InProgress) return state
			if (state.answerRevealed) return state
			return state.copy(
				selectedOptionId = optionId,
				answerRevealed = true,
				correct = optionId == state.currentWord.correctOptionId,
			)
		}

		private fun moveToNext(
			state: CardPracticeComponent.State,
			msg: Msg.MoveNext,
		): CardPracticeComponent.State {
			if (state !is CardPracticeComponent.State.InProgress) return state
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
		CardPracticeComponent.State,
		Msg,
		CardPracticeComponent.Label,
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
				-> publish(CardPracticeComponent.Label.Close)
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
			val progress = state() as? CardPracticeComponent.State.InProgress ?: return
			if (!progress.answerRevealed) return

			val nextCorrectCount = if (progress.correct) {
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
		}
	}
}