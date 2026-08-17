package com.nmichail.wordly.android.features.authorization.signup.data.mapper

import com.nmichail.wordly.android.features.authorization.signup.data.dto.SignUpRequest
import com.nmichail.wordly.android.features.authorization.signup.domain.entity.SignUpForm

fun SignUpForm.toRequest(): SignUpRequest =
    SignUpRequest(
        email = email,
        password = password,
        firstName = firstName,
        lastName = lastName,
        englishLevel = englishLevel,
    )