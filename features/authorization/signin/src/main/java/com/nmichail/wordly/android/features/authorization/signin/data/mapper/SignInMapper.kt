package com.nmichail.wordly.android.features.authorization.signin.data.mapper

import com.nmichail.wordly.android.features.authorization.signin.data.dto.SignInRequest
import com.nmichail.wordly.android.features.authorization.signin.domain.entity.SignInData

fun SignInData.toRequest(): SignInRequest =
	SignInRequest(
		email = email,
		password = password,
	)
