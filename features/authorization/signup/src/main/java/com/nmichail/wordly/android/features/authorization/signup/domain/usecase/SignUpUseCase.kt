package com.nmichail.wordly.android.features.authorization.signup.domain.usecase

import com.nmichail.wordly.android.core.preferences.domain.entity.AuthTokens
import com.nmichail.wordly.android.features.authorization.signup.domain.entity.SignUpForm
import com.nmichail.wordly.android.features.authorization.signup.domain.repository.SignUpRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    signUpRepository: SignUpRepository,
) : suspend (SignUpForm) -> AuthTokens by signUpRepository::signUp