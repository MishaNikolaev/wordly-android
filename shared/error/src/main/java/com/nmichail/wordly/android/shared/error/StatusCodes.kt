package com.nmichail.wordly.android.shared.error

/**
 * Связь бизнес-кода (id в теле ошибки) и HTTP:
 * 102 -> 401, 103 -> 403, 302 -> сущность не найдена (бизнес).
 */
@Suppress("MagicNumber")
enum class StatusCodes(val statusCode: Int) {
	UNKNOWN(0),
	OK(200),
	INTERNAL_SERVER_ERROR(500),
	SERVICE_UNAVAILABLE(503),
	NEEDS_AUTHORIZATION(401),
	ACCESS_DENIED(403),
	AUTHORIZATION_FAILED(400),
	ENTITY_NOT_FOUND(404),
	REFRESH_FAILED(102),
	ACCESS_DENIED_103(103),
	ENTITY_WAS_NOT_FOUND(302),
	NO_CONNECTION(1000),
}
