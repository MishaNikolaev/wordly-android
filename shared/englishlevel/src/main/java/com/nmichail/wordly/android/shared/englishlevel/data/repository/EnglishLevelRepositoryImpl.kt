package com.nmichail.wordly.android.shared.englishlevel.data.repository

import com.nmichail.wordly.android.shared.englishlevel.data.api.EnglishLevelApi
import com.nmichail.wordly.android.shared.englishlevel.data.dto.EnglishLevelRequest
import com.nmichail.wordly.android.shared.englishlevel.domain.repository.EnglishLevelRepository
import javax.inject.Inject

class EnglishLevelRepositoryImpl @Inject constructor(
    private val englishLevelApi: EnglishLevelApi,
) : EnglishLevelRepository {

    override suspend fun updateEnglishLevel(level: String) {
        englishLevelApi.updateEnglishLevel(request = EnglishLevelRequest(level = level))
    }
}