package com.nmichail.wordly.android.features.words.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.component.presentation.launchTry
import com.nmichail.wordly.android.core.navigation.asValue
import com.nmichail.wordly.android.features.words.domain.entity.WordFilter
import com.nmichail.wordly.android.features.words.domain.entity.WordStatus
import com.nmichail.wordly.android.features.words.presentation.detail.WordDetailStore
import com.nmichail.wordly.android.features.words.presentation.detail.WordDetailStoreFactory
import com.nmichail.wordly.android.features.words.presentation.dialog.AddWordStore
import com.nmichail.wordly.android.features.words.presentation.dialog.AddWordStoreFactory
import com.nmichail.wordly.android.features.words.presentation.list.WordsListStore
import com.nmichail.wordly.android.features.words.presentation.list.WordsListStoreFactory

@Suppress("TooManyFunctions")
internal class DefaultWordsComponent(
    componentContext: ComponentContext,
    wordsListStoreFactory: WordsListStoreFactory,
    addWordStoreFactory: AddWordStoreFactory,
    wordDetailStoreFactory: WordDetailStoreFactory,
) : ComponentContext by componentContext,
	WordsComponent {

	private val listStore: WordsListStore = instanceKeeper.getStore {
		wordsListStoreFactory.create()
	}

	private val addWordStore: AddWordStore = instanceKeeper.getStore {
		addWordStoreFactory.create()
	}

	private val wordDetailStore: WordDetailStore = instanceKeeper.getStore {
		wordDetailStoreFactory.create()
	}

	override val listModel: Value<WordsListStore.State> = listStore.asValue()

	override val addWordModel: Value<AddWordStore.State> = addWordStore.asValue()

	override val wordDetailModel: Value<WordDetailStore.State> = wordDetailStore.asValue()

	init {
		launchTry {
			for (label in addWordStore.labelsChannel(lifecycle)) {
				when (label) {
					AddWordStore.Label.Dismiss -> Unit
					AddWordStore.Label.WordAdded -> {
						listStore.accept(WordsListStore.Intent.Refresh)
					}
				}
			}
		} catch {
			// ignored
		}
		launchTry {
			for (label in wordDetailStore.labelsChannel(lifecycle)) {
				when (label) {
					WordDetailStore.Label.Dismiss -> Unit
					WordDetailStore.Label.Changed -> {
						listStore.accept(WordsListStore.Intent.Refresh)
					}
				}
			}
		} catch {
			// ignored
		}
	}

	override fun handleRetry() {
		listStore.accept(WordsListStore.Intent.Retry)
	}

	override fun handleSearchQueryChange(query: String) {
		listStore.accept(WordsListStore.Intent.ChangeSearchQuery(query = query))
	}

	override fun handleFilterChange(filter: WordFilter) {
		listStore.accept(WordsListStore.Intent.ChangeFilter(filter = filter))
	}

	override fun handleOpenAddWord() {
		val content = listStore.state as? WordsListStore.State.Content ?: return
		wordDetailStore.accept(WordDetailStore.Intent.Dismiss)
		addWordStore.accept(AddWordStore.Intent.Open(availableTags = content.tags))
	}

	override fun handleDismissAddWord() {
		addWordStore.accept(AddWordStore.Intent.Dismiss)
	}

	override fun handleAddWordInputChange(value: String) {
		addWordStore.accept(AddWordStore.Intent.ChangeWordInput(value = value))
	}

	override fun handleToggleTag(tagId: String) {
		addWordStore.accept(AddWordStore.Intent.ToggleTag(tagId = tagId))
	}

	override fun handleConfirmAddWord() {
		addWordStore.accept(AddWordStore.Intent.Confirm)
	}

	override fun handleOpenWordDetail(wordId: String) {
		val content = listStore.state as? WordsListStore.State.Content ?: return
		val word = content.words.find { it.id == wordId } ?: return
		addWordStore.accept(AddWordStore.Intent.Dismiss)
		wordDetailStore.accept(WordDetailStore.Intent.Open(word = word))
	}

	override fun handleDismissWordDetail() {
		wordDetailStore.accept(WordDetailStore.Intent.Dismiss)
	}

	override fun handleDetailStatusChange(status: WordStatus) {
		wordDetailStore.accept(WordDetailStore.Intent.ChangeStatus(status = status))
	}

	override fun handleConfirmAddToReview() {
		wordDetailStore.accept(WordDetailStore.Intent.ConfirmAddToReview)
	}

	override fun handlePlayAudio() {
		wordDetailStore.accept(WordDetailStore.Intent.PlayAudio)
	}

	override fun handleCalendar(action: WordDetailStore.CalendarAction) {
		wordDetailStore.accept(WordDetailStore.Intent.Calendar(action = action))
	}
}
