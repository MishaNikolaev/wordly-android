package com.nmichail.wordly.android.features.materials.presentation.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value

interface MaterialDetailComponent {

	val model: Value<MaterialDetailStore.State>

	fun handleBack()

	fun handleRetry()

	fun handleShare()

	fun handleLike()

	fun handleDislike()

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			materialId: String,
			materialDetailRouter: MaterialDetailRouter,
		): MaterialDetailComponent
	}
}