package com.nmichail.wordly.android.features.authorization.signin.data.repository

import com.nmichail.wordly.android.core.preferences.data.dto.AuthTokensResponse
import com.nmichail.wordly.android.core.preferences.data.mapper.toEntity
import com.nmichail.wordly.android.core.testutils.doThrowSafe
import com.nmichail.wordly.android.features.authorization.signin.data.api.SignInApi
import com.nmichail.wordly.android.features.authorization.signin.data.dto.SignInRequest
import com.nmichail.wordly.android.features.authorization.signin.domain.entity.SignInData
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
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

	@ParameterizedTest
	@MethodSource("provide sign in errors")
	fun `sign in with error EXPECT exception propagated`(
		exception: Exception,
	) = runTest {
		whenever(signInApi.authorize(signInRequest)) doThrowSafe exception

		val actual = runCatching { repository.signIn(signInData) }.exceptionOrNull()

		assertInstanceOf(exception::class.java, actual)
	}

	private fun `provide sign in errors`(): Stream<Arguments> =
		Stream.of(
			Arguments.of(httpException(code = 401)),
			Arguments.of(httpException(code = 500)),
			Arguments.of(IOException("network")),
		)

	private fun httpException(code: Int): HttpException =
		HttpException(
			Response.error<Unit>(
				code,
				"".toResponseBody("application/json".toMediaTypeOrNull()),
			),
		)
}
