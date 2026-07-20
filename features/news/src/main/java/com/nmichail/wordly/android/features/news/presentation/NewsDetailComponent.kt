package com.nmichail.wordly.android.features.news.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.news.domain.entity.NewsContentBlock
import kotlinx.coroutines.channels.ReceiveChannel

interface NewsDetailComponent {

	val model: Value<State>

	fun labelsChannel(): ReceiveChannel<Label>

	fun handleBack()

	fun handleRetry()

	fun handleBookmark()

	sealed interface State {

		data object Loading : State

		data object Error : State

		data class Content(
			val title: String,
			val subtitle: String,
			val publishedAt: String,
			val readingMinutes: Int,
			val author: String,
			val imageUrl: String?,
			val content: List<NewsContentBlock>,
			val isBookmarked: Boolean,
		) : State
	}

	sealed interface Label {

		data object Close : Label

		data object NoConnection : Label

		data object NotFound : Label

		data object UnknownError : Label
	}

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			newsId: String,
			newsDetailRouter: NewsDetailRouter,
		): NewsDetailComponent
	}
}