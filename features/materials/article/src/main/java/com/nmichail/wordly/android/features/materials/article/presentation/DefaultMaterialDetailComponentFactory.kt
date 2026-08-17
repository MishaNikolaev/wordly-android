package com.nmichail.wordly.android.features.materials.article.presentation

import com.arkivanov.decompose.ComponentContext
import javax.inject.Inject

internal class DefaultMaterialDetailComponentFactory @Inject constructor(
	private val materialDetailStoreFactory: MaterialDetailStoreFactory,
) : MaterialDetailComponent.Factory {

	override fun invoke(
		componentContext: ComponentContext,
		materialId: String,
		materialDetailRouter: MaterialDetailRouter,
	): MaterialDetailComponent =
		DefaultMaterialDetailComponent(
			componentContext = componentContext,
			materialId = materialId,
			materialDetailStoreFactory = materialDetailStoreFactory,
			materialDetailRouter = materialDetailRouter,
		)
}