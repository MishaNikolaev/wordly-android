package com.nmichail.wordly.android.features.materials.presentation

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialFilter
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialItem

interface MaterialsStore :
	Store<MaterialsStore.Intent, MaterialsStore.State, MaterialsStore.Label> {

	sealed interface State {

		data object Loading : State

		data class Error(
			val locallyReadIds: Set<String>,
		) : State

		data class Content(
			val title: String,
			val selectedFilter: MaterialFilter,
			val items: List<MaterialItem>,
			val locallyReadIds: Set<String>,
		) : State
	}

	sealed interface Label {

		data class OpenMaterial(val material: MaterialItem) : Label
	}

	sealed interface Intent {

		data object Retry : Intent

		data class ChangeFilter(val filter: MaterialFilter) : Intent

		data class OpenMaterial(val materialId: String) : Intent
	}
}