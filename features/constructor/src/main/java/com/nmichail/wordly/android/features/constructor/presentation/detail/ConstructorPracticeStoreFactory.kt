package com.nmichail.wordly.android.features.constructor.presentation.detail

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorSession
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorWord
import com.nmichail.wordly.android.features.constructor.domain.usecase.GetConstructorSessionUseCase
import javax.inject.Inject

internal class ConstructorPracticeStoreFactory @Inject constructor(
	private val getConstructorSessionUseCase: GetConstructorSessionUseCase,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(themeId: String): ConstructorPracticeStore =
		object :
			ConstructorPracticeStore,
			Store<
				ConstructorPracticeStore.Intent,
				ConstructorPracticeComponent.State,
				ConstructorPracticeComponent.Label,
				> by storeFactory.create(
				name = "ConstructorPracticeStore",
				initialState = ConstructorPracticeComponent.State.Loading,
				bootstrapper = SimpleBootstrapper(Action.Load(themeId = themeId)),
				executorFactory = { ExecutorImpl(themeId = themeId) },
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data class Load(val themeId: String) : Action
	}

	private sealed interface Msg {

		data object Loading : Msg

		data class SessionLoaded(val session: ConstructorSession) : Msg

		data object SetError : Msg

		data class WordPlaced(
			val bank: List<ConstructorWord>,
			val answer: List<ConstructorWord>,
		) : Msg

		data class WordRemoved(
			val bank: List<ConstructorWord>,
			val answer: List<ConstructorWord>,
		) : Msg

		data class AnswerReordered(val answer: List<ConstructorWord>) : Msg

		data class AnswerChecked(val correct: Boolean) : Msg

		data class MoveNext(
			val nextIndex: Int,
			val correctCount: Int,
		) : Msg

		data class Finished(
			val totalCount: Int,
			val correctCount: Int,
		) : Msg
	}

	private object ReducerImpl : Reducer<ConstructorPracticeComponent.State, Msg> {

		override fun ConstructorPracticeComponent.State.reduce(
			msg: Msg,
		): ConstructorPracticeComponent.State {
			return when (msg) {
				Msg.Loading -> ConstructorPracticeComponent.State.Loading
				is Msg.SessionLoaded -> createInProgress(session = msg.session)
				Msg.SetError -> ConstructorPracticeComponent.State.Error
				is Msg.WordPlaced -> updateAnswer(
					state = this,
					bank = msg.bank,
					answer = msg.answer,
				)
				is Msg.WordRemoved -> updateAnswer(
					state = this,
					bank = msg.bank,
					answer = msg.answer,
				)
				is Msg.AnswerReordered -> {
					val inProgress = asInProgress() ?: return this
					if (inProgress.checkResult != null) return this
					inProgress.copy(answer = msg.answer)
				}
				is Msg.AnswerChecked -> {
					val inProgress = asInProgress() ?: return this
					if (inProgress.checkResult != null) return this
					inProgress.copy(checkResult = msg.correct)
				}
				is Msg.MoveNext -> moveToNext(state = this, msg = msg)
				is Msg.Finished -> ConstructorPracticeComponent.State.Finished(
					totalCount = msg.totalCount,
					correctCount = msg.correctCount,
				)
			}
		}

		private fun ConstructorPracticeComponent.State.asInProgress():
			ConstructorPracticeComponent.State.InProgress? =
			this as? ConstructorPracticeComponent.State.InProgress

		private fun createInProgress(session: ConstructorSession): ConstructorPracticeComponent.State {
			val phrase = session.phrases.first()
			return ConstructorPracticeComponent.State.InProgress(
				session = session,
				currentIndex = 0,
				bank = phrase.words,
				answer = emptyList(),
				checkResult = null,
				correctCount = 0,
			)
		}

		private fun updateAnswer(
			state: ConstructorPracticeComponent.State,
			bank: List<ConstructorWord>,
			answer: List<ConstructorWord>,
		): ConstructorPracticeComponent.State {
			val inProgress = state.asInProgress() ?: return state
			if (inProgress.checkResult != null) return state
			return inProgress.copy(bank = bank, answer = answer)
		}

		private fun moveToNext(
			state: ConstructorPracticeComponent.State,
			msg: Msg.MoveNext,
		): ConstructorPracticeComponent.State {
			val inProgress = state.asInProgress() ?: return state
			val nextPhrase = inProgress.session.phrases[msg.nextIndex]
			return inProgress.copy(
				currentIndex = msg.nextIndex,
				bank = nextPhrase.words,
				answer = emptyList(),
				checkResult = null,
				correctCount = msg.correctCount,
			)
		}
	}

	private inner class ExecutorImpl(
		private val themeId: String,
	) : BaseCoroutineExecutor<
		ConstructorPracticeStore.Intent,
		Action,
		ConstructorPracticeComponent.State,
		Msg,
		ConstructorPracticeComponent.Label,
		>() {

		override fun executeAction(action: Action) {
			when (action) {
				is Action.Load -> loadSession(themeId = action.themeId)
			}
		}

		override fun executeIntent(intent: ConstructorPracticeStore.Intent) {
			when (intent) {
				ConstructorPracticeStore.Intent.Close,
				ConstructorPracticeStore.Intent.Finish,
				-> publish(ConstructorPracticeComponent.Label.Close)
				ConstructorPracticeStore.Intent.Retry -> loadSession(themeId = themeId)
				is ConstructorPracticeStore.Intent.PlaceWord -> placeWord(wordId = intent.wordId)
				is ConstructorPracticeStore.Intent.RemoveWord -> removeWord(wordId = intent.wordId)
				is ConstructorPracticeStore.Intent.MoveAnswerWord -> {
					moveAnswerWord(
						fromIndex = intent.fromIndex,
						toIndex = intent.toIndex,
					)
				}
				ConstructorPracticeStore.Intent.Check -> checkAnswer()
				ConstructorPracticeStore.Intent.Continue -> handleContinue()
			}
		}

		private fun loadSession(themeId: String) {
			dispatch(Msg.Loading)
			launchTry {
				val session = getConstructorSessionUseCase(themeId)
				if (session.phrases.isEmpty()) {
					dispatch(Msg.SetError)
				} else {
					dispatch(Msg.SessionLoaded(session = session))
				}
			} catch {
				dispatch(Msg.SetError)
			}
		}

		private fun currentState(): ConstructorPracticeComponent.State.InProgress? =
			state() as? ConstructorPracticeComponent.State.InProgress

		private fun placeWord(wordId: String) {
			val inProgress = currentState() ?: return
			if (inProgress.checkResult != null) return

			val word = inProgress.bank.firstOrNull { it.id == wordId } ?: return
			dispatch(
				Msg.WordPlaced(
					bank = inProgress.bank.filterNot { it.id == wordId },
					answer = inProgress.answer + word,
				),
			)
		}

		private fun removeWord(wordId: String) {
			val inProgress = currentState() ?: return
			if (inProgress.checkResult != null) return

			val word = inProgress.answer.firstOrNull { it.id == wordId } ?: return
			dispatch(
				Msg.WordRemoved(
					bank = inProgress.bank + word,
					answer = inProgress.answer.filterNot { it.id == wordId },
				),
			)
		}

		private fun moveAnswerWord(fromIndex: Int, toIndex: Int) {
			val inProgress = currentState() ?: return
			if (inProgress.checkResult != null) return
			if (fromIndex !in inProgress.answer.indices || toIndex !in inProgress.answer.indices) return
			if (fromIndex == toIndex) return

			val answer = inProgress.answer.toMutableList()
			val word = answer.removeAt(fromIndex)
			answer.add(toIndex, word)
			dispatch(Msg.AnswerReordered(answer = answer))
		}

		private fun checkAnswer() {
			val inProgress = currentState() ?: return
			if (inProgress.checkResult != null) return

			val phrase = inProgress.session.phrases[inProgress.currentIndex]
			val correct = inProgress.answer.map { it.id } == phrase.correctOrder
			dispatch(Msg.AnswerChecked(correct = correct))
		}

		private fun handleContinue() {
			val inProgress = currentState() ?: return
			if (inProgress.checkResult == null) return

			val nextCorrectCount = if (inProgress.checkResult == true) {
				inProgress.correctCount + 1
			} else {
				inProgress.correctCount
			}
			val nextIndex = inProgress.currentIndex + 1
			val totalCount = inProgress.session.phrases.size
			if (nextIndex >= totalCount) {
				dispatch(
					Msg.Finished(
						totalCount = totalCount,
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
