package com.nmichail.wordly.android.core.fakenetwork.methods

import android.content.Context
import android.net.Uri
import com.nmichail.wordly.android.core.fakenetwork.FakeServerResponses
import com.nmichail.wordly.android.core.fakenetwork.R
import com.nmichail.wordly.android.core.fakenetwork.create
import com.nmichail.wordly.android.core.fakenetwork.error404
import com.nmichail.wordly.android.core.fakenetwork.getJson
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject

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
		response.create(
			description = "User profile",
			body = context.getJson(R.raw.profile_ok),
		)
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
	Regex("^/api/words$") to { context, uri, response ->
		val body = filterWordsCatalog(
			json = context.getJson(R.raw.get_words),
			status = uri.getQueryParameter("status"),
			query = uri.getQueryParameter("query"),
		)
		response.create(description = "Words catalog", body = body)
	},
	Regex("^/api/materials$") to { context, uri, response ->
		val body = filterMaterialsCatalog(
			json = context.getJson(R.raw.get_materials),
			category = uri.getQueryParameter("category"),
		)
		response.create(description = "Materials catalog", body = body)
	},
	Regex("^/api/materials/([^/]+)$") to { context, uri, response ->
		val materialId = uri.path?.removePrefix("/api/materials/").orEmpty()
		val body = when (materialId) {
			"present-perfect" -> context.getJson(R.raw.get_material_present_perfect)
			"daily-idioms" -> context.getJson(R.raw.get_material_daily_idioms)
			"small-talk" -> context.getJson(R.raw.get_material_small_talk)
			"listening-shadowing" -> context.getJson(R.raw.get_material_listening_shadowing)
			"conditionals" -> context.getJson(R.raw.get_material_conditionals)
			else -> null
		}
		if (body == null) {
			response.error404(context)
		} else {
			response.create(description = "Material detail", body = body)
		}
	},
	Regex("^/api/gateway/cards/([^/]+)/session$") to { context, uri, response ->
		val cardId = uri.path?.removePrefix("/api/gateway/cards/")?.removeSuffix("/session").orEmpty()
		if (cardId.isBlank()) {
			response.error404(context)
		} else {
			response.create(description = "Card practice session", body = context.getJson(R.raw.get_cards_session))
		}
	},
	Regex("^/api/gateway/constructor/([^/]+)/session$") to { context, uri, response ->
		val themeId = uri.path?.removePrefix("/api/gateway/constructor/")?.removeSuffix("/session").orEmpty()
		if (themeId.isBlank()) {
			response.error404(context)
		} else {
			val body = JSONObject(context.getJson(R.raw.get_constructor_session)).apply {
				put("themeId", themeId)
			}.toString()
			response.create(description = "Constructor session", body = body)
		}
	},
	Regex("^/api/gateway/books/([^/]+)/detail$") to { context, uri, response ->
		val bookId = uri.path?.removePrefix("/api/gateway/books/")?.removeSuffix("/detail").orEmpty()
		val detail = context.bookDetailJson(bookId)
		if (detail == null) {
			response.error404(context)
		} else {
			response.create(description = "Book detail", body = detail.toString())
		}
	},
	Regex("^/api/gateway/books/([^/]+)$") to { context, uri, response ->
		val bookId = uri.path?.removePrefix("/api/gateway/books/").orEmpty()
		if (bookId.isBlank()) {
			response.error404(context)
		} else {
			val raw = context.bookContentRaw(bookId)
			if (raw == null) {
				response.error404(context)
			} else {
				response.create(description = "Book content", body = context.getJson(raw))
			}
		}
	},
	Regex("^/api/gateway/books/([^/]+)/translation$") to { context, uri, response ->
		val bookId = uri.path?.removePrefix("/api/gateway/books/")?.removeSuffix("/translation").orEmpty()
		if (bookId.isBlank()) {
			response.error404(context)
		} else {
			val raw = context.bookTranslationRaw(bookId)
			if (raw == null) {
				response.error404(context)
			} else {
				response.create(description = "Book translation", body = context.getJson(raw))
			}
		}
	},
	Regex("^/api/gateway/vocabulary/lookup$") to { _, uri, response ->
		val query = uri.getQueryParameter("q")?.trim().orEmpty().ifBlank { "resilience" }
		response.create(
			description = "Vocabulary lookup",
			body = JSONObject()
				.put("word", query)
				.put("phonetic", "/ˈwɜːd/")
				.put("translation", "слово, термин")
				.put("definition", "a single distinct meaningful element of speech or writing")
				.put("audioUrl", JSONObject.NULL)
				.put(
					"examples",
					JSONArray().put(
						JSONObject()
							.put("text", "This word is useful.")
							.put("translation", "Это слово полезно."),
					),
				)
				.put("level", "A1")
				.put("difficulty", 1)
				.put("pos", "noun")
				.toString(),
		)
	},
)

