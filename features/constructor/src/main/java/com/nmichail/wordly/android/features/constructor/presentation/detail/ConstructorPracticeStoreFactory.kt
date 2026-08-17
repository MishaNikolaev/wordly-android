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
				bootstrapper = SimpleBootstrapper(Action.Init(themeId = themeId)),
				executorFactory = { ExecutorImpl(themeId = themeId) },
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data class Init(val themeId: String) : Action
	}

	private sealed interface Msg {

		data object Loading : Msg

		data class SessionLoaded(val session: ConstructorSession) : Msg

		data object SetError : Msg

		data class PlaceWord(val wordId: String) : Msg

		data class RemoveWord(val wordId: String) : Msg

		data class MoveAnswerWord(
			val fromIndex: Int,
			val toIndex: Int,
		) : Msg

		data object Check : Msg

		data object Continue : Msg
	}

	private object ReducerImpl : Reducer<ConstructorPracticeStore.State, Msg> {

		override fun ConstructorPracticeStore.State.reduce(
			msg: Msg,
		): ConstructorPracticeStore.State =
			when (msg) {
				Msg.Loading -> ConstructorPracticeStore.State.Loading
				is Msg.SessionLoaded -> sessionLoaded(session = msg.session)
				Msg.SetError -> ConstructorPracticeStore.State.Error
				is Msg.PlaceWord -> placeWord(wordId = msg.wordId)
				is Msg.RemoveWord -> removeWord(wordId = msg.wordId)
				is Msg.MoveAnswerWord -> moveAnswerWord(
					fromIndex = msg.fromIndex,
					toIndex = msg.toIndex,
				)
				Msg.Check -> checkAnswer()
				Msg.Continue -> continueToNext()
			}

		private fun sessionLoaded(
			session: ConstructorSession,
		): ConstructorPracticeStore.State.Content.InProgress {
			val phrase = session.phrases.first()
			return ConstructorPracticeStore.State.Content.InProgress(
				session = session,
				currentIndex = 0,
				bank = phrase.words,
				answer = emptyList(),
				checkResult = null,
				correctCount = 0,
				totalCount = session.phrases.size,
			)
		}

		private fun ConstructorPracticeStore.State.inProgress():
			ConstructorPracticeStore.State.Content.InProgress? =
			this as? ConstructorPracticeStore.State.Content.InProgress

		private fun ConstructorPracticeStore.State.placeWord(
			wordId: String,
		): ConstructorPracticeStore.State {
			val content = inProgress() ?: return this
			if (content.checkResult != null) return this
			val word = content.bank.firstOrNull { it.id == wordId } ?: return this
			return content.copy(
				bank = content.bank.filterNot { it.id == wordId },
				answer = content.answer + word,
			)
		}

		private fun ConstructorPracticeStore.State.removeWord(
			wordId: String,
		): ConstructorPracticeStore.State {
			val content = inProgress() ?: return this
			if (content.checkResult != null) return this
			val word = content.answer.firstOrNull { it.id == wordId } ?: return this
			return content.copy(
				bank = content.bank + word,
				answer = content.answer.filterNot { it.id == wordId },
			)
		}

		private fun ConstructorPracticeStore.State.moveAnswerWord(
			fromIndex: Int,
			toIndex: Int,
		): ConstructorPracticeStore.State {
			val content = inProgress() ?: return this
			if (content.checkResult != null) return this
			if (fromIndex !in content.answer.indices || toIndex !in content.answer.indices) {
				return this
			}
			if (fromIndex == toIndex) return this
			val answer = content.answer.toMutableList()
			val word = answer.removeAt(fromIndex)
			answer.add(toIndex, word)
			return content.copy(answer = answer)
		}

		private fun ConstructorPracticeStore.State.checkAnswer(): ConstructorPracticeStore.State {
			val content = inProgress() ?: return this
			if (content.checkResult != null) return this
			val phrase = content.session.phrases[content.currentIndex]
			val correct = content.answer.map { it.id } == phrase.correctOrder
			return content.copy(checkResult = correct)
		}

		private fun ConstructorPracticeStore.State.continueToNext():
			ConstructorPracticeStore.State {
			val content = inProgress() ?: return this
			if (content.checkResult == null) return this
			val nextCorrectCount = if (content.checkResult == true) {
				content.correctCount + 1
			} else {
				content.correctCount
			}
			val nextIndex = content.currentIndex + 1
			if (nextIndex >= content.totalCount) {
				return ConstructorPracticeStore.State.Content.Finished(
					correctCount = nextCorrectCount,
					totalCount = content.totalCount,
				)
			}
			val nextPhrase = content.session.phrases[nextIndex]
			return content.copy(
				currentIndex = nextIndex,
				bank = nextPhrase.words,
				answer = emptyList(),
				checkResult = null,
				correctCount = nextCorrectCount,
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
				is Action.Init -> loadSession(themeId = action.themeId)
			}
		}

		override fun executeIntent(intent: ConstructorPracticeStore.Intent) {
			when (intent) {
				ConstructorPracticeStore.Intent.Close,
				ConstructorPracticeStore.Intent.Finish,
				-> publish(ConstructorPracticeStore.Label.Close)
				ConstructorPracticeStore.Intent.Retry -> loadSession(themeId = themeId)
				is ConstructorPracticeStore.Intent.PlaceWord -> {
					dispatch(Msg.PlaceWord(wordId = intent.wordId))
				}
				is ConstructorPracticeStore.Intent.RemoveWord -> {
					dispatch(Msg.RemoveWord(wordId = intent.wordId))
				}
				is ConstructorPracticeStore.Intent.MoveAnswerWord -> {
					dispatch(
						Msg.MoveAnswerWord(
							fromIndex = intent.fromIndex,
							toIndex = intent.toIndex,
						),
					)
				}
				ConstructorPracticeStore.Intent.Check -> dispatch(Msg.Check)
				ConstructorPracticeStore.Intent.Continue -> dispatch(Msg.Continue)
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
	}
}
