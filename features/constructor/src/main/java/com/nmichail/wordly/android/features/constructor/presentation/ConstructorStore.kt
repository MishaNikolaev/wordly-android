package com.nmichail.wordly.android.features.constructor.presentation

import com.arkivanov.mvikotlin.core.store.Store

internal interface ConstructorStore :
	Store<ConstructorStore.Intent, ConstructorComponent.State, ConstructorComponent.Label> {

	sealed interface Intent {

		data object Back : Intent

		data object Retry : Intent

		data class ChangeSearchQuery(val query: String) : Intent

		data class ChangeLevel(val level: String) : Intent

		data class SelectTheme(val themeId: String) : Intent
	}
}
