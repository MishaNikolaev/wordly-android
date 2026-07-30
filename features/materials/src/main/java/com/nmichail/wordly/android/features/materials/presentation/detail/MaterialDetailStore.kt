package com.nmichail.wordly.android.features.materials.presentation.detail

import com.arkivanov.mvikotlin.core.store.Store

internal interface MaterialDetailStore :
	Store<MaterialDetailStore.Intent, MaterialDetailComponent.State, MaterialDetailComponent.Label> {

	sealed interface Intent {

		data object Retry : Intent

		data object Back : Intent

		data object Share : Intent

		data object Like : Intent

		data object Dislike : Intent
	}
}