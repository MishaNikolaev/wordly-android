package com.nmichail.wordly.android.features.materials.presentation.detail

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialDetail
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialReaction

interface MaterialDetailComponent {

	val model: Value<State>

	fun handleBack()

	fun handleRetry()

	fun handleShare()

	fun handleLike()

	fun handleDislike()

	sealed interface State {

		data object Loading : State

		data object Error : State

		data class Content(
			val material: MaterialDetail,
			val selectedReaction: MaterialReaction?,
		) : State
	}

	sealed interface Label {

		data object Close : Label

		data class Share(val title: String) : Label
	}

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			materialId: String,
			materialDetailRouter: MaterialDetailRouter,
		): MaterialDetailComponent
	}
}