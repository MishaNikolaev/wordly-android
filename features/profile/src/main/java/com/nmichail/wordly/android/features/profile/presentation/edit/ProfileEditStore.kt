package com.nmichail.wordly.android.features.profile.presentation.edit

import com.arkivanov.mvikotlin.core.store.Store

interface ProfileEditStore :
	Store<ProfileEditStore.Intent, ProfileEditStore.State, ProfileEditStore.Label> {

	sealed interface State {

		data object Loading : State

		data class Content(
			val email: String,
			val firstName: String,
			val lastName: String,
			val englishLevel: String,
			val saving: Boolean,
			val saved: Boolean,
		) : State

		data object Error : State
	}

	sealed interface Label {

		data object Close : Label
	}

	sealed interface Intent {

		data object Retry : Intent

		data object Back : Intent

		data class ChangeFirstName(val value: String) : Intent

		data class ChangeLastName(val value: String) : Intent

		data class ChangeEnglishLevel(val value: String) : Intent

		data object Save : Intent
	}
}
