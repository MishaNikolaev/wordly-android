package com.nmichail.wordly.android.shared.error

sealed class NetworkException {

	data object Unknown : NetworkException()

	data class ErrorMessage(
		val statusCode: StatusCodes,
		val messageId: Int,
		val message: String,
	) : NetworkException()
}

fun NetworkException?.messageIdOrNull(): Int? =
	when (this) {
		is NetworkException.ErrorMessage -> messageId
		NetworkException.Unknown, null -> null
	}
