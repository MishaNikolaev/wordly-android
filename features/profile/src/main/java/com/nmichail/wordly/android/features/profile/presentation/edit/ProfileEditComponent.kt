package com.nmichail.wordly.android.features.profile.presentation.edit

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value

interface ProfileEditComponent {

	val model: Value<State>

	fun handleBack()

	fun handleRetry()

	fun handleChangeFirstName(value: String)

	fun handleChangeLastName(value: String)

	fun handleChangeEnglishLevel(value: String)

	fun handleSave()

	sealed interface State {

		data object Loading : State

		data object Error : State

		data class Content(
			val email: String,
			val firstName: String,
			val lastName: String,
			val englishLevel: String,
			val saving: Boolean,
			val saved: Boolean = false,
		) : State
	}

	sealed interface Label {

		data object Close : Label
	}

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			profileEditRouter: ProfileEditRouter,
		): ProfileEditComponent
	}
}
