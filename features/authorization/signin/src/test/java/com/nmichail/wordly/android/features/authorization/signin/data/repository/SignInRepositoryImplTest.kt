package com.nmichail.wordly.android.features.authorization.signin.data.repository

import com.nmichail.wordly.android.core.preferences.data.dto.AuthTokensResponse
import com.nmichail.wordly.android.core.preferences.domain.entity.AuthTokens
import com.nmichail.wordly.android.core.preferences.domain.usecase.SaveAuthTokensUseCase
import com.nmichail.wordly.android.features.authorization.signin.data.api.SignInApi
import com.nmichail.wordly.android.features.authorization.signin.data.dto.SignInRequest
import com.nmichail.wordly.android.features.authorization.signin.domain.entity.SignInData
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class SignInRepositoryImplTest {

	private val signInApi: SignInApi = mock()
	private val saveAuthTokensUseCase: SaveAuthTokensUseCase = mock()
	private val repository = SignInRepositoryImpl(
		signInApi = signInApi,
		saveAuthTokensUseCase = saveAuthTokensUseCase,
	)

	private val email = "demo@wordly.app"
	private val password = "12345678"
	private val signInData = SignInData(
		email = email,
		password = password,
	)
	private val tokens = AuthTokens(
		accessToken = "mock-access-token",
		refreshToken = "mock-refresh-token",
	)

	@Test
	fun `sign in EXPECT api authorization and save tokens`() = runTest {
		whenever(signInApi.authorize(SignInRequest(email = email, password = password))) doReturn
			AuthTokensResponse(
				accessToken = tokens.accessToken,
				refreshToken = tokens.refreshToken,
			)

		val actual = repository.signIn(signInData)

		assertEquals(tokens, actual)
		verify(signInApi).authorize(SignInRequest(email = email, password = password))
		verify(saveAuthTokensUseCase).invoke(tokens)
	}
}
