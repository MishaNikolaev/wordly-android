package com.nmichail.wordly.android.core.network.datasource

import android.content.Context
import com.nmichail.wordly.android.core.network.domain.entity.Endpoint

private const val ENDPOINT_PREFERENCES = "ENDPOINT_PREFERENCES"
private const val ENDPOINT_NAME = "ENDPOINT_NAME"

class EndpointDataSourceImpl(
    context: Context,
) : EndpointDataSource {

    private val preferences =
        context.getSharedPreferences(ENDPOINT_PREFERENCES, Context.MODE_PRIVATE)

    override fun getEndpoint(): Endpoint {
        val name = preferences.getString(ENDPOINT_NAME, Endpoint.DEV.name) ?: Endpoint.DEV.name
        return try {
            Endpoint.valueOf(name)
        } catch (_: Exception) {
            Endpoint.DEV
        }
    }

    override fun setEndpoint(endpoint: Endpoint) {
        preferences.edit().putString(ENDPOINT_NAME, endpoint.name).apply()
    }

    override fun getEndpoints(): List<Endpoint> = listOf(Endpoint.DEV)
}