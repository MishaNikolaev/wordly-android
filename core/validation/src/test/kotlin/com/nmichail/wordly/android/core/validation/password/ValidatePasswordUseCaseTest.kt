package com.nmichail.wordly.android.core.validation.password

import com.nmichail.wordly.android.core.validation.DefaultValidationState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ValidatePasswordUseCaseTest {

	private val validatePasswordUseCase = ValidatePasswordUseCase(PasswordValidation())

	@ParameterizedTest
	@MethodSource("provide passwords")
	fun `invoke validate password use case EXPECT validation item`(password: String, expected: PasswordValidationItem) {
		assertEquals(expected, validatePasswordUseCase(password))
	}

	private fun `provide passwords`(): Stream<Arguments> {
		val overMaxLengthPassword = "a".repeat(129)

		return Stream.of(
			Arguments.of(
				"",
				PasswordValidationItem(
					data = "",
					validationState = DefaultValidationState.Invalid(PasswordInvalidState.EMPTY_PASSWORD),
				),
			),
			Arguments.of(
				"1234567",
				PasswordValidationItem(
					data = "1234567",
					validationState = DefaultValidationState.Invalid(PasswordInvalidState.INVALID_MIN_LENGTH),
				),
			),
			Arguments.of(
				overMaxLengthPassword,
				PasswordValidationItem(
					data = overMaxLengthPassword,
					validationState = DefaultValidationState.Invalid(PasswordInvalidState.INVALID_MAX_LENGTH),
				),
			),
			Arguments.of(
				"password123",
				PasswordValidationItem(
					data = "password123",
					validationState = DefaultValidationState.Valid,
				),
			),
		)
	}
}
