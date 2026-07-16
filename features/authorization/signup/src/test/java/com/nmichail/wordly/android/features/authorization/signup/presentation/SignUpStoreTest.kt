package com.nmichail.wordly.android.features.authorization.signup.presentation

import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.destroy
import com.arkivanov.mvikotlin.extensions.coroutines.labelsChannel
import com.nmichail.wordly.android.core.preferences.domain.entity.AuthTokens
import com.nmichail.wordly.android.core.preferences.domain.usecase.SaveAuthTokensUseCase
import com.nmichail.wordly.android.core.testutils.InstantExecutorExtension
import com.nmichail.wordly.android.core.testutils.TestCoroutineExtension
import com.nmichail.wordly.android.core.testutils.createTestLifecycle
import com.nmichail.wordly.android.core.testutils.doThrowSafe
import com.nmichail.wordly.android.core.validation.DefaultValidationState
import com.nmichail.wordly.android.core.validation.email.EmailInvalidState
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
import com.nmichail.wordly.android.shared.error.NetworkException
import com.nmichail.wordly.android.shared.error.NetworkExceptionConverter
import com.nmichail.wordly.android.shared.error.StatusCodes
import com.nmichail.wordly.android.shared.error.presentation.ErrorDelegate
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.IOException

@ExtendWith(
	MockitoExtension::class,
	TestCoroutineExtension::class,
	InstantExecutorExtension::class,
)
class SignUpStoreTest {

	private val validateEmailUseCase: ValidateEmailUseCase = mock()
	private val validatePasswordUseCase: ValidatePasswordUseCase = mock()
	private val validateNameUseCase: ValidateNameUseCase = mock()
	private val validateNotEmptyUseCase: ValidateNotEmptyUseCase = mock()
	private val signUpUseCase: SignUpUseCase = mock()
	private val saveAuthTokensUseCase: SaveAuthTokensUseCase = mock()
	private val networkExceptionConverter: NetworkExceptionConverter = mock()
	private val errorDelegate: ErrorDelegate = mock()

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
	private val emailInvalidItem = EmailValidationItem(
		email,
		DefaultValidationState.Invalid(EmailInvalidState.EMPTY_EMAIL),
	)
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
	private val exception = IOException("network")
	private val noConnectionError = NetworkException.ErrorMessage(
		statusCode = StatusCodes.NO_CONNECTION,
		messageId = StatusCodes.NO_CONNECTION.statusCode,
		message = "No connection",
	)

	private lateinit var lifecycle: LifecycleRegistry
	private lateinit var store: SignUpStore

	@BeforeEach
	fun setUp() {
		lifecycle = createTestLifecycle()
		store = createStore(
			validateEmailUseCase = validateEmailUseCase,
			validatePasswordUseCase = validatePasswordUseCase,
			validateNameUseCase = validateNameUseCase,
			validateNotEmptyUseCase = validateNotEmptyUseCase,
			signUpUseCase = signUpUseCase,
			saveAuthTokensUseCase = saveAuthTokensUseCase,
			networkExceptionConverter = networkExceptionConverter,
			errorDelegate = errorDelegate,
		)
	}

	@AfterEach
	fun tearDown() {
		lifecycle.destroy()
	}

	@Test
	fun `init EXPECT init state`() {
		assertEquals(SignUpComponent.State(), store.state)
	}

	@Test
	fun `change email EXPECT email in state`() {
		whenever(validateEmailUseCase(email)) doReturn emailValidItem

		store.accept(SignUpStore.Intent.ChangeEmail(email))

		assertEquals(emailValidItem, store.state.email)
	}

	@Test
	fun `navigate to sign in EXPECT open sign in label`() = runTest {
		val labelsChannel = store.labelsChannel(lifecycle)

		store.accept(SignUpStore.Intent.NavigateToSignIn)

		assertEquals(SignUpComponent.Label.OpenSignIn, labelsChannel.receive())
	}

	@Test
	fun `submit with invalid fields EXPECT sign up not invoked`() = runTest {
		whenever(validateEmailUseCase(email)) doReturn emailValidItem
		whenever(validatePasswordUseCase(password)) doReturn passwordValidItem
		whenever(validateNameUseCase(firstName, NamePart.NAME)) doReturn firstNameValidItem
		whenever(validateNameUseCase(lastName, NamePart.SURNAME)) doReturn lastNameValidItem
		whenever(validateNotEmptyUseCase(englishLevel)) doReturn englishLevelValidItem
		store.accept(SignUpStore.Intent.ChangeEmail(email))
		store.accept(SignUpStore.Intent.ChangePassword(password))
		store.accept(SignUpStore.Intent.ChangeFirstName(firstName))
		store.accept(SignUpStore.Intent.ChangeLastName(lastName))
		store.accept(SignUpStore.Intent.ChangeEnglishLevel(englishLevel))
		whenever(validateEmailUseCase(email)) doReturn emailInvalidItem

		store.accept(SignUpStore.Intent.Submit)

		verify(signUpUseCase, never()).invoke(signUpForm)
	}

