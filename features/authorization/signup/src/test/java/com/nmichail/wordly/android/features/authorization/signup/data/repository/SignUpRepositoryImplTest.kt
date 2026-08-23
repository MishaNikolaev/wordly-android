package com.nmichail.wordly.android.features.authorization.signup.data.repository

import com.nmichail.wordly.android.core.preferences.data.dto.AuthTokensResponse
import com.nmichail.wordly.android.core.preferences.domain.entity.AuthTokens
import com.nmichail.wordly.android.core.preferences.domain.usecase.SaveAuthTokensUseCase
import com.nmichail.wordly.android.features.authorization.signup.data.api.SignUpApi
import com.nmichail.wordly.android.features.authorization.signup.data.dto.SignUpRequest
import com.nmichail.wordly.android.features.authorization.signup.domain.entity.SignUpForm
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SignUpRepositoryImplTest {

	private val signUpApi: SignUpApi = mock()
	private val saveAuthTokensUseCase: SaveAuthTokensUseCase = mock()
	private val repository = SignUpRepositoryImpl(
		signUpApi = signUpApi,
		saveAuthTokensUseCase = saveAuthTokensUseCase,
	)

	private val signUpForm = SignUpForm(
		email = "demo@wordly.app",
		password = "12345678",
		firstName = "John",
		lastName = "Doe",
		englishLevel = "B1",
	)
	private val signUpRequest = SignUpRequest(
		email = signUpForm.email,
		password = signUpForm.password,
		firstName = signUpForm.firstName,
		lastName = signUpForm.lastName,
		englishLevel = signUpForm.englishLevel,
	)
	private val tokens = AuthTokens(
		accessToken = "mock-access-token",
		refreshToken = "mock-refresh-token",
	)

	@Test
	fun `sign up EXPECT api registration and save tokens`() = runTest {
		whenever(signUpApi.register(signUpRequest)) doReturn AuthTokensResponse(
			accessToken = tokens.accessToken,
			refreshToken = tokens.refreshToken,
		)

		val actual = repository.signUp(signUpForm)

		assertEquals(tokens, actual)
		verify(signUpApi).register(signUpRequest)
		verify(saveAuthTokensUseCase).invoke(tokens)
	}
}
