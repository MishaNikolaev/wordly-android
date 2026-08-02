package com.nmichail.wordly.android.features.authorization.signup.data.repository

import com.nmichail.wordly.android.core.preferences.data.dto.AuthTokensResponse
import com.nmichail.wordly.android.core.preferences.data.mapper.toEntity
import com.nmichail.wordly.android.features.authorization.signup.data.api.SignUpApi
import com.nmichail.wordly.android.features.authorization.signup.data.mapper.toRequest
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
	private val repository = SignUpRepositoryImpl(signUpApi)

	private val signUpForm = SignUpForm(
		email = "demo@wordly.app",
		password = "12345678",
		firstName = "John",
		lastName = "Doe",
		englishLevel = "B1",
	)
	private val signUpRequest = signUpForm.toRequest()
	private val authTokensResponse = AuthTokensResponse(
		accessToken = "mock-access-token",
		refreshToken = "mock-refresh-token",
	)

	@Test
	fun `sign up EXPECT api method invocation`() = runTest {
		whenever(signUpApi.register(signUpRequest)) doReturn authTokensResponse

		repository.signUp(signUpForm)

		verify(signUpApi).register(signUpRequest)
	}

	@Test
	fun `sign up EXPECT tokens`() = runTest {
		whenever(signUpApi.register(signUpRequest)) doReturn authTokensResponse
		val expected = authTokensResponse.toEntity()

		val actual = repository.signUp(signUpForm)

		assertEquals(expected, actual)
	}
}
