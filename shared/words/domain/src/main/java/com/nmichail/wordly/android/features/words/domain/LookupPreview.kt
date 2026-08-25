package com.nmichail.wordly.android.features.words.domain

private const val PREVIEW_LIMIT = 2
private const val ELLIPSIS = "…"

fun previewDefinition(definition: String?, maxSenses: Int = PREVIEW_LIMIT): String? {
	val parts = definition
		?.split(';')
		?.map { it.trim() }
		?.filter { it.isNotEmpty() }
		.orEmpty()
	if (parts.isEmpty()) return null
	if (parts.size <= maxSenses) return parts.joinToString(separator = "; ")
	return parts.take(maxSenses).joinToString(separator = "; ") + ELLIPSIS
}

fun <T> List<T>.previewItems(max: Int = PREVIEW_LIMIT): Pair<List<T>, Boolean> {
	if (size <= max) return this to false
	return take(max) to true
}
