package com.nmichail.wordly.android.core.fakenetwork

import com.nmichail.wordly.android.core.fakenetwork.models.MockResponse
import java.util.concurrent.ConcurrentHashMap

private const val DEFAULT_HTTP_OK = 200

object FakeServerResponses {

	private val mockResponses = ConcurrentHashMap<String, ResponseSequence>()

	internal fun takeNextResponse(method: String, path: String, query: String?): MockResponse? {
		val key = requestKey(method, path, query)
		val sequence = mockResponses[key] ?: return null
		return sequence.next()
	}

	fun reply(
		method: String,
		path: String,
		code: Int = DEFAULT_HTTP_OK,
		body: String = "",
		query: String? = null,
	) {
		val key = requestKey(method, path, query)
		mockResponses[key] = ResponseSequence(listOf(MockResponse(code, body)))
	}

	fun clear() {
		mockResponses.clear()
	}

	private fun requestKey(method: String, path: String, query: String?): String {
		val normalizedPath = path.trim().removeSuffix("/")
		return if (query != null) {
			"$method $normalizedPath?$query"
		} else {
			"$method $normalizedPath"
		}
	}
}
