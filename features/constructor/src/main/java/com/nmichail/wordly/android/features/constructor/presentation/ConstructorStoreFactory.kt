package com.nmichail.wordly.android.features.constructor.presentation

import kotlinx.coroutines.launch
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.core.network.domain.usecase.UpdateEnglishLevelUseCase
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorCatalog
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorSection
import com.nmichail.wordly.android.features.constructor.domain.usecase.GetConstructorCatalogUseCase
import com.nmichail.wordly.android.shared.catalog.filterCatalogSections
import com.nmichail.wordly.android.shared.catalog.findCatalogItem
import com.nmichail.wordly.android.shared.catalog.matchesCatalogSearch
import com.nmichail.wordly.android.shared.catalog.updateCatalogLevelSectionTitles
import javax.inject.Inject

internal class ConstructorStoreFactory @Inject constructor(
	private val getConstructorCatalogUseCase: GetConstructorCatalogUseCase,
	private val updateEnglishLevelUseCase: UpdateEnglishLevelUseCase,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): ConstructorStore =
		object :
			ConstructorStore,
			Store<ConstructorStore.Intent, ConstructorStore.State, ConstructorStore.Label> by storeFactory.create(
				name = "ConstructorStore",
				initialState = ConstructorStore.State.Initial,
				bootstrapper = SimpleBootstrapper(Action.Load),
				executorFactory = ::ExecutorImpl,
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data object Load : Action
	}

	private sealed interface Msg {

		data object Loading : Msg

		data class CatalogLoaded(val catalog: ConstructorCatalog) : Msg

		data object SetError : Msg

		data class SearchUpdated(
			val query: String,
			val sections: List<ConstructorSection>,
		) : Msg

		data class LevelUpdated(
			val level: String,
			val allSections: List<ConstructorSection>,
			val sections: List<ConstructorSection>,
		) : Msg
	}

	private object ReducerImpl : Reducer<ConstructorStore.State, Msg> {

		override fun ConstructorStore.State.reduce(msg: Msg): ConstructorStore.State =
			when (msg) {
				Msg.Loading -> ConstructorStore.State.Loading
				is Msg.CatalogLoaded -> ConstructorStore.State.Content(
					title = msg.catalog.title,
					searchQuery = "",
					searchPlaceholder = msg.catalog.searchPlaceholder,
					levelBanner = msg.catalog.levelBanner,
					allSections = msg.catalog.sections,
					sections = msg.catalog.sections,
				)
				Msg.SetError -> ConstructorStore.State.Error
				is Msg.SearchUpdated -> {
					val content = this as? ConstructorStore.State.Content ?: return this
					content.copy(
						searchQuery = msg.query,
						sections = msg.sections,
					)
				}
				is Msg.LevelUpdated -> {
					val content = this as? ConstructorStore.State.Content ?: return this
					val banner = content.levelBanner ?: return this
					content.copy(
						levelBanner = banner.copy(levelLabel = msg.level),
						allSections = msg.allSections,
						sections = msg.sections,
					)
				}
			}
	}

	private inner class ExecutorImpl :
		BaseCoroutineExecutor<
			ConstructorStore.Intent,
			Action,
			ConstructorStore.State,
			Msg,
			ConstructorStore.Label,
			>() {

		override fun executeAction(action: Action) {
			when (action) {
				Action.Load -> loadCatalog()
			}
		}

		override fun executeIntent(intent: ConstructorStore.Intent) {
			when (intent) {
				ConstructorStore.Intent.Back -> publish(ConstructorStore.Label.Close)
				ConstructorStore.Intent.Retry -> loadCatalog()
				is ConstructorStore.Intent.ChangeSearchQuery -> {
					val content = state() as? ConstructorStore.State.Content ?: return
					dispatch(
						Msg.SearchUpdated(
							query = intent.query,
							sections = filterCatalogSections(
								sections = content.allSections,
								query = intent.query,
								getItems = { it.items },
								itemMatches = { item, query ->
									matchesCatalogSearch(
										title = item.title,
										subtitle = item.subtitle,
										badge = item.badge,
										query = query,
									)
								},
								copyWithItems = { section, items -> section.copy(items = items) },
							),
						),
					)
				}
				is ConstructorStore.Intent.SelectTheme -> {
					val content = state() as? ConstructorStore.State.Content ?: return
					val theme = findCatalogItem(
						sections = content.allSections,
						getItems = { it.items },
						predicate = { it.id == intent.themeId },
					) ?: return
					publish(ConstructorStore.Label.OpenTheme(theme = theme))
				}
				is ConstructorStore.Intent.ChangeLevel -> changeLevel(level = intent.level)
			}
		}

		private fun changeLevel(level: String) {
			val content = state() as? ConstructorStore.State.Content ?: return
			scope.launch {
				updateEnglishLevelUseCase(level)
				val allSections = updateCatalogLevelSectionTitles(
					sections = content.allSections,
					level = level,
					getTitle = { it.title },
					copyWithTitle = { section, title -> section.copy(title = title) },
				)
				val sections = filterCatalogSections(
					sections = allSections,
					query = content.searchQuery,
					getItems = { it.items },
					itemMatches = { item, query ->
						matchesCatalogSearch(
							title = item.title,
							subtitle = item.subtitle,
							badge = item.badge,
							query = query,
						)
					},
					copyWithItems = { section, items -> section.copy(items = items) },
				)
				dispatch(
					Msg.LevelUpdated(
						level = level,
						allSections = allSections,
						sections = sections,
					),
				)
			}
		}

		private fun loadCatalog() {
			dispatch(Msg.Loading)
			scope.launch {
				try {
					val catalog = getConstructorCatalogUseCase()
					dispatch(Msg.CatalogLoaded(catalog = catalog))
				} catch (_: Exception) {
					dispatch(Msg.SetError)
				}
			}
		}
	}
}
