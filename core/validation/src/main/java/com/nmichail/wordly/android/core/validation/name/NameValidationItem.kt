package com.nmichail.wordly.android.core.validation.name

import com.nmichail.wordly.android.core.validation.DefaultValidationState
import com.nmichail.wordly.android.core.validation.ValidationItem

data class NameValidationItem(
	override val data: String = "",
	override val validationState: DefaultValidationState = DefaultValidationState.Unverified,
	val namePart: NamePart,
) : ValidationItem<DefaultValidationState>()

fun NameValidationItem.isValid(): Boolean = validationState is DefaultValidationState.Valid