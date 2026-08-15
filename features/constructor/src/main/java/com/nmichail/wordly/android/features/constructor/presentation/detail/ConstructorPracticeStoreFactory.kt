package com.nmichail.wordly.android.features.constructor.presentation.detail

import kotlinx.coroutines.launch
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
				ConstructorPracticeStore.State,
				ConstructorPracticeStore.Label,
				> by storeFactory.create(
				name = "ConstructorPracticeStore",
				initialState = ConstructorPracticeStore.State.Initial,
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

		data class Finished(val correctCount: Int) : Msg
	}

	private object ReducerImpl : Reducer<ConstructorPracticeStore.State, Msg> {

		@Suppress("CyclomaticComplexMethod")
		override fun ConstructorPracticeStore.State.reduce(
			msg: Msg,
		): ConstructorPracticeStore.State {
			return when (msg) {
				Msg.Loading -> ConstructorPracticeStore.State.Loading
				is Msg.SessionLoaded -> sessionLoaded(session = msg.session)
				Msg.SetError -> ConstructorPracticeStore.State.Error
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
				is Msg.AnswerReordered -> reorderAnswer(state = this, answer = msg.answer)
				is Msg.AnswerChecked -> checkAnswer(state = this, correct = msg.correct)
				is Msg.MoveNext -> moveToNext(state = this, msg = msg)
				is Msg.Finished -> {
					val content = asContent() ?: return this
					content.copy(
						correctCount = msg.correctCount,
						finished = true,
					)
				}
			}
		}

		private fun sessionLoaded(session: ConstructorSession): ConstructorPracticeStore.State {
			val phrase = session.phrases.first()
			return ConstructorPracticeStore.State.Content(
				session = session,
				currentIndex = 0,
				bank = phrase.words,
				answer = emptyList(),
				checkResult = null,
				correctCount = 0,
				totalCount = session.phrases.size,
				finished = false,
			)
		}

		private fun ConstructorPracticeStore.State.asContent():
			ConstructorPracticeStore.State.Content? =
			this as? ConstructorPracticeStore.State.Content

		private fun reorderAnswer(
			state: ConstructorPracticeStore.State,
			answer: List<ConstructorWord>,
		): ConstructorPracticeStore.State {
			val content = state.asContent() ?: return state
			if (content.finished || content.checkResult != null) return state
			return content.copy(answer = answer)
		}

		private fun checkAnswer(
			state: ConstructorPracticeStore.State,
			correct: Boolean,
		): ConstructorPracticeStore.State {
			val content = state.asContent() ?: return state
			if (content.finished || content.checkResult != null) return state
			return content.copy(checkResult = correct)
		}

		private fun updateAnswer(
			state: ConstructorPracticeStore.State,
			bank: List<ConstructorWord>,
			answer: List<ConstructorWord>,
		): ConstructorPracticeStore.State {
			val content = state.asContent() ?: return state
			if (content.finished || content.checkResult != null) return state
			return content.copy(bank = bank, answer = answer)
		}

		private fun moveToNext(
			state: ConstructorPracticeStore.State,
			msg: Msg.MoveNext,
		): ConstructorPracticeStore.State {
			val content = state.asContent() ?: return state
			if (content.finished) return state
			val nextPhrase = content.session.phrases[msg.nextIndex]
			return content.copy(
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
			ConstructorPracticeStore.State,
		Msg,
			ConstructorPracticeStore.Label,
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
				-> publish(ConstructorPracticeStore.Label.Close)
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
			scope.launch {
				try {
					val session = getConstructorSessionUseCase(themeId)
					if (session.phrases.isEmpty()) {
						dispatch(Msg.SetError)
					} else {
						dispatch(Msg.SessionLoaded(session = session))
					}
				} catch (_: Exception) {
					dispatch(Msg.SetError)
				}
			}
		}

		private fun currentContent(): ConstructorPracticeStore.State.Content? =
			state() as? ConstructorPracticeStore.State.Content

		private fun placeWord(wordId: String) {
			val content = currentContent() ?: return
			if (content.finished || content.checkResult != null) return

			val word = content.bank.firstOrNull { it.id == wordId } ?: return
			dispatch(
				Msg.WordPlaced(
					bank = content.bank.filterNot { it.id == wordId },
					answer = content.answer + word,
				),
			)
		}

		private fun removeWord(wordId: String) {
			val content = currentContent() ?: return
			if (content.finished || content.checkResult != null) return

			val word = content.answer.firstOrNull { it.id == wordId } ?: return
			dispatch(
				Msg.WordRemoved(
					bank = content.bank + word,
					answer = content.answer.filterNot { it.id == wordId },
				),
			)
		}

		private fun moveAnswerWord(fromIndex: Int, toIndex: Int) {
			val content = currentContent() ?: return
			if (content.finished || content.checkResult != null) return
			if (fromIndex !in content.answer.indices || toIndex !in content.answer.indices) return
			if (fromIndex == toIndex) return

			val answer = content.answer.toMutableList()
			val word = answer.removeAt(fromIndex)
			answer.add(toIndex, word)
			dispatch(Msg.AnswerReordered(answer = answer))
		}

		private fun checkAnswer() {
			val content = currentContent() ?: return
			if (content.finished || content.checkResult != null) return

			val phrase = content.session.phrases[content.currentIndex]
			val correct = content.answer.map { it.id } == phrase.correctOrder
			dispatch(Msg.AnswerChecked(correct = correct))
		}

		private fun handleContinue() {
			val content = currentContent() ?: return
			if (content.finished || content.checkResult == null) return

			val nextCorrectCount = if (content.checkResult == true) {
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
