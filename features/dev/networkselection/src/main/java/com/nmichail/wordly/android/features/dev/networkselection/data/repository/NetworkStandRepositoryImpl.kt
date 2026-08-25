package com.nmichail.wordly.android.features.dev.networkselection.data.repository

import com.nmichail.wordly.android.core.network.datasource.EndpointDataSource
import com.nmichail.wordly.android.core.network.datasource.MockDataSource
import com.nmichail.wordly.android.core.network.domain.entity.Endpoint
import com.nmichail.wordly.android.features.dev.networkselection.domain.entity.NetworkStand
import com.nmichail.wordly.android.features.dev.networkselection.domain.repository.NetworkStandRepository
import javax.inject.Inject

class NetworkStandRepositoryImpl @Inject constructor(
	private val endpointDataSource: EndpointDataSource,
	private val mockDataSource: MockDataSource,
) : NetworkStandRepository {

	override fun getStands(): List<NetworkStand> =
		NetworkStand.entries

	override fun getSelected(): NetworkStand =
		if (mockDataSource.isMock()) {
			NetworkStand.MOCK
		} else {
			when (endpointDataSource.getEndpoint()) {
				Endpoint.LOCAL -> NetworkStand.LOCAL
				Endpoint.DEV -> NetworkStand.DEV
			}
		}

	override fun setSelected(stand: NetworkStand) {
		when (stand) {
			NetworkStand.LOCAL -> {
				mockDataSource.setMock(false)
				endpointDataSource.setEndpoint(Endpoint.LOCAL)
			}

			NetworkStand.DEV -> {
				mockDataSource.setMock(false)
				endpointDataSource.setEndpoint(Endpoint.DEV)
			}

			NetworkStand.MOCK -> {
				mockDataSource.setMock(true)
			}
		}
	}
}