	@Test
	fun `submit success EXPECT save tokens`() = runTest {
		whenever(validateEmailUseCase(email)) doReturn emailValidItem
		whenever(validatePasswordUseCase(password)) doReturn passwordValidItem
		whenever(validateNameUseCase(firstName, NamePart.NAME)) doReturn firstNameValidItem
		whenever(validateNameUseCase(lastName, NamePart.SURNAME)) doReturn lastNameValidItem
		whenever(validateNotEmptyUseCase(englishLevel)) doReturn englishLevelValidItem
		store.accept(SignUpStore.Intent.ChangeEmail(email))
		store.accept(SignUpStore.Intent.ChangePassword(password))
		store.accept(SignUpStore.Intent.ChangeFirstName(firstName))
		store.accept(SignUpStore.Intent.ChangeLastName(lastName))
		store.accept(SignUpStore.Intent.ChangeEnglishLevel(englishLevel))
		whenever(signUpUseCase(signUpForm)) doReturn tokens

		store.accept(SignUpStore.Intent.Submit)

		verify(saveAuthTokensUseCase).invoke(tokens)
	}

	@Test
	fun `submit success EXPECT open main host label`() = runTest {
		whenever(validateEmailUseCase(email)) doReturn emailValidItem
		whenever(validatePasswordUseCase(password)) doReturn passwordValidItem
		whenever(validateNameUseCase(firstName, NamePart.NAME)) doReturn firstNameValidItem
		whenever(validateNameUseCase(lastName, NamePart.SURNAME)) doReturn lastNameValidItem
		whenever(validateNotEmptyUseCase(englishLevel)) doReturn englishLevelValidItem
		store.accept(SignUpStore.Intent.ChangeEmail(email))
		store.accept(SignUpStore.Intent.ChangePassword(password))
		store.accept(SignUpStore.Intent.ChangeFirstName(firstName))
		store.accept(SignUpStore.Intent.ChangeLastName(lastName))
		store.accept(SignUpStore.Intent.ChangeEnglishLevel(englishLevel))
		whenever(signUpUseCase(signUpForm)) doReturn tokens
		val labelsChannel = store.labelsChannel(lifecycle)

		store.accept(SignUpStore.Intent.Submit)

		assertEquals(SignUpComponent.Label.OpenMainHost, labelsChannel.receive())
	}

	@Test
	fun `submit with registration error EXPECT registration error in state`() = runTest {
		whenever(validateEmailUseCase(email)) doReturn emailValidItem
		whenever(validatePasswordUseCase(password)) doReturn passwordValidItem
		whenever(validateNameUseCase(firstName, NamePart.NAME)) doReturn firstNameValidItem
		whenever(validateNameUseCase(lastName, NamePart.SURNAME)) doReturn lastNameValidItem
		whenever(validateNotEmptyUseCase(englishLevel)) doReturn englishLevelValidItem
		store.accept(SignUpStore.Intent.ChangeEmail(email))
		store.accept(SignUpStore.Intent.ChangePassword(password))
		store.accept(SignUpStore.Intent.ChangeFirstName(firstName))
		store.accept(SignUpStore.Intent.ChangeLastName(lastName))
		store.accept(SignUpStore.Intent.ChangeEnglishLevel(englishLevel))
		whenever(signUpUseCase(signUpForm)) doThrowSafe exception
		whenever(networkExceptionConverter.convert(exception)) doReturn NetworkException.Unknown

		store.accept(SignUpStore.Intent.Submit)

		assertEquals(SignUpComponent.Error.RegistrationFailed, store.state.error)
	}

	@Test
	fun `submit with no connection EXPECT no connection error in state`() = runTest {
		whenever(validateEmailUseCase(email)) doReturn emailValidItem
		whenever(validatePasswordUseCase(password)) doReturn passwordValidItem
		whenever(validateNameUseCase(firstName, NamePart.NAME)) doReturn firstNameValidItem
		whenever(validateNameUseCase(lastName, NamePart.SURNAME)) doReturn lastNameValidItem
		whenever(validateNotEmptyUseCase(englishLevel)) doReturn englishLevelValidItem
		store.accept(SignUpStore.Intent.ChangeEmail(email))
		store.accept(SignUpStore.Intent.ChangePassword(password))
		store.accept(SignUpStore.Intent.ChangeFirstName(firstName))
		store.accept(SignUpStore.Intent.ChangeLastName(lastName))
		store.accept(SignUpStore.Intent.ChangeEnglishLevel(englishLevel))
		whenever(signUpUseCase(signUpForm)) doThrowSafe exception
		whenever(networkExceptionConverter.convert(exception)) doReturn noConnectionError

		store.accept(SignUpStore.Intent.Submit)

		assertEquals(SignUpComponent.Error.NoConnection, store.state.error)
	}
}

private fun createStore(
	validateEmailUseCase: ValidateEmailUseCase,
	validatePasswordUseCase: ValidatePasswordUseCase,
	validateNameUseCase: ValidateNameUseCase,
	validateNotEmptyUseCase: ValidateNotEmptyUseCase,
	signUpUseCase: SignUpUseCase,
	saveAuthTokensUseCase: SaveAuthTokensUseCase,
	networkExceptionConverter: NetworkExceptionConverter,
	errorDelegate: ErrorDelegate,
): SignUpStore =
	SignUpStoreFactory(
		validateEmailUseCase = validateEmailUseCase,
		validatePasswordUseCase = validatePasswordUseCase,
		validateNameUseCase = validateNameUseCase,
		validateNotEmptyUseCase = validateNotEmptyUseCase,
		signUpUseCase = signUpUseCase,
		saveAuthTokensUseCase = saveAuthTokensUseCase,
		networkExceptionConverter = networkExceptionConverter,
		errorDelegate = errorDelegate,
	).create()