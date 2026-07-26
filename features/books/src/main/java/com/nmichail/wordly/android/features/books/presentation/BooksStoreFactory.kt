package com.nmichail.wordly.android.features.books.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.core.network.domain.usecase.UpdateEnglishLevelUseCase
import com.nmichail.wordly.android.features.books.domain.entity.BooksCatalog
import com.nmichail.wordly.android.features.books.domain.entity.BooksSection
import com.nmichail.wordly.android.features.books.domain.usecase.GetBooksCatalogUseCase
import javax.inject.Inject

internal class BooksStoreFactory @Inject constructor(
	private val getBooksCatalogUseCase: GetBooksCatalogUseCase,
	private val updateEnglishLevelUseCase: UpdateEnglishLevelUseCase,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): BooksStore =
		object :
			BooksStore,
			Store<BooksStore.Intent, BooksComponent.State, BooksComponent.Label> by storeFactory.create(
				name = "BooksStore",
				initialState = BooksComponent.State.Loading,
				bootstrapper = SimpleBootstrapper(Action.Load),
				executorFactory = ::ExecutorImpl,
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data object Load : Action
	}

	private sealed interface Msg {

		data object Loading : Msg

		data class CatalogLoaded(val catalog: BooksCatalog) : Msg

		data object SetError : Msg

		data class SearchUpdated(
			val query: String,
			val sections: List<BooksSection>,
		) : Msg

		data class LevelUpdated(val level: String) : Msg
	}

	private object ReducerImpl : Reducer<BooksComponent.State, Msg> {

		override fun BooksComponent.State.reduce(msg: Msg): BooksComponent.State =
			when (msg) {
				Msg.Loading -> BooksComponent.State.Loading
				is Msg.CatalogLoaded -> BooksComponent.State.Content(
					title = msg.catalog.title,
					searchQuery = "",
					searchPlaceholder = msg.catalog.searchPlaceholder,
					levelBanner = msg.catalog.levelBanner,
					sections = msg.catalog.sections,
				)
				Msg.SetError -> BooksComponent.State.Error
				is Msg.SearchUpdated -> {
					val content = this as? BooksComponent.State.Content ?: return this
					content.copy(
						searchQuery = msg.query,
						sections = msg.sections,
					)
				}
				is Msg.LevelUpdated -> {
					val content = this as? BooksComponent.State.Content ?: return this
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
			BooksStore.Intent,
			Action,
			BooksComponent.State,
			Msg,
			BooksComponent.Label,
			>() {

		private var allSections: List<BooksSection> = emptyList()

		override fun executeAction(action: Action) {
			when (action) {
				Action.Load -> loadCatalog()
			}
		}

		override fun executeIntent(intent: BooksStore.Intent) {
			when (intent) {
				BooksStore.Intent.Back -> publish(BooksComponent.Label.Close)
				BooksStore.Intent.Retry -> loadCatalog()
				is BooksStore.Intent.ChangeSearchQuery -> {
					dispatch(
						Msg.SearchUpdated(
							query = intent.query,
							sections = filterSections(allSections, intent.query),
						),
					)
				}
				is BooksStore.Intent.SelectBook -> {
					val book = allSections
						.asSequence()
						.flatMap { it.items.asSequence() }
						.firstOrNull { it.id == intent.bookId }
						?: return
					publish(BooksComponent.Label.OpenBook(book = book))
				}
				is BooksStore.Intent.ChangeLevel -> changeLevel(level = intent.level)
			}
		}

		private fun changeLevel(level: String) {
			launchTry {
				updateEnglishLevelUseCase(level)
				allSections = updateLevelSectionTitles(sections = allSections, level = level)
				dispatch(Msg.LevelUpdated(level = level))
			} catch {
				// ignored
			}
		}

		private fun loadCatalog() {
			dispatch(Msg.Loading)
			launchTry {
				val catalog = getBooksCatalogUseCase()
				allSections = catalog.sections
				dispatch(Msg.CatalogLoaded(catalog = catalog))
			} catch {
				dispatch(Msg.SetError)
			}
		}

		private fun filterSections(
			sections: List<BooksSection>,
			query: String,
		): List<BooksSection> {
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
	sections: List<BooksSection>,
	level: String,
): List<BooksSection> =
	sections.map { section ->
		if (section.title.startsWith(LEVEL_SECTION_PREFIX)) {
			section.copy(title = "$LEVEL_SECTION_PREFIX$level")
		} else {
			section
		}
	}