package com.nmichail.wordly.android.features.authorization.signup.presentation

import com.nmichail.wordly.android.core.validation.DefaultValidationState
import com.nmichail.wordly.android.core.validation.email.EmailValidationItem
import com.nmichail.wordly.android.core.validation.name.NamePart
import com.nmichail.wordly.android.core.validation.name.NameValidationItem
import com.nmichail.wordly.android.core.validation.notEmpty.NotEmptyValidationItem
import com.nmichail.wordly.android.core.validation.notEmpty.NotEmptyValidationState
import com.nmichail.wordly.android.core.validation.password.PasswordValidationItem
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AreFieldsValidTest {

	@Test
	fun `all fields unverified EXPECT false`() {
		val state = SignUpStore.State.Content(
			email = EmailValidationItem(),
			password = PasswordValidationItem(),
			firstName = NameValidationItem(namePart = NamePart.NAME),
			lastName = NameValidationItem(namePart = NamePart.SURNAME),
			englishLevel = NotEmptyValidationItem(),
			submitting = false,
		)

		Assertions.assertEquals(false, state.areFieldsValid())
	}

	@Test
	fun `all fields valid EXPECT true`() {
		val state = SignUpStore.State.Content(
			email = EmailValidationItem(validationState = DefaultValidationState.Valid),
			password = PasswordValidationItem(validationState = DefaultValidationState.Valid),
			firstName = NameValidationItem(
				namePart = NamePart.NAME,
				validationState = DefaultValidationState.Valid,
			),
			lastName = NameValidationItem(
				namePart = NamePart.SURNAME,
				validationState = DefaultValidationState.Valid,
			),
			englishLevel = NotEmptyValidationItem(validationState = NotEmptyValidationState.Valid),
			submitting = false,
		)

		Assertions.assertEquals(true, state.areFieldsValid())
	}

	@Test
	fun `english level invalid EXPECT false`() {
		val state = SignUpStore.State.Content(
			email = EmailValidationItem(validationState = DefaultValidationState.Valid),
			password = PasswordValidationItem(validationState = DefaultValidationState.Valid),
			firstName = NameValidationItem(
				namePart = NamePart.NAME,
				validationState = DefaultValidationState.Valid,
			),
			lastName = NameValidationItem(
				namePart = NamePart.SURNAME,
				validationState = DefaultValidationState.Valid,
			),
			englishLevel = NotEmptyValidationItem(validationState = NotEmptyValidationState.Invalid),
			submitting = false,
		)

		Assertions.assertEquals(false, state.areFieldsValid())
	}
}
