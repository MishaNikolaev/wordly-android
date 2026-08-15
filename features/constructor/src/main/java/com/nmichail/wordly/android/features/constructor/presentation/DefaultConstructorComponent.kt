package com.nmichail.wordly.android.features.constructor.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.component.presentation.launchTry
import com.nmichail.wordly.android.core.navigation.asValue
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorTheme

internal class DefaultConstructorComponent(
	componentContext: ComponentContext,
	constructorStoreFactory: ConstructorStoreFactory,
	private val constructorRouter: ConstructorRouter,
	private val onThemeClick: (ConstructorTheme) -> Unit,
) : ComponentContext by componentContext,
	ConstructorComponent {

	private val store: ConstructorStore = instanceKeeper.getStore {
		constructorStoreFactory.create()
	}

	override val model: Value<ConstructorStore.State> = store.asValue()

	init {
		launchTry {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					ConstructorStore.Label.Close -> constructorRouter.navigateBack()
					is ConstructorStore.Label.OpenTheme -> onThemeClick(label.theme)
				}
			}
		} catch {
			// ignored
		}
	}

	override fun handleBack() {
		store.accept(ConstructorStore.Intent.Back)
	}

	override fun handleRetry() {
		store.accept(ConstructorStore.Intent.Retry)
	}

	override fun handleSearchQueryChange(query: String) {
		store.accept(ConstructorStore.Intent.ChangeSearchQuery(query = query))
	}

	override fun handleLevelChange(level: String) {
		store.accept(ConstructorStore.Intent.ChangeLevel(level = level))
	}

	override fun handleThemeClick(themeId: String) {
		store.accept(ConstructorStore.Intent.SelectTheme(themeId = themeId))
	}
}
