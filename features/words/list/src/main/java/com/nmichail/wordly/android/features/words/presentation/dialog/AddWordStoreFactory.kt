package com.nmichail.wordly.android.features.words.presentation.dialog

import kotlinx.coroutines.launch
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.features.words.domain.entity.NewWord
import com.nmichail.wordly.android.features.words.domain.entity.WordLookup
import com.nmichail.wordly.android.features.words.domain.entity.WordTag
import com.nmichail.wordly.android.features.words.domain.usecase.AddWordUseCase
import com.nmichail.wordly.android.features.words.domain.usecase.LookupWordUseCase
import javax.inject.Inject
import kotlinx.coroutines.Job

internal class AddWordStoreFactory @Inject constructor(
	private val lookupWordUseCase: LookupWordUseCase,
	private val addWordUseCase: AddWordUseCase,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): AddWordStore =
		object :
			AddWordStore,
			Store<AddWordStore.Intent, AddWordStore.State, AddWordStore.Label> by storeFactory.create(
				name = "AddWordStore",
				initialState = AddWordStore.State.Initial,
				bootstrapper = SimpleBootstrapper(Action.Init),
				executorFactory = ::ExecutorImpl,
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data object Init : Action
	}

	private sealed interface Msg {

		data class Opened(val dialog: AddWordDialogState) : Msg

		data object Closed : Msg

		data class DialogUpdated(val dialog: AddWordDialogState) : Msg
	}

	private object ReducerImpl : Reducer<AddWordStore.State, Msg> {

		override fun AddWordStore.State.reduce(msg: Msg): AddWordStore.State =
			when (msg) {
				is Msg.Opened -> AddWordStore.State.Open(dialog = msg.dialog)
				Msg.Closed -> AddWordStore.State.Closed
				is Msg.DialogUpdated -> {
					val open = this as? AddWordStore.State.Open ?: return this
					open.copy(dialog = msg.dialog)
				}
			}
	}

	private inner class ExecutorImpl :
		BaseCoroutineExecutor<
			AddWordStore.Intent,
			Action,
			AddWordStore.State,
			Msg,
			AddWordStore.Label,
			>() {

		private var lookupJob: Job? = null

		override fun executeAction(action: Action) {
			when (action) {
				Action.Init -> dispatch(Msg.Closed)
			}
		}

		override fun executeIntent(intent: AddWordStore.Intent) {
			when (intent) {
				is AddWordStore.Intent.Open -> open(availableTags = intent.availableTags)
				AddWordStore.Intent.Dismiss -> dismiss()
				is AddWordStore.Intent.ChangeWordInput -> changeWordInput(intent.value)
				is AddWordStore.Intent.ToggleTag -> toggleTag(intent.tagId)
				AddWordStore.Intent.Confirm -> confirm()
			}
		}

		private fun open(availableTags: List<WordTag>) {
			lookupJob?.cancel()
			dispatch(
				Msg.Opened(
					dialog = AddWordDialogState(
						wordInput = "",
						phonetic = null,
						translation = null,
						definition = null,
						examples = emptyList(),
						difficulty = 1,
						selectedTagIds = emptySet(),
						availableTags = availableTags,
						lookingUp = false,
						submitting = false,
					),
				),
			)
		}

		private fun dismiss() {
			lookupJob?.cancel()
			dispatch(Msg.Closed)
			publish(AddWordStore.Label.Dismiss)
		}

		private fun currentDialog(): AddWordDialogState? =
			(state() as? AddWordStore.State.Open)?.dialog

		private fun changeWordInput(value: String) {
			val dialog = currentDialog() ?: return
			lookupJob?.cancel()
			if (value.trim().isEmpty()) {
				dispatch(
					Msg.DialogUpdated(
						dialog = dialog.copy(
							wordInput = value,
							phonetic = null,
							translation = null,
							definition = null,
							examples = emptyList(),
							difficulty = 1,
							lookingUp = false,
						),
					),
				)
				return
			}
			dispatch(
				Msg.DialogUpdated(
					dialog = dialog.copy(
						wordInput = value,
						lookingUp = true,
					),
				),
			)
			lookupJob = scope.launch {
				try {
					val lookup = lookupWordUseCase(value)
					applyLookup(lookup = lookup, input = value)
				} catch (_: Exception) {
					val current = currentDialog() ?: return@launch
					dispatch(Msg.DialogUpdated(dialog = current.copy(lookingUp = false)))
				}
			}
		}

		private fun applyLookup(lookup: WordLookup, input: String) {
			val dialog = currentDialog() ?: return
			if (dialog.wordInput != input) return
			dispatch(
				Msg.DialogUpdated(
					dialog = dialog.copy(
						phonetic = lookup.phonetic,
						translation = lookup.translation,
						definition = lookup.definition,
						examples = lookup.examples,
						difficulty = lookup.difficulty,
						lookingUp = false,
					),
				),
			)
		}

		private fun toggleTag(tagId: String) {
			val dialog = currentDialog() ?: return
			val selected = dialog.selectedTagIds.toMutableSet()
			if (!selected.add(tagId)) {
				selected.remove(tagId)
			}
			dispatch(Msg.DialogUpdated(dialog = dialog.copy(selectedTagIds = selected)))
		}

		private fun confirm() {
			val dialog = currentDialog() ?: return
			val word = dialog.wordInput.trim()
			if (word.isEmpty() || dialog.submitting) return

			dispatch(Msg.DialogUpdated(dialog = dialog.copy(submitting = true)))
			scope.launch {
				try {
					addWordUseCase(
						NewWord(
							word = word,
							phonetic = dialog.phonetic,
							translation = dialog.translation,
							definition = dialog.definition,
							examples = dialog.examples,
							tagIds = dialog.selectedTagIds.toList(),
							difficulty = dialog.difficulty,
						),
					)
					lookupJob?.cancel()
					dispatch(Msg.Closed)
					publish(AddWordStore.Label.WordAdded)
				} catch (_: Exception) {
					val current = currentDialog() ?: return@launch
					dispatch(Msg.DialogUpdated(dialog = current.copy(submitting = false)))
				}
			}
		}
	}
}
