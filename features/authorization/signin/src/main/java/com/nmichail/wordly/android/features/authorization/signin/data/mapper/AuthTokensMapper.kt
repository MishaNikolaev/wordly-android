package com.nmichail.wordly.android.features.authorization.signin.data.mapper

import com.nmichail.wordly.android.core.preferences.domain.entity.AuthTokens
import com.nmichail.wordly.android.features.authorization.signin.data.dto.AuthTokensResponse

fun AuthTokensResponse.toEntity(): AuthTokens =
	AuthTokens(
		accessToken = accessToken,
		refreshToken = refreshToken,
	)
