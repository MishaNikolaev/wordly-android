package com.nmichail.wordly.android.core.network.factory

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {

	fun create(
		okHttpClient: OkHttpClient,
		baseUrl: String,
	): Retrofit =
		Retrofit.Builder()
			.baseUrl(baseUrl)
			.client(okHttpClient)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
}
