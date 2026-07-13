package com.nmichail.wordly.android.shared.error.presentation

import com.nmichail.wordly.android.core.preferences.domain.usecase.ClearAuthTokensUseCase
import com.nmichail.wordly.android.core.preferences.domain.usecase.IsAuthTokensExistUseCase
import com.nmichail.wordly.android.shared.error.NetworkException
import com.nmichail.wordly.android.shared.error.StatusCodes
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class ErrorDelegateImplTest {

	private val errorLogoutRouter: ErrorLogoutRouter = mock()
	private val clearAuthTokensUseCase: ClearAuthTokensUseCase = mock()
	private val isAuthTokensExistUseCase: IsAuthTokensExistUseCase = mock()

	private val errorDelegate = ErrorDelegateImpl(
		errorLogoutRouter = errorLogoutRouter,
		clearAuthTokensUseCase = clearAuthTokensUseCase,
		isAuthTokensExistUseCase = isAuthTokensExistUseCase,
	)

	private val exceptionAccessDenied = NetworkException.ErrorMessage(
		statusCode = StatusCodes.ACCESS_DENIED,
		messageId = 103,
		message = "message",
	)
	private val exceptionLegacyToken = NetworkException.ErrorMessage(
		statusCode = StatusCodes.NEEDS_AUTHORIZATION,
		messageId = 102,
		message = "message",
	)
	private val exceptionEntityWasNotFound = NetworkException.ErrorMessage(
		statusCode = StatusCodes.ENTITY_NOT_FOUND,
		messageId = 302,
		message = "message",
	)

	@Test
	fun `handle error with access denied code and token EXPECT logout blocked`() {
		whenever(isAuthTokensExistUseCase()) doReturn true

		errorDelegate.handleError(exceptionAccessDenied)

		verify(clearAuthTokensUseCase).invoke()
		verify(errorLogoutRouter).navigateToLogoutScreen(userBlocked = true)
	}

	@Test
	fun `handle error with access denied code and token EXPECT handled`() {
		whenever(isAuthTokensExistUseCase()) doReturn true
		val expected = HandleErrorResult.HANDLED

		val actual = errorDelegate.handleError(exceptionAccessDenied)

		assertEquals(expected, actual)
	}

	@Test
	fun `handle error with access denied code without token EXPECT not handled`() {
		whenever(isAuthTokensExistUseCase()) doReturn false
		val expected = HandleErrorResult.NOT_HANDLED

		val actual = errorDelegate.handleError(exceptionAccessDenied)

		assertEquals(expected, actual)
		verify(clearAuthTokensUseCase, never()).invoke()
		verify(errorLogoutRouter, never()).navigateToLogoutScreen(userBlocked = true)
	}

	@Test
	fun `handle legacy token EXPECT logout not blocked and handled`() {
		val expected = HandleErrorResult.HANDLED

		val actual = errorDelegate.handleError(exceptionLegacyToken)

		assertEquals(expected, actual)
		verify(clearAuthTokensUseCase).invoke()
		verify(errorLogoutRouter).navigateToLogoutScreen(userBlocked = false)
	}

	@Test
	fun `handle entity was not found EXPECT logout not blocked and handled`() {
		val expected = HandleErrorResult.HANDLED

		val actual = errorDelegate.handleError(exceptionEntityWasNotFound)

		assertEquals(expected, actual)
		verify(clearAuthTokensUseCase).invoke()
		verify(errorLogoutRouter).navigateToLogoutScreen(userBlocked = false)
	}

	@Test
	fun `handle other error EXPECT not handled`() {
		val exception = NetworkException.ErrorMessage(
			statusCode = StatusCodes.ACCESS_DENIED,
			messageId = 30200,
			message = "s",
		)
		val expected = HandleErrorResult.NOT_HANDLED

		val actual = errorDelegate.handleError(exception)

		assertEquals(expected, actual)
	}

	@Test
	fun `handle unknown EXPECT not handled`() {
		val expected = HandleErrorResult.NOT_HANDLED

		val actual = errorDelegate.handleError(NetworkException.Unknown)

		assertEquals(expected, actual)
	}
}
