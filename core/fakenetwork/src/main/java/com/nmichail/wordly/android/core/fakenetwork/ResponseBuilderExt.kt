package com.nmichail.wordly.android.core.fakenetwork

import android.content.Context
import android.util.Log
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.net.HttpURLConnection.HTTP_NOT_FOUND
import java.net.HttpURLConnection.HTTP_OK

internal fun Response.Builder.create(
	code: Int = HTTP_OK,
	description: String,
	body: String = "",
): Response.Builder =
	code(code)
		.message(description)
		.body(body.toResponseBody(MEDIA_TYPE_JSON.toMediaTypeOrNull()))

internal fun Response.Builder.error404(context: Context): Response.Builder =
	create(
		code = HTTP_NOT_FOUND,
		description = HTTP_NOT_FOUND.toString(),
		body = context.getString(R.string.fake_network_not_found_body),
	)

internal fun Context.getJson(resId: Int): String =
	resources.openRawResource(resId).bufferedReader().use { it.readText() }
		.also { Log.d(LOG_TAG, "JSON\n $it") }