private fun filterMaterialsCatalog(json: String, category: String?): String {
	if (category.isNullOrBlank()) return json
	val root = JSONObject(json)
	val items = root.getJSONArray("items")
	val filtered = JSONArray()
	for (index in 0 until items.length()) {
		val item = items.getJSONObject(index)
		if (item.optString("category").equals(category, ignoreCase = true)) {
			filtered.put(item)
		}
	}
	root.put("items", filtered)
	return root.toString()
}

private fun filterWordsCatalog(json: String, status: String?, query: String?): String {
	if (status.isNullOrBlank() && query.isNullOrBlank()) return json
	val root = JSONObject(json)
	val words = root.getJSONArray("words")
	val normalizedQuery = query?.trim().orEmpty()
	val filtered = JSONArray()
	for (index in 0 until words.length()) {
		val word = words.getJSONObject(index)
		val matchesStatus = status.isNullOrBlank() ||
			word.optString("status").equals(status, ignoreCase = true)
		val matchesQuery = normalizedQuery.isEmpty() || wordMatchesQuery(word, normalizedQuery)
		if (matchesStatus && matchesQuery) {
			filtered.put(word)
		}
	}
	root.put("words", filtered)
	return root.toString()
}

private fun wordMatchesQuery(word: JSONObject, query: String): Boolean {
	val fields = listOf(
		word.optString("word"),
		word.optString("translation"),
		word.optString("definition"),
		word.optString("phonetic"),
	)
	return fields.any { it.contains(query, ignoreCase = true) }
}

private fun Context.findBookCatalogItem(bookId: String): JSONObject? {
	val catalog = JSONObject(getJson(R.raw.get_books))
	val sections = catalog.getJSONArray("sections")
	for (sectionIndex in 0 until sections.length()) {
		val items = sections.getJSONObject(sectionIndex).getJSONArray("items")
		for (itemIndex in 0 until items.length()) {
			val item = items.getJSONObject(itemIndex)
			if (item.optString("id") == bookId) {
				return item
			}
		}
	}
	return null
}

private fun Context.bookDetailJson(bookId: String): JSONObject? {
	val item = findBookCatalogItem(bookId) ?: return null
	return JSONObject()
		.put("id", bookId)
		.put("title", item.optString("title"))
		.put("author", item.optString("author"))
		.put(
			"coverUrl",
			if (item.has("imageUrl") && !item.isNull("imageUrl")) {
				item.get("imageUrl")
			} else {
				JSONObject.NULL
			},
		)
		.put("genre", item.optString("genre"))
		.put("category", item.optString("category"))
		.put("badge", item.optString("badge"))
		.put("description", item.optString("description"))
}

private fun Context.bookRawResourceId(bookId: String, translation: Boolean): Int? {
	val normalized = bookId.replace('-', '_')
	val name = if (translation) {
		"get_book_translation_$normalized"
	} else {
		"get_book_$normalized"
	}
	val rawId = resources.getIdentifier(name, "raw", packageName)
	return rawId.takeIf { it != 0 }
}

private fun Context.bookContentRaw(bookId: String): Int? = bookRawResourceId(bookId, translation = false)

private fun Context.bookTranslationRaw(bookId: String): Int? = bookRawResourceId(bookId, translation = true)