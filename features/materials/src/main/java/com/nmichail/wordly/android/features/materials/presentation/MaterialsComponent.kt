package com.nmichail.wordly.android.features.materials.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialFilter
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialItem

interface MaterialsComponent {

	val model: Value<State>

	fun handleRetry()

	fun handleFilterChange(filter: MaterialFilter)

	fun handleOpenMaterial(materialId: String)

	sealed interface State {

		data object Loading : State

		data object Error : State

		data class Content(
			val title: String,
			val selectedFilter: MaterialFilter,
			val items: List<MaterialItem>,
		) : State
	}

	sealed interface Label {

		data class OpenMaterial(val material: MaterialItem) : Label
	}

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			onMaterialClick: (MaterialItem) -> Unit,
		): MaterialsComponent
	}
}