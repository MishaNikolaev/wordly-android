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
import com.nmichail.wordly.android.shared.error.NetworkExceptionConverter
import com.nmichail.wordly.android.shared.error.StatusCodes
import com.nmichail.wordly.android.shared.error.messageIdOrNull
import com.nmichail.wordly.android.shared.error.presentation.ErrorDelegate
import com.nmichail.wordly.android.shared.error.presentation.HandleErrorResult
import javax.inject.Inject

internal class NewsDetailStoreFactory @Inject constructor(
	private val getNewsUseCase: GetNewsUseCase,
	private val networkExceptionConverter: NetworkExceptionConverter,
	private val errorDelegate: ErrorDelegate,
) {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(newsId: String): NewsDetailStore =
		object :
			NewsDetailStore,
			Store<NewsDetailStore.Intent, NewsDetailComponent.State, NewsDetailComponent.Label>
			by storeFactory.create(
				name = "NewsDetailStore",
				initialState = NewsDetailComponent.State.Loading,
				bootstrapper = SimpleBootstrapper(Action.Load(newsId = newsId)),
				executorFactory = { ExecutorImpl(newsId = newsId) },
				reducer = ReducerImpl,
			) {}

	private sealed interface Action {

		data class Load(val newsId: String) : Action
	}

	private sealed interface Msg {

		data object Loading : Msg

		data class NewsLoaded(val news: News) : Msg

		data object SetError : Msg

		data object ToggleBookmark : Msg
	}

	private object ReducerImpl : Reducer<NewsDetailComponent.State, Msg> {

		override fun NewsDetailComponent.State.reduce(msg: Msg): NewsDetailComponent.State =
			when (msg) {
				Msg.Loading -> NewsDetailComponent.State.Loading
				is Msg.NewsLoaded -> NewsDetailComponent.State.Content(
					title = msg.news.title,
					subtitle = msg.news.subtitle,
					publishedAt = msg.news.publishedAt,
					readingMinutes = msg.news.readingMinutes,
					author = msg.news.author,
					imageUrl = msg.news.imageUrl,
					content = msg.news.content,
					isBookmarked = false,
				)
				Msg.SetError -> NewsDetailComponent.State.Error
				Msg.ToggleBookmark -> when (this) {
					is NewsDetailComponent.State.Content -> copy(isBookmarked = !isBookmarked)
					NewsDetailComponent.State.Loading,
					NewsDetailComponent.State.Error,
					-> this
				}
			}
	}

	private inner class ExecutorImpl(
		private val newsId: String,
	) : BaseCoroutineExecutor<
		NewsDetailStore.Intent,
		Action,
		NewsDetailComponent.State,
		Msg,
		NewsDetailComponent.Label,
		>() {

		override fun executeAction(action: Action) {
			when (action) {
				is Action.Load -> loadNews(newsId = action.newsId)
			}
		}

		override fun executeIntent(intent: NewsDetailStore.Intent) {
			when (intent) {
				NewsDetailStore.Intent.Back -> publish(NewsDetailComponent.Label.Close)
				NewsDetailStore.Intent.Retry -> loadNews(newsId = newsId)
				NewsDetailStore.Intent.Bookmark -> dispatch(Msg.ToggleBookmark)
			}
		}

		private fun loadNews(newsId: String) {
			dispatch(Msg.Loading)
			launchTry {
				val news = getNewsUseCase(newsId)
				dispatch(Msg.NewsLoaded(news = news))
			} catch { error ->
				handleLoadError(error)
			}
		}

		private fun handleLoadError(error: Exception) {
			val networkError = networkExceptionConverter.convert(error)
			if (errorDelegate.handleError(networkError) == HandleErrorResult.HANDLED) {
				return
			}

			dispatch(Msg.SetError)
			publish(errorLabel(networkError.messageIdOrNull()))
		}

		private fun errorLabel(messageId: Int?): NewsDetailComponent.Label =
			when (messageId) {
				StatusCodes.NO_CONNECTION.statusCode -> NewsDetailComponent.Label.NoConnection
				StatusCodes.ENTITY_NOT_FOUND.statusCode,
				StatusCodes.ENTITY_WAS_NOT_FOUND.statusCode,
				-> NewsDetailComponent.Label.NotFound
				else -> NewsDetailComponent.Label.UnknownError
			}
	}
}
