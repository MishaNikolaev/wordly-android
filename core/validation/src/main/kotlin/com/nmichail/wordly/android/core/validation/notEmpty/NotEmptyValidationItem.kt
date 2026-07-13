package com.nmichail.wordly.android.core.validation.notEmpty

import com.nmichail.wordly.android.core.validation.ValidationItem

data class NotEmptyValidationItem(
	override val data: String = "",
	override val validationState: NotEmptyValidationState = NotEmptyValidationState.Unverified,
) : ValidationItem<NotEmptyValidationState>()

fun NotEmptyValidationItem.isValid(): Boolean = validationState is NotEmptyValidationState.Valid
