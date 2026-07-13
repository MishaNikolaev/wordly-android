package com.nmichail.wordly.android.features.dev.networkselection.presentation

import com.nmichail.wordly.android.core.network.domain.entity.Endpoint

data class NetworkSelectionState(
	val endpoints: List<Endpoint> = emptyList(),
	val currentEndpoint: Endpoint = Endpoint.DEV,
	val mockState: Boolean = false,
)
