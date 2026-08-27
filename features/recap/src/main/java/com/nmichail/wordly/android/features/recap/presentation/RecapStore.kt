package com.nmichail.wordly.android.features.recap.presentation

import com.arkivanov.mvikotlin.core.store.Store

interface RecapStore :
	Store<RecapStore.Intent, RecapStore.State, RecapStore.Label> {

	sealed interface State {

		data object Content : State
	}

	sealed interface Label {

		data object Close : Label
	}

	sealed interface Intent {

		data object Back : Intent
	}
}
