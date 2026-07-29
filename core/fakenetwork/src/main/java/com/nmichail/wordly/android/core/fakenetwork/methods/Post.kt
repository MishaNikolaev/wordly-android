package com.nmichail.wordly.android.core.fakenetwork.methods

import android.content.Context
import android.net.Uri
import com.nmichail.wordly.android.core.fakenetwork.FakeServerResponses
import com.nmichail.wordly.android.core.fakenetwork.R
import com.nmichail.wordly.android.core.fakenetwork.create
import com.nmichail.wordly.android.core.fakenetwork.error404
import com.nmichail.wordly.android.core.fakenetwork.getJson
import okhttp3.Response
import org.json.JSONObject
import java.net.HttpURLConnection.HTTP_UNAUTHORIZED

internal fun post(
	context: Context,
	uri: Uri,
	response: Response.Builder,
	requestBody: String?,
): Response.Builder {
	val path = uri.path ?: return response.error404(context)

	FakeServerResponses.takeNextResponse(
		method = "POST",
		path = path,
		query = uri.query,
	)?.let { mockResponse ->
		return response.create(
			code = mockResponse.code,
			description = "Mock POST $path",
			body = mockResponse.body,
		)
	}
	val handler = postHandlers.entries.firstOrNull {
		it.key.matches(path)
	}?.value
	return handler?.invoke(context, uri, response, requestBody) ?: response.error404(context)
}

private fun addWordToReview(
	wordId: String,
	response: Response.Builder,
	requestBody: String?,
): Response.Builder {
	if (wordId.isBlank()) {
		return response.create(
			code = 400,
			description = "Word id is required",
			body = """{"message":"wordId is required"}""",
		)
	}
	val epochDay = runCatching {
		val json = JSONObject(requestBody.orEmpty())
		if (json.has("epochDay")) json.getLong("epochDay") else null
	}.getOrNull()
	return response.create(
		description = "Word added to review queue",
		body = JSONObject()
			.put("wordId", wordId)
			.put("epochDay", epochDay)
			.toString(),
	)
}

private fun updateWordStatus(
	wordId: String,
	response: Response.Builder,
	requestBody: String?,
): Response.Builder {
	if (wordId.isBlank()) {
		return response.create(
			code = 400,
			description = "Word id is required",
			body = """{"message":"wordId is required"}""",
		)
	}
	val status = runCatching {
		JSONObject(requestBody.orEmpty()).optString("status")
	}.getOrDefault("")
	if (status.isBlank()) {
		return response.create(
			code = 400,
			description = "Status is required",
			body = """{"message":"status is required"}""",
		)
	}
	return response.create(
		description = "Word status updated",
		body = JSONObject()
			.put("wordId", wordId)
			.put("status", status)
			.toString(),
	)
}

private fun updateEnglishLevel(
	response: Response.Builder,
	requestBody: String?,
): Response.Builder {
	val level = runCatching {
		JSONObject(requestBody.orEmpty()).optString("level")
	}.getOrDefault("")
	if (level.isBlank()) {
		return response.create(
			code = 400,
			description = "English level is required",
			body = """{"message":"level is required"}""",
		)
	}
	return response.create(
		description = "English level updated",
		body = JSONObject().put("level", level).toString(),
	)
}

private fun isValidDemoAuthorization(context: Context, requestBody: String?): Boolean {
	if (requestBody.isNullOrBlank()) return false
	return runCatching {
		val json = JSONObject(requestBody)
		val password = json.optString("password")
		password == context.getString(R.string.mock_demo_password)
	}.getOrDefault(false)
}

private typealias PostHandler = (Context, Uri, Response.Builder, String?) -> Response.Builder

private val postHandlers: Map<Regex, PostHandler> = mapOf(
	Regex("^/api/gateway/authorization$") to { context, _, response, requestBody ->
		if (isValidDemoAuthorization(context, requestBody)) {
			response.create(
				description = "Authorization",
				body = context.getJson(R.raw.authorization_ok),
			)
		} else {
			response.create(
				code = HTTP_UNAUTHORIZED,
				description = "Unauthorized",
				body = context.getJson(R.raw.authorization_unauthorized),
			)
		}
	},
	Regex("^/api/gateway/registration$") to { context, _, response, _ ->
		response.create(
			description = "Registration",
			body = context.getJson(R.raw.registration_ok),
		)
	},
	Regex("^/api/gateway/refresh$") to { context, _, response, _ ->
		response.create(
			description = "Refresh tokens",
			body = context.getJson(R.raw.refresh_ok),
		)
	},
	Regex("^/api/gateway/password/reset$") to { _, _, response, _ ->
		response.create(description = "Password reset requested")
	},
	Regex("^/api/gateway/logout$") to { _, _, response, _ ->
		response.create(description = "Logout")
	},
	Regex("^/api/review/answer$") to { _, _, response, _ ->
		response.create(description = "Review answer accepted; correct=true removes word from review queue")
	},
	Regex("^/api/words$") to { _, _, response, requestBody ->
		response.create(
			description = "Word created",
			body = requestBody.orEmpty().ifBlank { "{}" },
		)
	},
	Regex("^/api/gateway/english-level$") to { _, _, response, requestBody ->
		updateEnglishLevel(response = response, requestBody = requestBody)
	},
	Regex("""^/api/words/([^/]+)/review$""") to { _, uri, response, requestBody ->
		val wordId = uri.path?.removePrefix("/api/words/")?.removeSuffix("/review").orEmpty()
		addWordToReview(wordId = wordId, response = response, requestBody = requestBody)
	},
	Regex("""^/api/words/([^/]+)/status$""") to { _, uri, response, requestBody ->
		val wordId = uri.path?.removePrefix("/api/words/")?.removeSuffix("/status").orEmpty()
		updateWordStatus(wordId = wordId, response = response, requestBody = requestBody)
	},
)
