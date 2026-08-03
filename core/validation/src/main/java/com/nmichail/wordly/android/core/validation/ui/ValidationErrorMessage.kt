package com.nmichail.wordly.android.core.validation.ui

import com.nmichail.wordly.android.core.validation.DefaultValidationState
import com.nmichail.wordly.android.core.validation.R
import com.nmichail.wordly.android.core.validation.email.EmailInvalidState
import com.nmichail.wordly.android.core.validation.name.NameInvalidState
import com.nmichail.wordly.android.core.validation.name.NamePart
import com.nmichail.wordly.android.core.validation.notEmpty.NotEmptyValidationState
import com.nmichail.wordly.android.core.validation.password.PasswordInvalidState

fun emailErrorMessage(state: DefaultValidationState): Int {
	if (state !is DefaultValidationState.Invalid) {
		return R.string.empty
	}

	return when (state.invalidState) {
		EmailInvalidState.EMPTY_EMAIL -> R.string.validation_error_empty_email
		EmailInvalidState.INVALID_LENGTH_BEFORE_AT_SIGN,
		EmailInvalidState.INVALID_LENGTH,
		EmailInvalidState.INVALID_EMAIL,
		-> R.string.validation_error_invalid_email
		else -> R.string.empty
	}
}

fun passwordErrorMessage(state: DefaultValidationState): Int {
	if (state !is DefaultValidationState.Invalid) {
		return R.string.empty
	}

	return when (state.invalidState) {
		PasswordInvalidState.EMPTY_PASSWORD -> R.string.validation_error_empty_password
		PasswordInvalidState.INVALID_MIN_LENGTH -> R.string.validation_error_invalid_min_length_password
		PasswordInvalidState.INVALID_MAX_LENGTH -> R.string.validation_error_invalid_max_length_password
		else -> R.string.empty
	}
}

fun nameErrorMessage(state: DefaultValidationState, namePart: NamePart): Int {
	if (state !is DefaultValidationState.Invalid) {
		return R.string.empty
	}

	return when (state.invalidState) {
		NameInvalidState.EMPTY_NAME -> nameEmptyErrorMessage(namePart)
		NameInvalidState.INVALID_MIN_LENGTH -> nameShortErrorMessage(namePart)
		NameInvalidState.INVALID_MAX_LENGTH -> nameLongErrorMessage(namePart)
		NameInvalidState.INVALID_NAME -> R.string.validation_error_invalid_name
		else -> R.string.empty
	}
}

private fun nameEmptyErrorMessage(namePart: NamePart): Int =
	when (namePart) {
		NamePart.NAME -> R.string.validation_error_empty_first_name
		NamePart.SURNAME -> R.string.validation_error_empty_last_name
	}

private fun nameShortErrorMessage(namePart: NamePart): Int =
	when (namePart) {
		NamePart.NAME -> R.string.validation_error_short_first_name
		NamePart.SURNAME -> R.string.validation_error_short_last_name
	}

private fun nameLongErrorMessage(namePart: NamePart): Int =
	when (namePart) {
		NamePart.NAME -> R.string.validation_error_long_first_name
		NamePart.SURNAME -> R.string.validation_error_long_last_name
	}

fun notEmptyErrorMessage(state: NotEmptyValidationState): Int =
	if (state is NotEmptyValidationState.Invalid) {
		R.string.validation_error_empty
	} else {
		R.string.empty
	}
