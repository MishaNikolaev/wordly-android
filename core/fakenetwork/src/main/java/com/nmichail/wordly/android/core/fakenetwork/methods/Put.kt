package com.nmichail.wordly.android.core.fakenetwork.methods

import android.content.Context
import android.net.Uri
import com.nmichail.wordly.android.core.fakenetwork.FakeServerResponses
import com.nmichail.wordly.android.core.fakenetwork.create
import com.nmichail.wordly.android.core.fakenetwork.error404
import okhttp3.Response

internal fun put(context: Context, uri: Uri, response: Response.Builder): Response.Builder {
	val path = uri.path ?: return response.error404(context)

	FakeServerResponses.takeNextResponse(
		method = "PUT",
		path = path,
		query = uri.query,
	)?.let { mockResponse ->
		return response.create(
			code = mockResponse.code,
			description = "Mock PUT $path",
			body = mockResponse.body,
		)
	}

	val handler = putHandlers.entries.firstOrNull {
		it.key.matches(path)
	}?.value
	return handler?.invoke(context, uri, response) ?: response.error404(context)
}

private typealias PutHandler = (Context, Uri, Response.Builder) -> Response.Builder

private val putHandlers: Map<Regex, PutHandler> = emptyMap()
