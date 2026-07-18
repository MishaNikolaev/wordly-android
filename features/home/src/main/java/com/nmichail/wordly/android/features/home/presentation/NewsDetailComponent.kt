package com.nmichail.wordly.android.features.home.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.home.domain.entity.News

interface NewsDetailComponent {

	val model: Value<State>

	fun handleBack()

	data class State(
		val title: String,
		val subtitle: String,
		val body: String,
		val publishedAt: String,
	)

	fun interface Factory {

		operator fun invoke(
			componentContext: ComponentContext,
			news: News,
			onBack: () -> Unit,
		): NewsDetailComponent
	}
}
