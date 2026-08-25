package com.nmichail.wordly.android.core.network.okhttp.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor

class LoggingInterceptor : Interceptor {

	private val delegate = HttpLoggingInterceptor { message ->
		Log.d(TAG, message)
	}.apply {
		level = HttpLoggingInterceptor.Level.BODY
	}

	override fun intercept(chain: Interceptor.Chain): Response =
		delegate.intercept(chain)

	private companion object {
		const val TAG = "OkHttp"
	}
}
