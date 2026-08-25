package com.nmichail.wordly.android.features.authorization.signin.data.repository

import com.nmichail.wordly.android.core.preferences.data.mapper.toEntity
import com.nmichail.wordly.android.core.preferences.domain.entity.AuthTokens
import com.nmichail.wordly.android.core.preferences.domain.usecase.SaveAuthTokensUseCase
import com.nmichail.wordly.android.features.authorization.signin.data.api.SignInApi
import com.nmichail.wordly.android.features.authorization.signin.data.mapper.toRequest
import com.nmichail.wordly.android.features.authorization.signin.domain.entity.SignInData
import com.nmichail.wordly.android.features.authorization.signin.domain.repository.SignInRepository
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(
	private val signInApi: SignInApi,
	private val saveAuthTokensUseCase: SaveAuthTokensUseCase,
) : SignInRepository {

	override suspend fun signIn(signInData: SignInData): AuthTokens {
		val tokens = signInApi.authorize(signInData.toRequest()).toEntity()
		saveAuthTokensUseCase(tokens)
		// TODO(fcm): зарегистрировать FCM-токен устройства после успешного входа
		return tokens
	}
}