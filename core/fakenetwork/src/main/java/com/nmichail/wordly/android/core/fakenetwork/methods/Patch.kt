package com.nmichail.wordly.android.core.fakenetwork.methods

import android.content.Context
import android.net.Uri
import com.nmichail.wordly.android.core.fakenetwork.FakeServerResponses
import com.nmichail.wordly.android.core.fakenetwork.R
import com.nmichail.wordly.android.core.fakenetwork.create
import com.nmichail.wordly.android.core.fakenetwork.error404
import com.nmichail.wordly.android.core.fakenetwork.getJson
import okhttp3.Response

internal fun patch(context: Context, uri: Uri, response: Response.Builder): Response.Builder {
	val path = uri.path ?: return response.error404(context)

	FakeServerResponses.takeNextResponse(
		method = "PATCH",
		path = path,
		query = uri.query,
	)?.let { mockResponse ->
		return response.create(
			code = mockResponse.code,
			description = "Mock PATCH $path",
			body = mockResponse.body,
		)
	}

	val handler = patchHandlers.entries.firstOrNull {
		it.key.matches(path)
	}?.value
	return handler?.invoke(context, uri, response) ?: response.error404(context)
}

private typealias PatchHandler = (Context, Uri, Response.Builder) -> Response.Builder

private val patchHandlers: Map<Regex, PatchHandler> = mapOf(
	Regex("^/api/gateway/profile$") to { context, _, response ->
		response.create(
			description = "Update profile",
			body = context.getJson(R.raw.profile_ok),
		)
	},
	Regex("^/api/gateway/profile/password$") to { _, _, response ->
		response.create(description = "Change password")
	},
)
