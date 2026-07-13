package com.nmichail.wordly.android.core.preferences.domain.usecase

import com.nmichail.wordly.android.core.preferences.domain.repository.TokenRepository
import javax.inject.Inject

class GetAccessTokenUseCase @Inject constructor(
	tokenRepository: TokenRepository,
) : () -> String? by tokenRepository::getAccessToken
