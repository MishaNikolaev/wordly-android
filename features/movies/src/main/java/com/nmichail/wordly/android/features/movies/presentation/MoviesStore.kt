package com.nmichail.wordly.android.features.movies.presentation

import com.arkivanov.mvikotlin.core.store.Store

interface MoviesStore :
	Store<MoviesStore.Intent, MoviesStore.State, MoviesStore.Label> {

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
