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

		data class LevelUpdated(val level: String) : Msg
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
			CardsStore.Intent,
			Action,
			CardsComponent.State,
			Msg,
			CardsComponent.Label,
			>() {

		private var allSections: List<CardsSection> = emptyList()

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
					dispatch(
						Msg.SearchUpdated(
							query = intent.query,
							sections = filterSections(allSections, intent.query),
						),
					)
				}
				is CardsStore.Intent.SelectCard -> {
					val item = allSections
						.asSequence()
						.flatMap { it.items.asSequence() }
						.firstOrNull { it.id == intent.cardId }
						?: return
					publish(CardsComponent.Label.OpenCard(item = item))
				}
				is CardsStore.Intent.ChangeLevel -> changeLevel(level = intent.level)
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

		private fun loadCards() {
			dispatch(Msg.Loading)
			launchTry {
				val cards = getCardsUseCase()
				allSections = cards.sections
				dispatch(Msg.CardsLoaded(cards = cards))
			} catch {
				dispatch(Msg.SetError)
			}
		}

		private fun filterSections(
			sections: List<CardsSection>,
			query: String,
		): List<CardsSection> {
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
	sections: List<CardsSection>,
	level: String,
): List<CardsSection> =
	sections.map { section ->
		if (section.title.startsWith(LEVEL_SECTION_PREFIX)) {
			section.copy(title = "$LEVEL_SECTION_PREFIX$level")
		} else {
			section
		}
	}