package com.nmichail.wordly.android.features.authorization.signin.presentation

import com.nmichail.wordly.android.core.validation.DefaultValidationState
import com.nmichail.wordly.android.core.validation.email.EmailValidationItem
import com.nmichail.wordly.android.core.validation.password.PasswordValidationItem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AreFieldsValidTest {

	@Test
	fun `all fields unverified EXPECT false`() {
		val state = SignInComponent.State()

		assertEquals(false, state.areFieldsValid())
	}

	@Test
	fun `only email valid EXPECT false`() {
		val state = SignInComponent.State(
			email = EmailValidationItem(validationState = DefaultValidationState.Valid),
		)

		assertEquals(false, state.areFieldsValid())
	}

	@Test
	fun `all fields valid EXPECT true`() {
		val state = SignInComponent.State(
			email = EmailValidationItem(validationState = DefaultValidationState.Valid),
			password = PasswordValidationItem(validationState = DefaultValidationState.Valid),
		)

		assertEquals(true, state.areFieldsValid())
	}
}
