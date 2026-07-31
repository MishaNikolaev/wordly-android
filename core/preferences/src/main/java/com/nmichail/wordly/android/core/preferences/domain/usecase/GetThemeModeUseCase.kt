package com.nmichail.wordly.android.core.preferences.domain.usecase

import com.nmichail.wordly.android.core.preferences.domain.entity.AppThemeMode
import com.nmichail.wordly.android.core.preferences.domain.repository.ThemeRepository
import javax.inject.Inject

class GetThemeModeUseCase @Inject constructor(
	private val themeRepository: ThemeRepository,
) {
	operator fun invoke(): AppThemeMode = themeRepository.getThemeMode()
}
