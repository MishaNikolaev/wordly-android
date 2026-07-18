package com.nmichail.wordly.android.features.news.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.component.presentation.launchTry
import com.nmichail.wordly.android.core.navigation.asValue

internal class DefaultNewsDetailComponent(
	componentContext: ComponentContext,
	newsId: String,
	newsDetailStoreFactory: NewsDetailStoreFactory,
	private val newsDetailRouter: NewsDetailRouter,
) : ComponentContext by componentContext,
	NewsDetailComponent {

	private val store: NewsDetailStore = instanceKeeper.getStore {
		newsDetailStoreFactory.create(newsId = newsId)
	}

	override val model: Value<NewsDetailComponent.State> = store.asValue()

	init {
		launchTry {
			for (label in store.labelsChannel(lifecycle)) {
				when (label) {
					NewsDetailComponent.Label.Close -> newsDetailRouter.navigateBack()
				}
			}
		} catch {
			// ignored
		}
	}

	override fun handleBack() {
		store.accept(NewsDetailStore.Intent.Back)
	}

	override fun handleShare() {
		store.accept(NewsDetailStore.Intent.Share)
	}

	override fun handleBookmark() {
		store.accept(NewsDetailStore.Intent.Bookmark)
	}
}
