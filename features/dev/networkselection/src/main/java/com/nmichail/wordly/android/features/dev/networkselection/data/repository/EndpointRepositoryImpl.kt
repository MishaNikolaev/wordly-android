package com.nmichail.wordly.android.features.dev.networkselection.data.repository

import com.nmichail.wordly.android.core.network.datasource.EndpointDataSource
import com.nmichail.wordly.android.core.network.domain.entity.Endpoint
import com.nmichail.wordly.android.features.dev.networkselection.domain.repository.EndpointRepository
import javax.inject.Inject

class EndpointRepositoryImpl @Inject constructor(
	private val dataSource: EndpointDataSource,
) : EndpointRepository {

	override fun getEndpoints(): List<Endpoint> =
		dataSource.getEndpoints()

	override fun getCurrentEndpoint(): Endpoint =
		dataSource.getEndpoint()

	override fun setEndpoint(endpoint: Endpoint) =
		dataSource.setEndpoint(endpoint)
}
