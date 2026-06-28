package com.nmichail.wordly.android.core.validation.password

import com.nmichail.wordly.android.core.validation.DefaultValidationState
import com.nmichail.wordly.android.core.validation.ValidationItem

data class PasswordValidationItem(
	override val data: String = "",
	override val validationState: DefaultValidationState = DefaultValidationState.Unverified,
) : ValidationItem<DefaultValidationState>()

fun PasswordValidationItem.isValid(): Boolean = validationState is DefaultValidationState.Valid