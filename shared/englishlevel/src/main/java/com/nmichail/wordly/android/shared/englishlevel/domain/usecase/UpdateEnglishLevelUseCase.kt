package com.nmichail.wordly.android.shared.englishlevel.domain.usecase

import com.nmichail.wordly.android.shared.englishlevel.domain.repository.EnglishLevelRepository
import javax.inject.Inject

class UpdateEnglishLevelUseCase @Inject constructor(
    englishLevelRepository: EnglishLevelRepository,
) : suspend (String) -> Unit by englishLevelRepository::updateEnglishLevel