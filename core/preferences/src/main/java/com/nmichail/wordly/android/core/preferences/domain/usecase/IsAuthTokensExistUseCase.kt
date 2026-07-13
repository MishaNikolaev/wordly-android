package com.nmichail.wordly.android.core.preferences.domain.usecase

import com.nmichail.wordly.android.core.preferences.domain.repository.TokenRepository
import javax.inject.Inject

class IsAuthTokensExistUseCase @Inject constructor(
	tokenRepository: TokenRepository,
) : () -> Boolean by tokenRepository::exists
