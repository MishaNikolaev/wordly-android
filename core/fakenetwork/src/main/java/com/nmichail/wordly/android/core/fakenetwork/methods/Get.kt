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

	val handler = getHandlers.entries.firstOrNull {
		it.key.matches(path)
	}?.value
	return handler?.invoke(context, uri, response) ?: response.error404(context)
}

private typealias Handler = (Context, Uri, Response.Builder) -> Response.Builder

private val getHandlers: Map<Regex, Handler> = mapOf(
	Regex("^/api/gateway/session$") to { context, _, response ->
		response.create(description = "Auth session", body = context.getJson(R.raw.session_ok))
	},
	Regex("^/api/gateway/profile$") to { context, _, response ->
		response.create(description = "User profile", body = context.getJson(R.raw.profile_ok))
	},
	Regex("^/api/home$") to { context, _, response ->
		response.create(description = "Home screen", body = context.getJson(R.raw.get_home))
	},
	Regex("^/api/review/session$") to { context, _, response ->
		response.create(description = "Review session", body = context.getJson(R.raw.get_review_session))
	},
	Regex("^/api/gateway/cards$") to { context, _, response ->
		response.create(description = "Cards catalog", body = context.getJson(R.raw.get_cards))
	},
	Regex("^/api/gateway/constructor$") to { context, _, response ->
		response.create(description = "Constructor catalog", body = context.getJson(R.raw.get_constructor))
	},
	Regex("^/api/gateway/books$") to { context, _, response ->
		response.create(description = "Books catalog", body = context.getJson(R.raw.get_books))
	},
	Regex("^/api/words$") to { context, _, response ->
		response.create(description = "Words catalog", body = context.getJson(R.raw.get_words))
	},
	Regex("^/api/gateway/cards/([^/]+)/session$") to { context, uri, response ->
		val cardId = uri.path?.removePrefix("/api/gateway/cards/")?.removeSuffix("/session").orEmpty()
		if (cardId !in setOf("science", "journalism", "medicine", "engineering")) {
			response.error404(context)
		} else {
			response.create(description = "Card practice session", body = context.getJson(R.raw.get_cards_session))
		}
	},
	Regex("^/api/gateway/constructor/([^/]+)/session$") to { context, uri, response ->
		val themeId = uri.path?.removePrefix("/api/gateway/constructor/")?.removeSuffix("/session").orEmpty()
		if (themeId !in setOf("philosophy", "movies", "books")) {
			response.error404(context)
		} else {
			response.create(description = "Constructor session", body = context.getJson(R.raw.get_constructor_session))
		}
	},
	Regex("^/api/gateway/books/([^/]+)$") to { context, uri, response ->
		val bookId = uri.path?.removePrefix("/api/gateway/books/").orEmpty()
		if (bookId !in setOf("little-prince", "animal-farm", "1984")) {
			response.error404(context)
		} else {
			response.create(description = "Book content", body = context.getJson(R.raw.get_book_little_prince))
		}
	},
	Regex("^/api/gateway/books/([^/]+)/translation$") to { context, uri, response ->
		val bookId = uri.path?.removePrefix("/api/gateway/books/")?.removeSuffix("/translation").orEmpty()
		if (bookId !in setOf("little-prince", "animal-farm", "1984")) {
			response.error404(context)
		} else {
			response.create(description = "Book translation", body = context.getJson(R.raw.get_book_translation_little_prince))
		}
	},
	Regex("^/api/news/([^/]+)$") to { context, uri, response ->
		val newsId = uri.path?.removePrefix("/api/news/").orEmpty()
		val body = when (newsId) {
			"phrasal-verbs" -> context.getJson(R.raw.get_news_phrasal_verbs)
			"spaced-repetition" -> context.getJson(R.raw.get_news_spaced_repetition)
			"listening-tip" -> context.getJson(R.raw.get_news_listening_tip)
			else -> null
		}
		if (body == null) {
			response.error404(context)
		} else {
			response.create(description = "News detail", body = body)
		}
	},
)
