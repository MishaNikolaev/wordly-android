package com.nmichail.wordly.android.features.profile.presentation.edit

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.component.presentation.launchTry
import com.nmichail.wordly.android.core.navigation.asValue

internal class DefaultProfileEditComponent(
	componentContext: ComponentContext,
	profileEditStoreFactory: ProfileEditStoreFactory,
	private val profileEditRouter: ProfileEditRouter,
) : ComponentContext by componentContext,
	ProfileEditComponent {

	private val store: ProfileEditStore = instanceKeeper.getStore {
		profileEditStoreFactory.create()
	}

	override val model = store.asValue()

	init {
		launchTry {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					ProfileEditComponent.Label.Close,
					ProfileEditComponent.Label.Saved,
					-> profileEditRouter.navigateBack()
				}
			}
		} catch {
			// ignored
		}
	}

	override fun handleBack() {
		store.accept(ProfileEditStore.Intent.Back)
	}

	override fun handleRetry() {
		store.accept(ProfileEditStore.Intent.Retry)
	}

	override fun handleChangeFirstName(value: String) {
		store.accept(ProfileEditStore.Intent.ChangeFirstName(value = value))
	}

	override fun handleChangeLastName(value: String) {
		store.accept(ProfileEditStore.Intent.ChangeLastName(value = value))
	}

	override fun handleChangeEnglishLevel(value: String) {
		store.accept(ProfileEditStore.Intent.ChangeEnglishLevel(value = value))
	}

	override fun handleSave() {
		store.accept(ProfileEditStore.Intent.Save)
	}
}
