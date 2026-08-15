package com.nmichail.wordly.android.features.constructor.presentation

import com.arkivanov.mvikotlin.core.store.Store
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorLevelBanner
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorSection
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorTheme

interface ConstructorStore :
	Store<ConstructorStore.Intent, ConstructorStore.State, ConstructorStore.Label> {

	sealed interface State {

		data object Loading : State

		data object Error : State

		data class Content(
			val title: String,
			val searchQuery: String,
			val searchPlaceholder: String,
			val levelBanner: ConstructorLevelBanner?,
			val allSections: List<ConstructorSection>,
			val sections: List<ConstructorSection>,
		) : State
	}

	sealed interface Label {

		data object Close : Label

		data class OpenTheme(val theme: ConstructorTheme) : Label
	}

	sealed interface Intent {

		data object Back : Intent

		data object Retry : Intent

		data class ChangeSearchQuery(val query: String) : Intent

		data class ChangeLevel(val level: String) : Intent

		data class SelectTheme(val themeId: String) : Intent
	}
}
