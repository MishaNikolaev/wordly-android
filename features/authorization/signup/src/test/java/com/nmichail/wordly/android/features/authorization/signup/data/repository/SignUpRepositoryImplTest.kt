package com.nmichail.wordly.android.features.authorization.signup.data.repository

import com.nmichail.wordly.android.core.preferences.data.dto.AuthTokensResponse
import com.nmichail.wordly.android.core.preferences.data.mapper.toEntity
import com.nmichail.wordly.android.core.testutils.doThrowSafe
import com.nmichail.wordly.android.features.authorization.signup.data.api.SignUpApi
import com.nmichail.wordly.android.features.authorization.signup.data.mapper.toRequest
import com.nmichail.wordly.android.features.authorization.signup.domain.entity.SignUpForm
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

	@ParameterizedTest
	@MethodSource("provide sign up errors")
	fun `sign up with error EXPECT exception propagated`(
		exception: Exception,
	) = runTest {
		whenever(signUpApi.register(signUpRequest)) doThrowSafe exception

		val actual = runCatching { repository.signUp(signUpForm) }.exceptionOrNull()

		assertInstanceOf(exception::class.java, actual)
	}

	private fun `provide sign up errors`(): Stream<Arguments> =
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
