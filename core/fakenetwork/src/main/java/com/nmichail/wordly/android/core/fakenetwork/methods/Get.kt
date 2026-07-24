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

	return when (path) {
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

		else -> {
			val newsId = path.removePrefix("/api/news/").takeIf {
				path.startsWith("/api/news/") && it.isNotEmpty() && !it.contains('/')
			}
			if (newsId != null) {
				val newsBody = newsJson(context, newsId)
				if (newsBody != null) {
					response.create(
						description = "News detail",
						body = newsBody,
					)
				} else {
					response.error404(context)
				}
			} else {
				response.error404(context)
			}
		}
	}
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
