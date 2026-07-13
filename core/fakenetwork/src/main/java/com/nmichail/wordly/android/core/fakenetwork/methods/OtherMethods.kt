package com.nmichail.wordly.android.core.fakenetwork.methods

import android.content.Context
import android.net.Uri
import com.nmichail.wordly.android.core.fakenetwork.error404
import okhttp3.Response

internal fun patch(context: Context, uri: Uri, response: Response.Builder): Response.Builder =
	response.error404(context)

internal fun put(context: Context, uri: Uri, response: Response.Builder): Response.Builder =
	response.error404(context)

internal fun delete(context: Context, uri: Uri, response: Response.Builder): Response.Builder =
	response.error404(context)
