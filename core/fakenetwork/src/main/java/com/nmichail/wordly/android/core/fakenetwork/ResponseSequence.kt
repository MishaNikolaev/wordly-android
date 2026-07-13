package com.nmichail.wordly.android.core.fakenetwork

import com.nmichail.wordly.android.core.fakenetwork.models.MockResponse

internal class ResponseSequence(
	private val responses: List<MockResponse>,
) {

	private var index = 0

	fun next(): MockResponse? {
		if (responses.isEmpty()) return null

		synchronized(this) {
			val currentIndex = index
			val result = responses[currentIndex]
			if (currentIndex < responses.lastIndex) {
				index = currentIndex + 1
			}
			return result
		}
	}
}
