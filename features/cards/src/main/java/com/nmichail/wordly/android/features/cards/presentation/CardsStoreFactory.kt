package com.nmichail.wordly.android.features.cards.presentation

import kotlinx.coroutines.launch
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.core.network.domain.usecase.UpdateEnglishLevelUseCase
import com.nmichail.wordly.android.features.cards.domain.entity.Cards
import com.nmichail.wordly.android.features.cards.domain.entity.CardsSection
import com.nmichail.wordly.android.features.cards.domain.usecase.GetCardsUseCase
import com.nmichail.wordly.android.shared.catalog.filterCatalogSections
import com.nmichail.wordly.android.shared.catalog.findCatalogItem
import com.nmichail.wordly.android.shared.catalog.matchesCatalogSearch
import com.nmichail.wordly.android.shared.catalog.updateCatalogLevelSectionTitles
import javax.inject.Inject

internal class CardsStoreFactory @Inject constructor(
	private val getCardsUseCase: GetCardsUseCase,
	private val updateEnglishLevelUseCase: UpdateEnglishLevelUseCase,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): CardsStore =
		object :
			CardsStore,
			Store<CardsStore.Intent, CardsStore.State, CardsStore.Label> by storeFactory.create(
				name = "CardsStore",
				initialState = CardsStore.State.Initial,
				bootstrapper = SimpleBootstrapper(Action.Init),
				executorFactory = ::ExecutorImpl,
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data object Init : Action
	}

	private sealed interface Msg {

		data object Loading : Msg

		data class CardsLoaded(val cards: Cards) : Msg

		data object SetError : Msg

		data class SearchUpdated(
			val query: String,
			val sections: List<CardsSection>,
		) : Msg

		data class LevelUpdated(
			val level: String,
			val allSections: List<CardsSection>,
			val sections: List<CardsSection>,
		) : Msg
	}

	private object ReducerImpl : Reducer<CardsStore.State, Msg> {

		override fun CardsStore.State.reduce(msg: Msg): CardsStore.State =
			when (msg) {
				Msg.Loading -> CardsStore.State.Loading
				is Msg.CardsLoaded -> CardsStore.State.Content(
					title = msg.cards.title,
					searchQuery = "",
					searchPlaceholder = msg.cards.searchPlaceholder,
					levelBanner = msg.cards.levelBanner,
					allSections = msg.cards.sections,
					sections = msg.cards.sections,
				)
				Msg.SetError -> CardsStore.State.Error
				is Msg.SearchUpdated -> {
					val content = this as? CardsStore.State.Content ?: return this
					content.copy(
						searchQuery = msg.query,
						sections = msg.sections,
					)
				}
				is Msg.LevelUpdated -> {
					val content = this as? CardsStore.State.Content ?: return this
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
			CardsStore.Intent,
			Action,
			CardsStore.State,
			Msg,
			CardsStore.Label,
			>() {

		override fun executeAction(action: Action) {
			when (action) {
				Action.Init -> loadCards()
			}
		}

		override fun executeIntent(intent: CardsStore.Intent) {
			when (intent) {
				CardsStore.Intent.Back -> publish(CardsStore.Label.Close)
				CardsStore.Intent.Retry -> loadCards()
				is CardsStore.Intent.ChangeSearchQuery -> {
					val content = state() as? CardsStore.State.Content ?: return
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
				is CardsStore.Intent.SelectCard -> {
					val content = state() as? CardsStore.State.Content ?: return
					val item = findCatalogItem(
						sections = content.allSections,
						getItems = { it.items },
						predicate = { it.id == intent.cardId },
					) ?: return
					publish(CardsStore.Label.OpenCard(item = item))
				}
				is CardsStore.Intent.ChangeLevel -> changeLevel(level = intent.level)
			}
		}

		private fun changeLevel(level: String) {
			val content = state() as? CardsStore.State.Content ?: return
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

		private fun loadCards() {
			dispatch(Msg.Loading)
			scope.launch {
				try {
					val cards = getCardsUseCase()
					dispatch(Msg.CardsLoaded(cards = cards))
				} catch (_: Exception) {
					dispatch(Msg.SetError)
				}
			}
		}
	}
}