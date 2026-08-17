package com.nmichail.wordly.android.features.materials.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialFilter
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialItem

interface MaterialsComponent {

	val model: Value<MaterialsStore.State>

	fun handleRetry()

	fun handleFilterChange(filter: MaterialFilter)

	fun handleOpenMaterial(materialId: String)

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			onMaterialClick: (MaterialItem) -> Unit,
		): MaterialsComponent
	}
}