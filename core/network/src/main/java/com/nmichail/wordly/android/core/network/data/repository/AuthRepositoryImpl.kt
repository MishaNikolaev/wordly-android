package com.nmichail.wordly.android.core.network.data.repository

import com.nmichail.wordly.android.core.network.api.AuthApi
import com.nmichail.wordly.android.core.network.data.mapper.toEntity
import com.nmichail.wordly.android.core.network.domain.model.AuthResult
import com.nmichail.wordly.android.core.network.domain.repository.AuthRepository
import com.nmichail.wordly.android.core.network.dto.SignInRequest
import com.nmichail.wordly.android.core.network.dto.SignUpRequest
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
	private val authApi: AuthApi,
) : AuthRepository {

	override suspend fun signIn(email: String, password: String): AuthResult =
		try {
			val tokens = authApi.authorize(
				SignInRequest(
					email = email,
					password = password,
				),
			)
			AuthResult.Success(tokens.toEntity())
		} catch (exception: HttpException) {
			if (exception.code() == HTTP_UNAUTHORIZED) {
				AuthResult.InvalidCredentials
			} else {
				AuthResult.Error
			}
		} catch (_: IOException) {
			AuthResult.Error
		}

	override suspend fun signUp(
		email: String,
		password: String,
		firstName: String,
		lastName: String,
		englishLevel: String,
	): AuthResult =
		try {
			val tokens = authApi.register(
				SignUpRequest(
					email = email,
					password = password,
					firstName = firstName,
					lastName = lastName,
					englishLevel = englishLevel,
				),
			)
			AuthResult.Success(tokens.toEntity())
		} catch (exception: HttpException) {
			if (exception.code() == HTTP_UNAUTHORIZED) {
				AuthResult.InvalidCredentials
			} else {
				AuthResult.Error
			}
		} catch (_: IOException) {
			AuthResult.Error
		}

	private companion object {

		const val HTTP_UNAUTHORIZED = 401
	}
}
