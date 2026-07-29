package com.nmichail.wordly.android.core.fakenetwork.methods

import android.content.Context
import android.net.Uri
import com.nmichail.wordly.android.core.fakenetwork.FakeServerResponses
import com.nmichail.wordly.android.core.fakenetwork.create
import com.nmichail.wordly.android.core.fakenetwork.error404
import okhttp3.Response

internal fun delete(context: Context, uri: Uri, response: Response.Builder): Response.Builder {
	val path = uri.path ?: return response.error404(context)

	FakeServerResponses.takeNextResponse(
		method = "DELETE",
		path = path,
		query = uri.query,
	)?.let { mockResponse ->
		return response.create(
			code = mockResponse.code,
			description = "Mock DELETE $path",
			body = mockResponse.body,
		)
	}

	val handler = deleteHandlers.entries.firstOrNull {
		it.key.matches(path)
	}?.value
	return handler?.invoke(context, uri, response) ?: response.error404(context)
}

private typealias DeleteHandler = (Context, Uri, Response.Builder) -> Response.Builder

private val deleteHandlers: Map<Regex, DeleteHandler> = emptyMap()
