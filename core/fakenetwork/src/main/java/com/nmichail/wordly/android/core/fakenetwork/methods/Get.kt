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

		else -> response.error404(context)
	}
}
