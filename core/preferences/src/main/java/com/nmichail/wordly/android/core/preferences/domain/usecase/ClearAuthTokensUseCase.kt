package com.nmichail.wordly.android.core.preferences.domain.usecase

import com.nmichail.wordly.android.core.preferences.domain.repository.TokenRepository
import javax.inject.Inject

class ClearAuthTokensUseCase @Inject constructor(
	tokenRepository: TokenRepository,
) : () -> Unit by tokenRepository::clear
