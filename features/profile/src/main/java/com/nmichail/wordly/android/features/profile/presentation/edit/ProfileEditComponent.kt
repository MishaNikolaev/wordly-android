package com.nmichail.wordly.android.features.profile.presentation.edit

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value

interface ProfileEditComponent {

	val model: Value<ProfileEditStore.State>

	fun handleBack()

	fun handleRetry()

	fun handleChangeFirstName(value: String)

	fun handleChangeLastName(value: String)

	fun handleChangeEnglishLevel(value: String)

	fun handleSave()

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			profileEditRouter: ProfileEditRouter,
		): ProfileEditComponent
	}
}
