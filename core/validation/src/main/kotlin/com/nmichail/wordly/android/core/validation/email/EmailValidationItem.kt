package com.nmichail.wordly.android.core.validation.email

import com.nmichail.wordly.android.core.validation.DefaultValidationState
import com.nmichail.wordly.android.core.validation.ValidationItem

data class EmailValidationItem(
	override val data: String = "",
	override val validationState: DefaultValidationState = DefaultValidationState.Unverified,
) : ValidationItem<DefaultValidationState>()

fun EmailValidationItem.isValid(): Boolean = validationState is DefaultValidationState.Valid
