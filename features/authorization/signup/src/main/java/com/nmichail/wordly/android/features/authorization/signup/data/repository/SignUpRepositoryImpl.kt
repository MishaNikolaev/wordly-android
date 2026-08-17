package com.nmichail.wordly.android.features.authorization.signup.data.repository

import com.nmichail.wordly.android.core.preferences.data.mapper.toEntity
import com.nmichail.wordly.android.core.preferences.domain.entity.AuthTokens
import com.nmichail.wordly.android.features.authorization.signup.data.api.SignUpApi
import com.nmichail.wordly.android.features.authorization.signup.data.mapper.toRequest
import com.nmichail.wordly.android.features.authorization.signup.domain.entity.SignUpForm
import com.nmichail.wordly.android.features.authorization.signup.domain.repository.SignUpRepository
import javax.inject.Inject

class SignUpRepositoryImpl @Inject constructor(
    private val signUpApi: SignUpApi,
) : SignUpRepository {

    override suspend fun signUp(form: SignUpForm): AuthTokens =
        signUpApi.register(form.toRequest()).toEntity()
}