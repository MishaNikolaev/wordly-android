package com.nmichail.wordly.android.core.fakenetwork.methods

import android.content.Context
import android.net.Uri
import com.nmichail.wordly.android.core.fakenetwork.FakeServerResponses
import com.nmichail.wordly.android.core.fakenetwork.R
import com.nmichail.wordly.android.core.fakenetwork.create
import com.nmichail.wordly.android.core.fakenetwork.error404
import com.nmichail.wordly.android.core.fakenetwork.getJson
import okhttp3.Response

internal fun get(context: Context, uri: Uri, response: Response.Builder): Response.Builder {
	val path = uri.path ?: return response.error404(context)

	FakeServerResponses.takeNextResponse(
		method = "GET",
		path = path,
		query = uri.query,
	)?.let { mockResponse ->
		return response.create(
			code = mockResponse.code,
			description = "Mock GET $path",
			body = mockResponse.body,
		)
	}

	return responseForPath(context = context, path = path, response = response)
}

private fun responseForPath(
	context: Context,
	path: String,
	response: Response.Builder,
): Response.Builder =
	when (path) {
		"/api/gateway/session" -> response.create(
			description = "Auth session",
			body = context.getJson(R.raw.session_ok),
		)
		"/api/gateway/profile" -> response.create(
			description = "User profile",
			body = context.getJson(R.raw.profile_ok),
		)
		"/api/home" -> response.create(
			description = "Home screen",
			body = context.getJson(R.raw.get_home),
		)
		"/api/review/session" -> response.create(
			description = "Review session",
			body = context.getJson(R.raw.get_review_session),
		)
		"/api/gateway/cards" -> response.create(
			description = "Cards catalog",
			body = context.getJson(R.raw.get_cards),
		)
		"/api/gateway/constructor" -> response.create(
			description = "Constructor catalog",
			body = context.getJson(R.raw.get_constructor),
		)
		else -> responseForDynamicPath(context = context, path = path, response = response)
	}

private fun responseForDynamicPath(
	context: Context,
	path: String,
	response: Response.Builder,
): Response.Builder =
	cardSessionResponse(context, path, response)
		?: constructorSessionResponse(context, path, response)
		?: newsDetailResponse(context, path, response)
		?: response.error404(context)

private fun cardSessionResponse(
	context: Context,
	path: String,
	response: Response.Builder,
): Response.Builder? {
	val cardId = Regex("^/api/gateway/cards/([^/]+)/session$")
		.matchEntire(path)
		?.groupValues
		?.get(1)
		?: return null
	if (cardId !in setOf("science", "journalism", "medicine", "engineering")) {
		return response.error404(context)
	}
	return response.create(
		description = "Card practice session",
		body = context.getJson(R.raw.get_cards_session),
	)
}

private fun constructorSessionResponse(
	context: Context,
	path: String,
	response: Response.Builder,
): Response.Builder? {
	val themeId = Regex("^/api/gateway/constructor/([^/]+)/session$")
		.matchEntire(path)
		?.groupValues
		?.get(1)
		?: return null
	if (themeId !in setOf("philosophy", "movies", "books")) {
		return response.error404(context)
	}
	return response.create(
		description = "Constructor session",
		body = context.getJson(R.raw.get_constructor_session),
	)
}

private fun newsDetailResponse(
	context: Context,
	path: String,
	response: Response.Builder,
): Response.Builder? {
	val newsId = pathSegmentId(path = path, prefix = "/api/news/") ?: return null
	val body = newsJson(context, newsId) ?: return response.error404(context)
	return response.create(description = "News detail", body = body)
}

private fun pathSegmentId(path: String, prefix: String): String? =
	path.removePrefix(prefix).takeIf {
		path.startsWith(prefix) && it.isNotEmpty() && !it.contains('/')
	}

private fun newsJson(context: Context, newsId: String): String? {
	val rawId = when (newsId) {
		"phrasal-verbs" -> R.raw.get_news_phrasal_verbs
		"spaced-repetition" -> R.raw.get_news_spaced_repetition
		"listening-tip" -> R.raw.get_news_listening_tip
		else -> return null
	}
	return context.getJson(rawId)
}
