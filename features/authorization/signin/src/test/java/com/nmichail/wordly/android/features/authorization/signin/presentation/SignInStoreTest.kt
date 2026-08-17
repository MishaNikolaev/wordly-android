package com.nmichail.wordly.android.features.authorization.signin.presentation

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
import com.nmichail.wordly.android.core.validation.password.PasswordValidationItem
import com.nmichail.wordly.android.core.validation.password.ValidatePasswordUseCase
import com.nmichail.wordly.android.features.authorization.signin.domain.entity.SignInData
import com.nmichail.wordly.android.features.authorization.signin.domain.usecase.SignInUseCase
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
class SignInStoreTest {

    private val validateEmailUseCase: ValidateEmailUseCase = mock()
    private val validatePasswordUseCase: ValidatePasswordUseCase = mock()
    private val signInUseCase: SignInUseCase = mock()
    private val saveAuthTokensUseCase: SaveAuthTokensUseCase = mock()
    private val networkExceptionConverter: NetworkExceptionConverter = mock()
    private val errorDelegate: ErrorDelegate = mock()

    private val email = "user@gmail.com"
    private val password = "12345678"
    private val signInData = SignInData(email = email, password = password)
    private val emailValidItem = EmailValidationItem(email, DefaultValidationState.Valid)
    private val passwordValidItem = PasswordValidationItem(password, DefaultValidationState.Valid)
    private val emailInvalidItem = EmailValidationItem(
        email,
        DefaultValidationState.Invalid(EmailInvalidState.EMPTY_EMAIL),
    )
    private val tokens = AuthTokens(
        accessToken = "access-token",
        refreshToken = "refresh-token",
    )
    private val exception = IOException("network")
    private val networkError = NetworkException.ErrorMessage(
        statusCode = StatusCodes.NEEDS_AUTHORIZATION,
        messageId = StatusCodes.NEEDS_AUTHORIZATION.statusCode,
        message = "Unauthorized",
    )

    private lateinit var lifecycle: LifecycleRegistry
    private lateinit var store: SignInStore

    private val content get() = store.state as SignInStore.State.Content

    @BeforeEach
    fun setUp() {
        lifecycle = createTestLifecycle()
        store = createStore()
    }

    @AfterEach
    fun tearDown() {
        lifecycle.destroy()
    }

    @Test
    fun `init EXPECT init state`() {
        assertEquals(
            SignInStore.State.Content(
                email = EmailValidationItem(),
                password = PasswordValidationItem(),
                submitting = false,
            ),
            store.state,
        )
    }

    @Test
    fun `change email EXPECT email in state`() {
        whenever(validateEmailUseCase(email)) doReturn emailValidItem

        store.accept(SignInStore.Intent.ChangeEmail(email))

        assertEquals(emailValidItem, content.email)
    }

    @Test
    fun `change password EXPECT password in state`() {
        whenever(validatePasswordUseCase(password)) doReturn passwordValidItem

        store.accept(SignInStore.Intent.ChangePassword(password))

        assertEquals(passwordValidItem, content.password)
    }

    @Test
    fun `navigate to sign up EXPECT open sign up label`() = runTest {
        val labelsChannel = store.labelsChannel(lifecycle)

        store.accept(SignInStore.Intent.NavigateToSignUp)

        assertEquals(SignInStore.Label.OpenSignUp, labelsChannel.receive())
    }

    @Test
    fun `submit with invalid fields EXPECT sign in not invoked`() = runTest {
        whenever(validateEmailUseCase(email)) doReturn emailInvalidItem
        whenever(validatePasswordUseCase(password)) doReturn passwordValidItem
        store.accept(SignInStore.Intent.ChangeEmail(email))
        store.accept(SignInStore.Intent.ChangePassword(password))

        store.accept(SignInStore.Intent.Submit)

        verify(signInUseCase, never()).invoke(signInData)
    }

    @Test
    fun `submit success EXPECT save tokens`() = runTest {
        whenever(validateEmailUseCase(email)) doReturn emailValidItem
        whenever(validatePasswordUseCase(password)) doReturn passwordValidItem
        whenever(signInUseCase(signInData)) doReturn tokens
        store.accept(SignInStore.Intent.ChangeEmail(email))
        store.accept(SignInStore.Intent.ChangePassword(password))

        store.accept(SignInStore.Intent.Submit)

        verify(saveAuthTokensUseCase).invoke(tokens)
    }

    @Test
    fun `submit success EXPECT open main host label`() = runTest {
        whenever(validateEmailUseCase(email)) doReturn emailValidItem
        whenever(validatePasswordUseCase(password)) doReturn passwordValidItem
        whenever(signInUseCase(signInData)) doReturn tokens
        store.accept(SignInStore.Intent.ChangeEmail(email))
        store.accept(SignInStore.Intent.ChangePassword(password))
        val labelsChannel = store.labelsChannel(lifecycle)

        store.accept(SignInStore.Intent.Submit)

        assertEquals(SignInStore.Label.OpenMainHost, labelsChannel.receive())
    }

    @Test
    fun `submit with error EXPECT error state`() = runTest {
        whenever(validateEmailUseCase(email)) doReturn emailValidItem
        whenever(validatePasswordUseCase(password)) doReturn passwordValidItem
        whenever(signInUseCase(signInData)) doThrowSafe exception
        whenever(networkExceptionConverter.convert(exception)) doReturn networkError
        store.accept(SignInStore.Intent.ChangeEmail(email))
        store.accept(SignInStore.Intent.ChangePassword(password))

        store.accept(SignInStore.Intent.Submit)

        assertEquals(
            SignInStore.State.Error(
                content = SignInStore.State.Content(
                    email = emailValidItem,
                    password = passwordValidItem,
                    submitting = false,
                ),
            ),
            store.state,
        )
    }

    @Test
    fun `retry after error EXPECT content restored`() = runTest {
        whenever(validateEmailUseCase(email)) doReturn emailValidItem
        whenever(validatePasswordUseCase(password)) doReturn passwordValidItem
        whenever(signInUseCase(signInData)) doThrowSafe exception
        whenever(networkExceptionConverter.convert(exception)) doReturn networkError
        store.accept(SignInStore.Intent.ChangeEmail(email))
        store.accept(SignInStore.Intent.ChangePassword(password))
        store.accept(SignInStore.Intent.Submit)

        store.accept(SignInStore.Intent.Retry)

        assertEquals(
            SignInStore.State.Content(
                email = emailValidItem,
                password = passwordValidItem,
                submitting = false,
            ),
            store.state,
        )
    }

    private fun createStore(): SignInStore =
        SignInStoreFactory(
            validateEmailUseCase = validateEmailUseCase,
            validatePasswordUseCase = validatePasswordUseCase,
            signInUseCase = signInUseCase,
            saveAuthTokensUseCase = saveAuthTokensUseCase,
            networkExceptionConverter = networkExceptionConverter,
            errorDelegate = errorDelegate,
        ).create()
}