package com.nmichail.wordly.android.core.network.datasource

interface MockDataSource {

	fun isMock(): Boolean

	fun setMock(enabled: Boolean)
}
