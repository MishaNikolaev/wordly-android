package com.nmichail.wordly.android.features.authorization.signup.domain.entity

data class SignUpForm(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val englishLevel: String,
)