package com.nmichail.wordly.android.features.cards.presentation

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
			Store<CardsStore.Intent, CardsComponent.State, CardsComponent.Label> by storeFactory.create(
				name = "CardsStore",
				initialState = CardsComponent.State.Loading,
				bootstrapper = SimpleBootstrapper(Action.Load),
				executorFactory = ::ExecutorImpl,
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data object Load : Action
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

	private object ReducerImpl : Reducer<CardsComponent.State, Msg> {

		override fun CardsComponent.State.reduce(msg: Msg): CardsComponent.State =
			when (msg) {
				Msg.Loading -> CardsComponent.State.Loading
				is Msg.CardsLoaded -> CardsComponent.State.Content(
					title = msg.cards.title,
					searchQuery = "",
					searchPlaceholder = msg.cards.searchPlaceholder,
					levelBanner = msg.cards.levelBanner,
					allSections = msg.cards.sections,
					sections = msg.cards.sections,
				)
				Msg.SetError -> CardsComponent.State.Error
				is Msg.SearchUpdated -> {
					val content = this as? CardsComponent.State.Content ?: return this
					content.copy(
						searchQuery = msg.query,
						sections = msg.sections,
					)
				}
				is Msg.LevelUpdated -> {
					val content = this as? CardsComponent.State.Content ?: return this
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
			CardsComponent.State,
			Msg,
			CardsComponent.Label,
			>() {

		override fun executeAction(action: Action) {
			when (action) {
				Action.Load -> loadCards()
			}
		}

		override fun executeIntent(intent: CardsStore.Intent) {
			when (intent) {
				CardsStore.Intent.Back -> publish(CardsComponent.Label.Close)
				CardsStore.Intent.Retry -> loadCards()
				is CardsStore.Intent.ChangeSearchQuery -> {
					val content = state() as? CardsComponent.State.Content ?: return
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
					val content = state() as? CardsComponent.State.Content ?: return
					val item = findCatalogItem(
						sections = content.allSections,
						getItems = { it.items },
						predicate = { it.id == intent.cardId },
					) ?: return
					publish(CardsComponent.Label.OpenCard(item = item))
				}
				is CardsStore.Intent.ChangeLevel -> changeLevel(level = intent.level)
			}
		}

		private fun changeLevel(level: String) {
			val content = state() as? CardsComponent.State.Content ?: return
			launchTry {
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
			} catch {
				// keep previous level
			}
		}

		private fun loadCards() {
			dispatch(Msg.Loading)
			launchTry {
				val cards = getCardsUseCase()
				dispatch(Msg.CardsLoaded(cards = cards))
			} catch {
				dispatch(Msg.SetError)
			}
		}
	}
}