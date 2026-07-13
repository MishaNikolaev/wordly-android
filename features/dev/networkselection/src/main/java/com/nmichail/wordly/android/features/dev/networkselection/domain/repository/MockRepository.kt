package com.nmichail.wordly.android.features.dev.networkselection.domain.repository

interface MockRepository {

	fun setMock(state: Boolean)

	fun isMock(): Boolean
}
