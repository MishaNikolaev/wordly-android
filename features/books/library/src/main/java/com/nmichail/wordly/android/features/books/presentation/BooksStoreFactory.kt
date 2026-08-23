package com.nmichail.wordly.android.features.books.presentation

import kotlinx.coroutines.launch
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.shared.englishlevel.domain.usecase.UpdateEnglishLevelUseCase
import com.nmichail.wordly.android.features.books.domain.entity.BooksCatalog
import com.nmichail.wordly.android.features.books.domain.entity.BooksSection
import com.nmichail.wordly.android.features.books.domain.usecase.GetBooksCatalogUseCase
import com.nmichail.wordly.android.shared.catalog.filterCatalogSections
import com.nmichail.wordly.android.shared.catalog.findCatalogItem
import com.nmichail.wordly.android.shared.catalog.matchesCatalogSearch
import com.nmichail.wordly.android.shared.catalog.regroupCatalogSectionsByLevel
import javax.inject.Inject

internal class BooksStoreFactory @Inject constructor(
    private val getBooksCatalogUseCase: GetBooksCatalogUseCase,
    private val updateEnglishLevelUseCase: UpdateEnglishLevelUseCase,
) {

    private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

    fun create(): BooksStore =
        object :
            BooksStore,
            Store<BooksStore.Intent, BooksStore.State, BooksStore.Label> by storeFactory.create(
                name = "BooksStore",
                initialState = BooksStore.State.Initial,
                bootstrapper = SimpleBootstrapper(Action.Init),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl,
            ) {}

    private sealed interface Action {

        data object Init : Action
    }

    private sealed interface Msg {

        data object Loading : Msg

        data class CatalogLoaded(val catalog: BooksCatalog) : Msg

        data object SetError : Msg

        data class SearchUpdated(
            val query: String,
            val sections: List<BooksSection>,
        ) : Msg

        data class LevelUpdated(
            val level: String,
            val allSections: List<BooksSection>,
            val sections: List<BooksSection>,
        ) : Msg
    }

    private object ReducerImpl : Reducer<BooksStore.State, Msg> {

        override fun BooksStore.State.reduce(msg: Msg): BooksStore.State =
            when (msg) {
                Msg.Loading -> BooksStore.State.Loading
                is Msg.CatalogLoaded -> {
                    val level = msg.catalog.levelBanner?.levelLabel.orEmpty().ifBlank { "B1" }
                    val sections = regroupCatalogSectionsByLevel(
                        sections = msg.catalog.sections,
                        level = level,
                        getItems = { it.items },
                        getBadge = { it.badge },
                        createSection = { title, items -> BooksSection(title = title, items = items) },
                    )
                    BooksStore.State.Content(
                        title = msg.catalog.title,
                        searchQuery = "",
                        searchPlaceholder = msg.catalog.searchPlaceholder,
                        levelBanner = msg.catalog.levelBanner,
                        allSections = sections,
                        sections = sections,
                    )
                }

                Msg.SetError -> BooksStore.State.Error
                is Msg.SearchUpdated -> {
                    val content = this as? BooksStore.State.Content ?: return this
                    content.copy(
                        searchQuery = msg.query,
                        sections = msg.sections,
                    )
                }

                is Msg.LevelUpdated -> {
                    val content = this as? BooksStore.State.Content ?: return this
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
                BooksStore.Intent,
                Action,
                BooksStore.State,
                Msg,
                BooksStore.Label,
                >() {

        override fun executeAction(action: Action) {
            when (action) {
                Action.Init -> loadCatalog()
            }
        }

        override fun executeIntent(intent: BooksStore.Intent) {
            when (intent) {
                BooksStore.Intent.Back -> publish(BooksStore.Label.Close)
                BooksStore.Intent.Retry -> loadCatalog()
                is BooksStore.Intent.ChangeSearchQuery -> {
                    val content = state() as? BooksStore.State.Content ?: return
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

                is BooksStore.Intent.SelectBook -> {
                    val content = state() as? BooksStore.State.Content ?: return
                    val book = findCatalogItem(
                        sections = content.allSections,
                        getItems = { it.items },
                        predicate = { it.id == intent.bookId },
                    ) ?: return
                    publish(BooksStore.Label.OpenBook(book = book))
                }

                is BooksStore.Intent.ChangeLevel -> changeLevel(level = intent.level)
            }
        }

        private fun changeLevel(level: String) {
            val content = state() as? BooksStore.State.Content ?: return
            scope.launch {
                updateEnglishLevelUseCase(level)
                val allSections = regroupCatalogSectionsByLevel(
                    sections = content.allSections,
                    level = level,
                    getItems = { it.items },
                    getBadge = { it.badge },
                    createSection = { title, items -> BooksSection(title = title, items = items) },
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
                    val catalog = getBooksCatalogUseCase()
                    dispatch(Msg.CatalogLoaded(catalog = catalog))
                } catch (_: Exception) {
                    dispatch(Msg.SetError)
                }
            }
        }
    }
}