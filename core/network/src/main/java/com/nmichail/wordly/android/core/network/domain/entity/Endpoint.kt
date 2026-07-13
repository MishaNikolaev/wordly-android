package com.nmichail.wordly.android.core.network.domain.entity

enum class Endpoint(
	val url: String,
	val description: String,
) {
	DEV(
		url = "https://api.wordly.local/",
		description = "dev stand",
	),
}
