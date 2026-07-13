package com.nmichail.wordly.android.core.validation

abstract class ValidationItem<State : ValidationState> {

	abstract val data: String

	abstract val validationState: State
}
