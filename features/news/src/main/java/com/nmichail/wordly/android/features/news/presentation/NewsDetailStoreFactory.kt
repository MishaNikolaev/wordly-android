package com.nmichail.wordly.android.features.news.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.nmichail.wordly.android.component.presentation.BaseCoroutineExecutor
import com.nmichail.wordly.android.features.news.domain.entity.News
import com.nmichail.wordly.android.features.news.domain.usecase.GetNewsUseCase
import javax.inject.Inject

internal class NewsDetailStoreFactory @Inject constructor(
	private val getNewsUseCase: GetNewsUseCase,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(newsId: String): NewsDetailStore =
		object :
			NewsDetailStore,
			Store<NewsDetailStore.Intent, NewsDetailComponent.State, NewsDetailComponent.Label>
			by storeFactory.create(
				name = "NewsDetailStore",
				initialState = NewsDetailComponent.State(),
				bootstrapper = SimpleBootstrapper(Action.Load(newsId = newsId)),
				executorFactory = ::ExecutorImpl,
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data class Load(val newsId: String) : Action
	}

	private sealed interface Msg {

		data class NewsLoaded(val news: News) : Msg
	}

	private object ReducerImpl : Reducer<NewsDetailComponent.State, Msg> {

		override fun NewsDetailComponent.State.reduce(msg: Msg): NewsDetailComponent.State =
			when (msg) {
				is Msg.NewsLoaded -> copy(
					title = msg.news.title,
					publishedAt = msg.news.publishedAt,
					readingMinutes = msg.news.readingMinutes,
					author = msg.news.author,
					imageUrl = msg.news.imageUrl,
					content = msg.news.content,
					isLoading = false,
				)
			}
	}

	private inner class ExecutorImpl :
		BaseCoroutineExecutor<
			NewsDetailStore.Intent,
			Action,
			NewsDetailComponent.State,
			Msg,
			NewsDetailComponent.Label,
			>() {

		override fun executeAction(action: Action) {
			when (action) {
				is Action.Load -> launchTry {
					val news = getNewsUseCase(action.newsId)
					dispatch(Msg.NewsLoaded(news = news))
				} catch {
					// empty
				}
			}
		}

		override fun executeIntent(intent: NewsDetailStore.Intent) {
			when (intent) {
				NewsDetailStore.Intent.Back -> publish(NewsDetailComponent.Label.Close)
				NewsDetailStore.Intent.Share,
				NewsDetailStore.Intent.Bookmark,
				-> Unit
			}
		}
	}
}
