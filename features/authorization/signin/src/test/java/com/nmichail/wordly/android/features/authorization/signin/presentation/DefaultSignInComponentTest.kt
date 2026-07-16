package com.nmichail.wordly.android.features.authorization.signin.presentation

import com.nmichail.wordly.android.core.preferences.domain.entity.AuthTokens
import com.nmichail.wordly.android.core.preferences.domain.usecase.SaveAuthTokensUseCase
import com.nmichail.wordly.android.core.testutils.InstantExecutorExtension
import com.nmichail.wordly.android.core.testutils.TestCoroutineExtension
import com.nmichail.wordly.android.core.testutils.createTestComponentContext
import com.nmichail.wordly.android.core.testutils.doThrowSafe
import com.nmichail.wordly.android.core.validation.DefaultValidationState
import com.nmichail.wordly.android.core.validation.email.EmailValidationItem
import com.nmichail.wordly.android.core.validation.email.ValidateEmailUseCase
import com.nmichail.wordly.android.core.validation.password.PasswordValidationItem
import com.nmichail.wordly.android.core.validation.password.ValidatePasswordUseCase
import com.nmichail.wordly.android.features.authorization.signin.domain.entity.SignInData
import com.nmichail.wordly.android.features.authorization.signin.domain.usecase.SignInUseCase
import com.nmichail.wordly.android.shared.error.NetworkException
import com.nmichail.wordly.android.shared.error.NetworkExceptionConverter
import com.nmichail.wordly.android.shared.error.StatusCodes
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
import java.io.IOException

@ExtendWith(
	MockitoExtension::class,
	TestCoroutineExtension::class,
	InstantExecutorExtension::class,
)
class DefaultSignInComponentTest {

	private val validateEmailUseCase: ValidateEmailUseCase = mock()
	private val validatePasswordUseCase: ValidatePasswordUseCase = mock()
	private val signInUseCase: SignInUseCase = mock()
	private val saveAuthTokensUseCase: SaveAuthTokensUseCase = mock()
	private val networkExceptionConverter: NetworkExceptionConverter = mock()
	private val errorDelegate: ErrorDelegate = mock()
	private val signInRouter: SignInRouter = mock()

	private val email = "user@gmail.com"
	private val password = "12345678"
	private val signInData = SignInData(email = email, password = password)
	private val emailValidItem = EmailValidationItem(email, DefaultValidationState.Valid)
	private val passwordValidItem = PasswordValidationItem(password, DefaultValidationState.Valid)
	private val tokens = AuthTokens(
		accessToken = "access-token",
		refreshToken = "refresh-token",
	)
	private val exception = IOException("network")
	private val invalidCredentialsError = NetworkException.ErrorMessage(
		statusCode = StatusCodes.NEEDS_AUTHORIZATION,
		messageId = StatusCodes.NEEDS_AUTHORIZATION.statusCode,
		message = "Unauthorized",
	)
	private val noConnectionError = NetworkException.ErrorMessage(
		statusCode = StatusCodes.NO_CONNECTION,
		messageId = StatusCodes.NO_CONNECTION.statusCode,
		message = "No connection",
	)

	private val initState = SignInComponent.State()

	private lateinit var component: DefaultSignInComponent
	private val model get() = component.model.value

	@BeforeEach
	fun setUp() {
		component = DefaultSignInComponent(
			componentContext = createTestComponentContext(),
			signInStoreFactory = SignInStoreFactory(
				validateEmailUseCase = validateEmailUseCase,
				validatePasswordUseCase = validatePasswordUseCase,
				signInUseCase = signInUseCase,
				saveAuthTokensUseCase = saveAuthTokensUseCase,
				networkExceptionConverter = networkExceptionConverter,
				errorDelegate = errorDelegate,
			),
			signInRouter = signInRouter,
		)
	}

	@Test
	fun `init EXPECT init state`() {
		assertEquals(initState, model)
	}

	@Test
	fun `change email EXPECT email in state`() {
		whenever(validateEmailUseCase(email)) doReturn emailValidItem

		component.handleChangeEmail(email)

		assertEquals(emailValidItem, model.email)
	}

	@Test
	fun `change password EXPECT password in state`() {
		whenever(validatePasswordUseCase(password)) doReturn passwordValidItem

		component.handleChangePassword(password)

		assertEquals(passwordValidItem, model.password)
	}

	@Test
	fun `submit success EXPECT save tokens`() = runTest {
		whenever(validateEmailUseCase(email)) doReturn emailValidItem
		whenever(validatePasswordUseCase(password)) doReturn passwordValidItem
		whenever(signInUseCase(signInData)) doReturn tokens
		component.handleChangeEmail(email)
		component.handleChangePassword(password)

		component.handleSubmit()

		verify(saveAuthTokensUseCase).invoke(tokens)
	}

	@Test
	fun `submit success EXPECT open main host`() = runTest {
		whenever(validateEmailUseCase(email)) doReturn emailValidItem
		whenever(validatePasswordUseCase(password)) doReturn passwordValidItem
		whenever(signInUseCase(signInData)) doReturn tokens
		component.handleChangeEmail(email)
		component.handleChangePassword(password)

		component.handleSubmit()

		verify(signInRouter).navigateToMain()
	}

	@Test
	fun `navigate to sign up EXPECT open sign up`() {
		component.handleNavigateToSignUp()

		verify(signInRouter).navigateToSignUp()
	}

	@Test
	fun `navigate to network selection EXPECT open network selection`() {
		component.handleNavigateToNetworkSelection()

		verify(signInRouter).navigateToNetworkSelection()
	}

	@Test
	fun `submit with invalid credentials EXPECT invalid credentials error`() = runTest {
		whenever(validateEmailUseCase(email)) doReturn emailValidItem
		whenever(validatePasswordUseCase(password)) doReturn passwordValidItem
		whenever(signInUseCase(signInData)) doThrowSafe exception
		whenever(networkExceptionConverter.convert(exception)) doReturn invalidCredentialsError
		component.handleChangeEmail(email)
		component.handleChangePassword(password)

		val labelsChannel = component.labelsChannel()
		component.handleSubmit()

		assertEquals(SignInComponent.Label.ShowInvalidCredentials, labelsChannel.receive())
	}

	@Test
	fun `submit with no connection EXPECT no connection error`() = runTest {
		whenever(validateEmailUseCase(email)) doReturn emailValidItem
		whenever(validatePasswordUseCase(password)) doReturn passwordValidItem
		whenever(signInUseCase(signInData)) doThrowSafe exception
		whenever(networkExceptionConverter.convert(exception)) doReturn noConnectionError
		component.handleChangeEmail(email)
		component.handleChangePassword(password)

		val labelsChannel = component.labelsChannel()
		component.handleSubmit()

		assertEquals(SignInComponent.Label.ShowNoConnection, labelsChannel.receive())
	}
}
