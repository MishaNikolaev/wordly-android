package com.nmichail.wordly.android.features.constructor.presentation

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
import javax.inject.Inject

internal class ConstructorStoreFactory @Inject constructor(
	private val getConstructorCatalogUseCase: GetConstructorCatalogUseCase,
	private val updateEnglishLevelUseCase: UpdateEnglishLevelUseCase,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): ConstructorStore =
		object :
			ConstructorStore,
			Store<ConstructorStore.Intent, ConstructorComponent.State, ConstructorComponent.Label> by storeFactory.create(
				name = "ConstructorStore",
				initialState = ConstructorComponent.State.Loading,
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

		data class LevelUpdated(val level: String) : Msg
	}

	private object ReducerImpl : Reducer<ConstructorComponent.State, Msg> {

		override fun ConstructorComponent.State.reduce(msg: Msg): ConstructorComponent.State =
			when (msg) {
				Msg.Loading -> ConstructorComponent.State.Loading
				is Msg.CatalogLoaded -> ConstructorComponent.State.Content(
					title = msg.catalog.title,
					searchQuery = "",
					searchPlaceholder = msg.catalog.searchPlaceholder,
					levelBanner = msg.catalog.levelBanner,
					sections = msg.catalog.sections,
				)
				Msg.SetError -> ConstructorComponent.State.Error
				is Msg.SearchUpdated -> {
					val content = this as? ConstructorComponent.State.Content ?: return this
					content.copy(
						searchQuery = msg.query,
						sections = msg.sections,
					)
				}
				is Msg.LevelUpdated -> {
					val content = this as? ConstructorComponent.State.Content ?: return this
					val banner = content.levelBanner ?: return this
					content.copy(
						levelBanner = banner.copy(levelLabel = msg.level),
						sections = updateLevelSectionTitles(
							sections = content.sections,
							level = msg.level,
						),
					)
				}
			}
	}

	private inner class ExecutorImpl :
		BaseCoroutineExecutor<
			ConstructorStore.Intent,
			Action,
			ConstructorComponent.State,
			Msg,
			ConstructorComponent.Label,
			>() {

		private var allSections: List<ConstructorSection> = emptyList()

		override fun executeAction(action: Action) {
			when (action) {
				Action.Load -> loadCatalog()
			}
		}

		override fun executeIntent(intent: ConstructorStore.Intent) {
			when (intent) {
				ConstructorStore.Intent.Back -> publish(ConstructorComponent.Label.Close)
				ConstructorStore.Intent.Retry -> loadCatalog()
				is ConstructorStore.Intent.ChangeSearchQuery -> {
					dispatch(
						Msg.SearchUpdated(
							query = intent.query,
							sections = filterSections(allSections, intent.query),
						),
					)
				}
				is ConstructorStore.Intent.SelectTheme -> {
					val theme = allSections
						.asSequence()
						.flatMap { it.items.asSequence() }
						.firstOrNull { it.id == intent.themeId }
						?: return
					publish(ConstructorComponent.Label.OpenTheme(theme = theme))
				}
				is ConstructorStore.Intent.ChangeLevel -> changeLevel(level = intent.level)
			}
		}

		private fun changeLevel(level: String) {
			launchTry {
				updateEnglishLevelUseCase(level)
				allSections = updateLevelSectionTitles(sections = allSections, level = level)
				dispatch(Msg.LevelUpdated(level = level))
			} catch {
				// keep previous level
			}
		}

		private fun loadCatalog() {
			dispatch(Msg.Loading)
			launchTry {
				val catalog = getConstructorCatalogUseCase()
				allSections = catalog.sections
				dispatch(Msg.CatalogLoaded(catalog = catalog))
			} catch {
				dispatch(Msg.SetError)
			}
		}

		private fun filterSections(
			sections: List<ConstructorSection>,
			query: String,
		): List<ConstructorSection> {
			val normalized = query.trim()
			if (normalized.isEmpty()) return sections

			return sections.mapNotNull { section ->
				val items = section.items.filter { item ->
					item.title.contains(normalized, ignoreCase = true) ||
						item.subtitle.contains(normalized, ignoreCase = true) ||
						item.badge.orEmpty().contains(normalized, ignoreCase = true)
				}
				if (items.isEmpty()) {
					null
				} else {
					section.copy(items = items)
				}
			}
		}
	}
}

private const val LEVEL_SECTION_PREFIX = "Под ваш уровень · "

private fun updateLevelSectionTitles(
	sections: List<ConstructorSection>,
	level: String,
): List<ConstructorSection> =
	sections.map { section ->
		if (section.title.startsWith(LEVEL_SECTION_PREFIX)) {
			section.copy(title = "$LEVEL_SECTION_PREFIX$level")
		} else {
			section
		}
	}
