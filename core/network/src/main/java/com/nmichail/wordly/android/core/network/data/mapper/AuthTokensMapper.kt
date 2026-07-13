package com.nmichail.wordly.android.core.network.data.mapper

import com.nmichail.wordly.android.core.network.domain.entity.AuthTokens
import com.nmichail.wordly.android.core.network.dto.AuthTokensResponse

fun AuthTokensResponse.toEntity(): AuthTokens =
	AuthTokens(
		accessToken = accessToken,
		refreshToken = refreshToken,
	)
