package com.nmichail.wordly.android.features.materials.presentation

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialFilter

internal interface MaterialsStore :
	Store<MaterialsStore.Intent, MaterialsComponent.State, MaterialsComponent.Label> {

	sealed interface Intent {

		data object Retry : Intent

		data class ChangeFilter(val filter: MaterialFilter) : Intent

		data class OpenMaterial(val materialId: String) : Intent
	}
}