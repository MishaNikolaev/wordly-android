package com.nmichail.wordly.android.core.validation.email

import com.nmichail.wordly.android.core.validation.DefaultValidationState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ValidateEmailUseCaseTest {

	private val validateEmailUseCase = ValidateEmailUseCase(EmailValidation())

	@ParameterizedTest
	@MethodSource("provide emails")
	fun `invoke validate email use case EXPECT validation item`(email: String, expected: EmailValidationItem) {
		assertEquals(expected, validateEmailUseCase(email))
	}

	private fun `provide emails`(): Stream<Arguments> {
		val lengthInvalidEmail = "@gmail.".padStart(64, 'a').padEnd(321, 'a')
		val lengthBeforeAtInvalidEmail = "@gmail.com".padStart(75, 'a')
		val invalidEmail = "@gmail.com".padStart(20, ';')
		val validEmail = "user@gmail.com"

		return Stream.of(
			Arguments.of("", EmailValidationItem("", DefaultValidationState.Invalid(EmailInvalidState.EMPTY_EMAIL))),
			Arguments.of(
				lengthInvalidEmail,
				EmailValidationItem(lengthInvalidEmail, DefaultValidationState.Invalid(EmailInvalidState.INVALID_LENGTH)),
			),
			Arguments.of(
				invalidEmail,
				EmailValidationItem(invalidEmail, DefaultValidationState.Invalid(EmailInvalidState.INVALID_EMAIL)),
			),
			Arguments.of(
				"user@g.c",
				EmailValidationItem("user@g.c", DefaultValidationState.Invalid(EmailInvalidState.INVALID_EMAIL)),
			),
			Arguments.of(
				"user@gmail.c",
				EmailValidationItem("user@gmail.c", DefaultValidationState.Invalid(EmailInvalidState.INVALID_EMAIL)),
			),
			Arguments.of(
				"user@.com",
				EmailValidationItem("user@.com", DefaultValidationState.Invalid(EmailInvalidState.INVALID_EMAIL)),
			),
			Arguments.of(validEmail, EmailValidationItem(validEmail, DefaultValidationState.Valid)),
			Arguments.of(
				lengthBeforeAtInvalidEmail,
				EmailValidationItem(
					lengthBeforeAtInvalidEmail,
					DefaultValidationState.Invalid(EmailInvalidState.INVALID_LENGTH_BEFORE_AT_SIGN),
				),
			),
		)
	}
}
