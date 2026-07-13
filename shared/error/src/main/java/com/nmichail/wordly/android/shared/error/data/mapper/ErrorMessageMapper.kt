package com.nmichail.wordly.android.shared.error.data.mapper

import com.nmichail.wordly.android.shared.error.NetworkException
import com.nmichail.wordly.android.shared.error.StatusCodes
import com.nmichail.wordly.android.shared.error.data.model.ErrorMessageModel

internal fun ErrorMessageModel.toEntity(httpCode: Int): NetworkException.ErrorMessage =
	NetworkException.ErrorMessage(
		statusCode = StatusCodes.entries.find { it.statusCode == httpCode } ?: StatusCodes.UNKNOWN,
		messageId = id.takeIf { it != 0 } ?: httpCode,
		message = message.takeIf { it.isNotBlank() }
			?: error.takeIf { it.isNotBlank() }
			?: "No message",
	)
