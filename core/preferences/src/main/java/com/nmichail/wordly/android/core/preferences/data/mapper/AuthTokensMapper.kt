package com.nmichail.wordly.android.core.preferences.data.mapper

import com.nmichail.wordly.android.core.preferences.data.dto.AuthTokensResponse
import com.nmichail.wordly.android.core.preferences.domain.entity.AuthTokens

fun AuthTokensResponse.toEntity(): AuthTokens =
	AuthTokens(
		accessToken = accessToken,
		refreshToken = refreshToken,
	)
