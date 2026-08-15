package com.nmichail.wordly.android.features.materials.presentation.detail

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialDetail
import com.nmichail.wordly.android.features.materials.domain.entity.MaterialReaction

interface MaterialDetailStore :
	Store<MaterialDetailStore.Intent, MaterialDetailStore.State, MaterialDetailStore.Label> {

	sealed interface State {

		data object Loading : State

		data class Content(
			val material: MaterialDetail,
			val selectedReaction: MaterialReaction?,
		) : State

		data object Error : State
	}

	sealed interface Label {

		data object Close : Label

		data class Share(val title: String) : Label
	}

	sealed interface Intent {

		data object Retry : Intent

		data object Back : Intent

		data object Share : Intent

		data object Like : Intent

		data object Dislike : Intent
	}
}