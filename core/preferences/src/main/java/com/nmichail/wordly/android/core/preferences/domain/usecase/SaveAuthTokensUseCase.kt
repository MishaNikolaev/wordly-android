package com.nmichail.wordly.android.core.preferences.domain.usecase

import com.nmichail.wordly.android.core.preferences.domain.entity.AuthTokens
import com.nmichail.wordly.android.core.preferences.domain.repository.TokenRepository
import javax.inject.Inject

class SaveAuthTokensUseCase @Inject constructor(
	tokenRepository: TokenRepository,
) : (AuthTokens) -> Unit by tokenRepository::save
