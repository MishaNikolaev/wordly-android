package com.nmichail.wordly.android.core.network.data.repository

import com.nmichail.wordly.android.core.network.api.AuthApi
import com.nmichail.wordly.android.core.network.data.mapper.toEntity
import com.nmichail.wordly.android.core.network.domain.model.AuthResult
import com.nmichail.wordly.android.core.network.dto.AuthTokensResponse
import com.nmichail.wordly.android.core.network.dto.SignInRequest
import com.nmichail.wordly.android.core.network.dto.SignUpRequest
import com.nmichail.wordly.android.core.testutils.doThrowSafe
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AuthRepositoryImplTest {

	private val authApi: AuthApi = mock()
	private val repository = AuthRepositoryImpl(authApi)

	private val email = "demo@wordly.app"
	private val password = "12345678"
	private val firstName = "John"
	private val lastName = "Doe"
	private val englishLevel = "B1"

	private val signInRequest = SignInRequest(
		email = email,
		password = password,
	)
	private val signUpRequest = SignUpRequest(
		email = email,
		password = password,
		firstName = firstName,
		lastName = lastName,
		englishLevel = englishLevel,
	)
	private val authTokensResponse = AuthTokensResponse(
		accessToken = "mock-access-token",
		refreshToken = "mock-refresh-token",
	)

	@Test
	fun `sign in EXPECT api method invocation`() = runTest {
		whenever(authApi.authorize(signInRequest)) doReturn authTokensResponse

		repository.signIn(email = email, password = password)

		verify(authApi).authorize(signInRequest)
	}

	@Test
	fun `sign in EXPECT success with tokens`() = runTest {
		whenever(authApi.authorize(signInRequest)) doReturn authTokensResponse
		val expected = AuthResult.Success(authTokensResponse.toEntity())

		val actual = repository.signIn(email = email, password = password)

		assertEquals(expected, actual)
	}

	@ParameterizedTest
	@MethodSource("provide sign in errors")
	fun `sign in with error EXPECT mapped auth result`(
		exception: Exception,
		expected: AuthResult,
	) = runTest {
		whenever(authApi.authorize(signInRequest)) doThrowSafe exception

		val actual = repository.signIn(email = email, password = password)

		assertEquals(expected, actual)
	}

	@Test
	fun `sign up EXPECT api method invocation`() = runTest {
		whenever(authApi.register(signUpRequest)) doReturn authTokensResponse

		repository.signUp(
			email = email,
			password = password,
			firstName = firstName,
			lastName = lastName,
			englishLevel = englishLevel,
		)

		verify(authApi).register(signUpRequest)
	}

	@Test
	fun `sign up EXPECT success with tokens`() = runTest {
		whenever(authApi.register(signUpRequest)) doReturn authTokensResponse
		val expected = AuthResult.Success(authTokensResponse.toEntity())

		val actual = repository.signUp(
			email = email,
			password = password,
			firstName = firstName,
			lastName = lastName,
			englishLevel = englishLevel,
		)

		assertEquals(expected, actual)
	}

	@ParameterizedTest
	@MethodSource("provide sign up errors")
	fun `sign up with error EXPECT mapped auth result`(
		exception: Exception,
		expected: AuthResult,
	) = runTest {
		whenever(authApi.register(signUpRequest)) doThrowSafe exception

		val actual = repository.signUp(
			email = email,
			password = password,
			firstName = firstName,
			lastName = lastName,
			englishLevel = englishLevel,
		)

		assertEquals(expected, actual)
	}

	private fun `provide sign in errors`(): Stream<Arguments> =
		errorCases()

	private fun `provide sign up errors`(): Stream<Arguments> =
		errorCases()

	private fun errorCases(): Stream<Arguments> =
		Stream.of(
			Arguments.of(httpException(code = 401), AuthResult.InvalidCredentials),
			Arguments.of(httpException(code = 500), AuthResult.Error),
			Arguments.of(IOException("network"), AuthResult.Error),
		)

	private fun httpException(code: Int): HttpException =
		HttpException(
			Response.error<Unit>(
				code,
				"".toResponseBody("application/json".toMediaTypeOrNull()),
			),
		)
}
