package com.nmichail.wordly.android.features.materials.presentation

import com.arkivanov.decompose.ComponentContext
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialItem
import javax.inject.Inject

internal class DefaultMaterialsComponentFactory @Inject constructor(
	private val materialsStoreFactory: MaterialsStoreFactory,
) : MaterialsComponent.Factory {

	override fun invoke(
		componentContext: ComponentContext,
		onMaterialClick: (MaterialItem) -> Unit,
	): MaterialsComponent =
		DefaultMaterialsComponent(
			componentContext = componentContext,
			materialsStoreFactory = materialsStoreFactory,
			onMaterialClick = onMaterialClick,
		)
}