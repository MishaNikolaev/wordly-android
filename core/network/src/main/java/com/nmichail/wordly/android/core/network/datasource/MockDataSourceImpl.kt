package com.nmichail.wordly.android.core.network.datasource

import android.content.Context

private const val IS_MOCK_PREFERENCES = "IS_MOCK_PREFERENCES"
private const val IS_MOCK_KEY = "IS_MOCK"

class MockDataSourceImpl(
    context: Context,
    private val defaultIsMock: Boolean,
) : MockDataSource {

    private val preferences =
        context.getSharedPreferences(IS_MOCK_PREFERENCES, Context.MODE_PRIVATE)

    override fun isMock(): Boolean =
        preferences.getBoolean(IS_MOCK_KEY, defaultIsMock)

    override fun setMock(enabled: Boolean) {
        preferences.edit().putBoolean(IS_MOCK_KEY, enabled).apply()
    }
}