package com.nmichail.wordly.android.features.profile.presentation.edit

import com.arkivanov.mvikotlin.core.store.Store

internal interface ProfileEditStore :
	Store<ProfileEditStore.Intent, ProfileEditComponent.State, ProfileEditComponent.Label> {

	sealed interface Intent {

		data object Retry : Intent

		data object Back : Intent

		data class ChangeFirstName(val value: String) : Intent

		data class ChangeLastName(val value: String) : Intent

		data class ChangeEnglishLevel(val value: String) : Intent

		data object Save : Intent
	}
}
