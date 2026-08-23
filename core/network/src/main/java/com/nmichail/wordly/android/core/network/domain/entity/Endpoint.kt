package com.nmichail.wordly.android.core.network.domain.entity

enum class Endpoint(
    val url: String,
    val description: String,
) {
	LOCAL(
		url = "http://192.168.0.11:8080/",
		description = "local ktor (LAN host)",
	),
    DEV(
        url = "https://api.wordly.local/",
        description = "dev stand",
    ),
}
