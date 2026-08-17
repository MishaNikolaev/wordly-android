package com.nmichail.wordly.android.shared.englishlevel.domain.repository

interface EnglishLevelRepository {

    suspend fun updateEnglishLevel(level: String)
}