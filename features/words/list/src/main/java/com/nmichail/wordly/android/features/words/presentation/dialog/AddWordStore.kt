package com.nmichail.wordly.android.features.words.presentation.dialog

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.features.words.domain.entity.WordTag

interface AddWordStore :
	Store<AddWordStore.Intent, AddWordStore.State, AddWordStore.Label> {

	sealed interface State {

		data object Initial : State

		data object Closed : State

		data class Open(val dialog: AddWordDialogState) : State
	}

	sealed interface Label {

		data object Dismiss : Label

		data object WordAdded : Label
	}

	sealed interface Intent {

		data class Open(val availableTags: List<WordTag>) : Intent

		data object Dismiss : Intent

		data class ChangeWordInput(val value: String) : Intent

		data class ToggleTag(val tagId: String) : Intent

		data object Confirm : Intent
	}
}
