package com.nmichail.wordly.android.features.review.presentation

import com.arkivanov.mvikotlin.core.store.Store

internal interface ReviewStore :
	Store<ReviewStore.Intent, ReviewComponent.State, ReviewComponent.Label> {

	sealed interface Intent {

		data object Close : Intent

		data object Retry : Intent

		data object PlayAudio : Intent

		data class SelectOption(val optionId: String) : Intent

		data object Continue : Intent

		data object Finish : Intent
	}
}