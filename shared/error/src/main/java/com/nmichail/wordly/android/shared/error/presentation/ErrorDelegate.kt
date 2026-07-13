package com.nmichail.wordly.android.shared.error.presentation

import com.nmichail.wordly.android.core.preferences.domain.usecase.ClearAuthTokensUseCase
import com.nmichail.wordly.android.core.preferences.domain.usecase.IsAuthTokensExistUseCase
import com.nmichail.wordly.android.shared.error.NetworkException
import javax.inject.Inject

interface ErrorDelegate {

	fun handleError(networkException: NetworkException): HandleErrorResult
}

class ErrorDelegateImpl @Inject constructor(
	private val errorLogoutRouter: ErrorLogoutRouter,
	private val clearAuthTokensUseCase: ClearAuthTokensUseCase,
	private val isAuthTokensExistUseCase: IsAuthTokensExistUseCase,
) : ErrorDelegate {

	private companion object {

		const val ACCESS_DENIED_CODE = 103
		const val LEGACY_TOKEN = 102
		const val ENTITY_WAS_NOT_FOUND = 302
	}

	override fun handleError(networkException: NetworkException): HandleErrorResult =
		when (networkException) {
			is NetworkException.ErrorMessage -> {
				when (networkException.messageId) {
					ACCESS_DENIED_CODE -> handleAccessDenied()

					LEGACY_TOKEN -> {
						logout(userBlocked = false)
						HandleErrorResult.HANDLED
					}

					ENTITY_WAS_NOT_FOUND -> {
						logout(userBlocked = false)
						HandleErrorResult.HANDLED
					}

					else -> HandleErrorResult.NOT_HANDLED
				}
			}

			NetworkException.Unknown -> HandleErrorResult.NOT_HANDLED
		}

	private fun handleAccessDenied(): HandleErrorResult {
		if (!isAuthTokensExistUseCase()) {
			return HandleErrorResult.NOT_HANDLED
		}

		logout(userBlocked = true)
		return HandleErrorResult.HANDLED
	}

	private fun logout(userBlocked: Boolean) {
		clearAuthTokensUseCase()
		errorLogoutRouter.navigateToLogoutScreen(userBlocked)
	}
}
