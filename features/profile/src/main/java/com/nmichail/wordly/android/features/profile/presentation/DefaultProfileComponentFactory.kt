package com.nmichail.wordly.android.features.profile.presentation

import com.arkivanov.decompose.ComponentContext
import javax.inject.Inject

internal class DefaultProfileComponentFactory @Inject constructor(
	private val profileStoreFactory: ProfileStoreFactory,
) : ProfileComponent.Factory {

	override fun invoke(
		componentContext: ComponentContext,
		onOpenEdit: () -> Unit,
	): ProfileComponent =
		DefaultProfileComponent(
			componentContext = componentContext,
			profileStoreFactory = profileStoreFactory,
			onOpenEdit = onOpenEdit,
		)
}
