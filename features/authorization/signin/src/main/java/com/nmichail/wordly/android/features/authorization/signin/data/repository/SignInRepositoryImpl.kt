package com.nmichail.wordly.android.features.authorization.signin.data.repository

import com.nmichail.wordly.android.core.preferences.data.mapper.toEntity
import com.nmichail.wordly.android.core.preferences.domain.entity.AuthTokens
import com.nmichail.wordly.android.features.authorization.signin.data.api.SignInApi
import com.nmichail.wordly.android.features.authorization.signin.data.mapper.toRequest
import com.nmichail.wordly.android.features.authorization.signin.domain.entity.SignInData
import com.nmichail.wordly.android.features.authorization.signin.domain.repository.SignInRepository
import javax.inject.Inject

class SignInRepositoryImpl @Inject constructor(
	private val signInApi: SignInApi,
) : SignInRepository {

	override suspend fun signIn(signInData: SignInData): AuthTokens =
		signInApi.authorize(signInData.toRequest()).toEntity()
}
