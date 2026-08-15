package com.nmichail.wordly.android.features.authorization.signup.presentation

import com.nmichail.wordly.android.core.preferences.domain.entity.AuthTokens
import com.nmichail.wordly.android.core.preferences.domain.usecase.SaveAuthTokensUseCase
import com.nmichail.wordly.android.core.testutils.InstantExecutorExtension
import com.nmichail.wordly.android.core.testutils.TestCoroutineExtension
import com.nmichail.wordly.android.core.testutils.createTestComponentContext
import com.nmichail.wordly.android.core.validation.DefaultValidationState
import com.nmichail.wordly.android.core.validation.email.EmailValidationItem
import com.nmichail.wordly.android.core.validation.email.ValidateEmailUseCase
import com.nmichail.wordly.android.core.validation.name.NamePart
import com.nmichail.wordly.android.core.validation.name.NameValidationItem
import com.nmichail.wordly.android.core.validation.name.ValidateNameUseCase
import com.nmichail.wordly.android.core.validation.notEmpty.NotEmptyValidationItem
import com.nmichail.wordly.android.core.validation.notEmpty.NotEmptyValidationState
import com.nmichail.wordly.android.core.validation.notEmpty.ValidateNotEmptyUseCase
import com.nmichail.wordly.android.core.validation.password.PasswordValidationItem
import com.nmichail.wordly.android.core.validation.password.ValidatePasswordUseCase
import com.nmichail.wordly.android.features.authorization.signup.domain.entity.SignUpForm
import com.nmichail.wordly.android.features.authorization.signup.domain.usecase.SignUpUseCase
import com.nmichail.wordly.android.shared.error.NetworkExceptionConverter
import com.nmichail.wordly.android.shared.error.presentation.ErrorDelegate
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(
	MockitoExtension::class,
	TestCoroutineExtension::class,
	InstantExecutorExtension::class,
)
class DefaultSignUpComponentTest {

	private val validateEmailUseCase: ValidateEmailUseCase = mock()
	private val validatePasswordUseCase: ValidatePasswordUseCase = mock()
	private val validateNameUseCase: ValidateNameUseCase = mock()
	private val validateNotEmptyUseCase: ValidateNotEmptyUseCase = mock()
	private val signUpUseCase: SignUpUseCase = mock()
	private val saveAuthTokensUseCase: SaveAuthTokensUseCase = mock()
	private val networkExceptionConverter: NetworkExceptionConverter = mock()
	private val errorDelegate: ErrorDelegate = mock()
	private val signUpRouter: SignUpRouter = mock()

	private val email = "user@gmail.com"
	private val password = "12345678"
	private val firstName = "John"
	private val lastName = "Doe"
	private val englishLevel = "B1"
	private val signUpForm = SignUpForm(
		email = email,
		password = password,
		firstName = firstName,
		lastName = lastName,
		englishLevel = englishLevel,
	)
	private val emailValidItem = EmailValidationItem(email, DefaultValidationState.Valid)
	private val passwordValidItem = PasswordValidationItem(password, DefaultValidationState.Valid)
	private val firstNameValidItem = NameValidationItem(
		data = firstName,
		validationState = DefaultValidationState.Valid,
		namePart = NamePart.NAME,
	)
	private val lastNameValidItem = NameValidationItem(
		data = lastName,
		validationState = DefaultValidationState.Valid,
		namePart = NamePart.SURNAME,
	)
	private val englishLevelValidItem = NotEmptyValidationItem(
		data = englishLevel,
		validationState = NotEmptyValidationState.Valid,
	)
	private val tokens = AuthTokens(
		accessToken = "access-token",
		refreshToken = "refresh-token",
	)

	private lateinit var component: DefaultSignUpComponent
	private val model get() = component.model.value

	@BeforeEach
	fun setUp() {
		component = DefaultSignUpComponent(
			componentContext = createTestComponentContext(),
			signUpStoreFactory = SignUpStoreFactory(
				validateEmailUseCase = validateEmailUseCase,
				validatePasswordUseCase = validatePasswordUseCase,
				validateNameUseCase = validateNameUseCase,
				validateNotEmptyUseCase = validateNotEmptyUseCase,
				signUpUseCase = signUpUseCase,
				saveAuthTokensUseCase = saveAuthTokensUseCase,
				networkExceptionConverter = networkExceptionConverter,
				errorDelegate = errorDelegate,
			),
			signUpRouter = signUpRouter,
		)
	}

	@Test
	fun `init EXPECT init state`() {
		assertEquals(
			SignUpStore.State.Content(
				email = EmailValidationItem(),
				password = PasswordValidationItem(),
				firstName = NameValidationItem(namePart = NamePart.NAME),
				lastName = NameValidationItem(namePart = NamePart.SURNAME),
				englishLevel = NotEmptyValidationItem(),
				submitting = false,
			),
			model,
		)
	}

	@Test
	fun `submit success EXPECT open main host`() = runTest {
		whenever(validateEmailUseCase(email)) doReturn emailValidItem
		whenever(validatePasswordUseCase(password)) doReturn passwordValidItem
		whenever(validateNameUseCase(firstName, NamePart.NAME)) doReturn firstNameValidItem
		whenever(validateNameUseCase(lastName, NamePart.SURNAME)) doReturn lastNameValidItem
		whenever(validateNotEmptyUseCase(englishLevel)) doReturn englishLevelValidItem
		whenever(signUpUseCase(signUpForm)) doReturn tokens
		component.handleChangeEmail(email)
		component.handleChangePassword(password)
		component.handleChangeFirstName(firstName)
		component.handleChangeLastName(lastName)
		component.handleChangeEnglishLevel(englishLevel)

		component.handleSubmit()

		verify(signUpRouter).navigateToMain()
	}

	@Test
	fun `navigate to sign in EXPECT open sign in`() {
		component.handleNavigateToSignIn()

		verify(signUpRouter).navigateToSignIn()
	}

	@Test
	fun `open terms of use EXPECT open terms`() {
		component.handleOpenTermsOfUse()

		verify(signUpRouter).openTermsOfUse()
	}
}
