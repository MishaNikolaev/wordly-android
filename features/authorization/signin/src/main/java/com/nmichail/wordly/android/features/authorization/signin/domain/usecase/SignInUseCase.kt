package com.nmichail.wordly.android.features.authorization.signin.domain.usecase

import com.nmichail.wordly.android.core.preferences.domain.entity.AuthTokens
import com.nmichail.wordly.android.features.authorization.signin.domain.entity.SignInData
import com.nmichail.wordly.android.features.authorization.signin.domain.repository.SignInRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    signInRepository: SignInRepository,
) : suspend (SignInData) -> AuthTokens by signInRepository::signIn