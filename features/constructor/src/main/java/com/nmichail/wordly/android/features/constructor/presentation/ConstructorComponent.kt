package com.nmichail.wordly.android.features.constructor.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorLevelBanner
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorSection
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorTheme

interface ConstructorComponent {

	val model: Value<State>

	fun handleBack()

	fun handleRetry()

	fun handleSearchQueryChange(query: String)

	fun handleLevelChange(level: String)

	fun handleThemeClick(themeId: String)

	sealed interface State {

		data object Loading : State

		data object Error : State

		data class Content(
			val title: String,
			val searchQuery: String,
			val searchPlaceholder: String,
			val levelBanner: ConstructorLevelBanner?,
			val sections: List<ConstructorSection>,
		) : State
	}

	sealed interface Label {

		data object Close : Label

		data class OpenTheme(val theme: ConstructorTheme) : Label
	}

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			constructorRouter: ConstructorRouter,
			onThemeClick: (ConstructorTheme) -> Unit,
		): ConstructorComponent
	}
}