package com.nmichail.wordly.android.core.validation.name

internal object NameValidator {

	private const val VALID_CHAR_REGEX_STRING = "^[a-zA-Z\\-]+$"
	private const val BLANK_START_REGEX_STRING = "^[-\\s]+.*"
	private const val BLANK_END_REGEX_STRING = ".*[-\\s]+$"
	private const val SPACE_REGEX_STRING = ".*\\s.*"
	private const val MIN_LENGTH = 2
	private const val MAX_LENGTH = 50

	fun isNameInvalid(name: String?): Boolean = !with(name.orEmpty()) {
		!matches(BLANK_START_REGEX_STRING.toRegex())
			&& !matches(BLANK_END_REGEX_STRING.toRegex())
			&& !matches(SPACE_REGEX_STRING.toRegex())
			&& replace(" ", "")
				.replace("-", "")
				.matches(VALID_CHAR_REGEX_STRING.toRegex())
	}

	fun isNameEmpty(name: String?): Boolean = name.isNullOrEmpty()

	fun isNameMinLengthInvalid(name: String?): Boolean = name.orEmpty().length < MIN_LENGTH

	fun isNameMaxLengthInvalid(name: String?): Boolean = name.orEmpty().length > MAX_LENGTH
}
