package com.nmichail.wordly.android.features.news.presentation

import com.arkivanov.mvikotlin.core.store.Store

internal interface NewsDetailStore :
	Store<NewsDetailStore.Intent, NewsDetailComponent.State, NewsDetailComponent.Label> {

	sealed interface Intent {

		data object Back : Intent

		data object Retry : Intent

		data object Bookmark : Intent
	}
}