package com.nmichail.wordly.android.features.dev.networkselection.domain.repository

import com.nmichail.wordly.android.core.network.domain.entity.Endpoint

interface EndpointRepository {

	fun getEndpoints(): List<Endpoint>

	fun getCurrentEndpoint(): Endpoint

	fun setEndpoint(endpoint: Endpoint)
}
