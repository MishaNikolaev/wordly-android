package com.nmichail.wordly.android.features.authorization.signup.data.dto

data class SignUpRequest(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val englishLevel: String,
)