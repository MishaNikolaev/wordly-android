package com.nmichail.wordly.android.features.authorization.signin.data.repository

import com.nmichail.wordly.android.core.preferences.data.dto.AuthTokensResponse
import com.nmichail.wordly.android.core.preferences.data.mapper.toEntity
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
	private val repository = SignInRepositoryImpl(signInApi)

	private val email = "demo@wordly.app"
	private val password = "12345678"
	private val signInData = SignInData(
		email = email,
		password = password,
	)
	private val signInRequest = SignInRequest(
		email = email,
		password = password,
	)
	private val authTokensResponse = AuthTokensResponse(
		accessToken = "mock-access-token",
		refreshToken = "mock-refresh-token",
	)

	@Test
	fun `sign in EXPECT api method invocation`() = runTest {
		whenever(signInApi.authorize(signInRequest)) doReturn authTokensResponse

		repository.signIn(signInData)

		verify(signInApi).authorize(signInRequest)
	}

	@Test
	fun `sign in EXPECT tokens`() = runTest {
		whenever(signInApi.authorize(signInRequest)) doReturn authTokensResponse
		val expected = authTokensResponse.toEntity()

		val actual = repository.signIn(signInData)

		assertEquals(expected, actual)
	}
}
