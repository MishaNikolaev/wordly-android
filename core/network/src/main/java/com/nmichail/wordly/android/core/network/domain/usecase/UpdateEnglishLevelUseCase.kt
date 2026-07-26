package com.nmichail.wordly.android.core.network.domain.usecase

import com.nmichail.wordly.android.core.network.api.EnglishLevelApi
import com.nmichail.wordly.android.core.network.api.EnglishLevelRequest
import javax.inject.Inject

class UpdateEnglishLevelUseCase @Inject constructor(
	private val englishLevelApi: EnglishLevelApi,
) : suspend (String) -> Unit {

	override suspend fun invoke(level: String) {
		englishLevelApi.updateEnglishLevel(request = EnglishLevelRequest(level = level))
	}
}