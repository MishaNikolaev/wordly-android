package com.nmichail.wordly.android.core.fakenetwork

import android.content.Context
import android.net.Uri
import android.util.Log
import com.nmichail.wordly.android.core.fakenetwork.methods.delete
import com.nmichail.wordly.android.core.fakenetwork.methods.get
import com.nmichail.wordly.android.core.fakenetwork.methods.patch
import com.nmichail.wordly.android.core.fakenetwork.methods.post
import com.nmichail.wordly.android.core.fakenetwork.methods.put
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response

internal const val LOG_TAG = "FakeServer"

internal const val CONTENT_TYPE = "content-type"

internal const val MEDIA_TYPE_JSON = "application/json"

class FakeServerInterceptor(
	private val context: Context,
) : Interceptor {

	override fun intercept(chain: Interceptor.Chain): Response {
		val response = Response.Builder()
			.request(chain.request())
			.protocol(Protocol.HTTP_2)
			.addHeader(CONTENT_TYPE, MEDIA_TYPE_JSON)

		return startFakeResponse(
			context = context,
			url = chain.request().url.toString(),
			response = response,
			chain = chain,
		).build()
	}

	private fun startFakeResponse(
		context: Context,
		url: String,
		response: Response.Builder,
		chain: Interceptor.Chain,
	): Response.Builder {
		Log.d(LOG_TAG, url)
		val uri = Uri.parse(url)
		return when (chain.request().method) {
			"GET" -> get(context, uri, response)
			"POST" -> post(
				context = context,
				uri = uri,
				response = response,
				requestBody = chain.request().body?.let { body ->
					val buffer = okio.Buffer()
					body.writeTo(buffer)
					buffer.readUtf8()
				},
			)
			"PATCH" -> patch(
				context = context,
				uri = uri,
				response = response,
				requestBody = chain.request().body?.let { body ->
					val buffer = okio.Buffer()
					body.writeTo(buffer)
					buffer.readUtf8()
				},
			)
			"PUT" -> put(context, uri, response)
			"DELETE" -> delete(context, uri, response)
			else -> response.error404(context)
		}
	}
}