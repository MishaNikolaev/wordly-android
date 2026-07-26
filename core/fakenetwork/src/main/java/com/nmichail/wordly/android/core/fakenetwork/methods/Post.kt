package com.nmichail.wordly.android.core.fakenetwork.methods

import android.content.Context
import android.net.Uri
import com.nmichail.wordly.android.core.fakenetwork.FakeEnglishLevelStore
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

	return when (path) {
		"/api/gateway/authorization" -> {
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
		}

		"/api/gateway/registration" -> response.create(
			description = "Registration",
			body = context.getJson(R.raw.registration_ok),
		)

		"/api/gateway/refresh" -> response.create(
			description = "Refresh tokens",
			body = context.getJson(R.raw.refresh_ok),
		)

		"/api/gateway/password/reset" -> response.create(
			description = "Password reset requested",
		)

		"/api/gateway/logout" -> response.create(
			description = "Logout",
		)

		"/api/review/answer" -> response.create(
			description = "Review answer accepted; correct=true removes word from review queue",
		)

		"/api/gateway/english-level" -> updateEnglishLevel(
			response = response,
			requestBody = requestBody,
		)

		else -> response.error404(context)
	}
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
	FakeEnglishLevelStore.update(level = level)
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
