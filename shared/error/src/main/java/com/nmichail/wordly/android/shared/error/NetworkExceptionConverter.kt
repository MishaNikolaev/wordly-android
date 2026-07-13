package com.nmichail.wordly.android.shared.error

import com.google.gson.Gson
import com.nmichail.wordly.android.shared.error.data.mapper.toEntity
import com.nmichail.wordly.android.shared.error.data.model.ErrorMessageModel
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NetworkExceptionConverter @Inject constructor(
	private val gson: Gson,
) {

	fun convert(exception: Exception): NetworkException =
		when (exception) {
			is HttpException -> exception.getHttpErrorMessage()
			is IOException -> getNoConnectionErrorMessage()
			else -> NetworkException.Unknown
		}

	private fun HttpException.getHttpErrorMessage(): NetworkException =
		response()?.errorBody()?.string()
			.takeIf { !it.isNullOrBlank() }
			?.let { body ->
				runCatching { gson.fromJson(body, ErrorMessageModel::class.java) }.getOrNull()
			}
			?.toEntity(code())
			?: getErrorWithoutBody(code())

	private fun getErrorWithoutBody(statusCode: Int): NetworkException.ErrorMessage =
		NetworkException.ErrorMessage(
			statusCode = StatusCodes.entries.find { it.statusCode == statusCode } ?: StatusCodes.UNKNOWN,
			messageId = statusCode,
			message = "No message",
		)

	private fun getNoConnectionErrorMessage(): NetworkException =
		NetworkException.ErrorMessage(
			statusCode = StatusCodes.NO_CONNECTION,
			messageId = StatusCodes.NO_CONNECTION.statusCode,
			message = "No connection",
		)
}
