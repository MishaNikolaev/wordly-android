package com.nmichail.wordly.android.features.dev.networkselection.data.repository

import com.nmichail.wordly.android.core.network.datasource.MockDataSource
import com.nmichail.wordly.android.features.dev.networkselection.domain.repository.MockRepository
import javax.inject.Inject

class MockRepositoryImpl @Inject constructor(
	private val dataSource: MockDataSource,
) : MockRepository {

	override fun setMock(state: Boolean) =
		dataSource.setMock(state)

	override fun isMock(): Boolean =
		dataSource.isMock()
}
