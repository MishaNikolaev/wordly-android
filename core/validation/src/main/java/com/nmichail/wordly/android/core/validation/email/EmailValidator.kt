package com.nmichail.wordly.android.core.validation.email

internal object EmailValidator {

	private const val VALID_EMAIL_REGEX_STRING = "^([a-z\\d]\\.?[_-]*)+[a-z\\d]@([a-z\\d]([-*]?[a-z\\d])*\\.)+[a-z\\d]+$"

	private const val AT = '@'

	private const val MAX_EMAIL_LENGTH = 320
	private const val MIN_EMAIL_LENGTH = 3
	private const val MAX_EMAIL_LENGTH_BEFORE_AT = 64
	private const val MIN_EMAIL_LENGTH_BEFORE_AT = 1

	private val validAllLength = MIN_EMAIL_LENGTH..MAX_EMAIL_LENGTH

	private val validBeforeAtLength = MIN_EMAIL_LENGTH_BEFORE_AT..MAX_EMAIL_LENGTH_BEFORE_AT

	fun isEmailEmpty(email: String): Boolean = email.isEmpty()

	fun isEmailInvalid(email: String): Boolean =
		!VALID_EMAIL_REGEX_STRING.toRegex().matches(email.lowercase())

	fun isEmailLengthBeforeAtValid(email: String): Boolean =
		if (email.contains(AT)) {
			email.substringBefore(AT).length in validBeforeAtLength
		} else {
			true
		}

	fun isEmailLengthAllValid(email: String): Boolean = email.length in validAllLength
}