package com.nmichail.wordly.android.features.profile.presentation.edit

import com.arkivanov.decompose.ComponentContext
import javax.inject.Inject

internal class DefaultProfileEditComponentFactory @Inject constructor(
	private val profileEditStoreFactory: ProfileEditStoreFactory,
) : ProfileEditComponent.Factory {

	override fun invoke(
		componentContext: ComponentContext,
		profileEditRouter: ProfileEditRouter,
	): ProfileEditComponent =
		DefaultProfileEditComponent(
			componentContext = componentContext,
			profileEditStoreFactory = profileEditStoreFactory,
			profileEditRouter = profileEditRouter,
		)
}
