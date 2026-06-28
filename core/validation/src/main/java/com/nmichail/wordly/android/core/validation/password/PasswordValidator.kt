package com.nmichail.wordly.android.core.validation.password

internal object PasswordValidator {

	private const val MIN_LENGTH = 8
	private const val MAX_LENGTH = 128

	fun isPasswordEmpty(password: String): Boolean = password.isEmpty()

	fun isPasswordMinLengthInvalid(password: String): Boolean = password.length < MIN_LENGTH

	fun isPasswordMaxLengthInvalid(password: String): Boolean = password.length > MAX_LENGTH
}