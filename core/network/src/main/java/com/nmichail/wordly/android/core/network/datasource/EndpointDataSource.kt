package com.nmichail.wordly.android.core.network.datasource

import com.nmichail.wordly.android.core.network.domain.entity.Endpoint

interface EndpointDataSource {

	fun getEndpoint(): Endpoint

	fun setEndpoint(endpoint: Endpoint)

	fun getEndpoints(): List<Endpoint>
}
