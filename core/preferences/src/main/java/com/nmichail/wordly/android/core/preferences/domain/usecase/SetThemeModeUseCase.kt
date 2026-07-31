package com.nmichail.wordly.android.core.preferences.domain.usecase

import com.nmichail.wordly.android.core.preferences.domain.entity.AppThemeMode
import com.nmichail.wordly.android.core.preferences.domain.repository.ThemeRepository
import javax.inject.Inject

class SetThemeModeUseCase @Inject constructor(
	private val themeRepository: ThemeRepository,
) {
	operator fun invoke(mode: AppThemeMode) {
		themeRepository.setThemeMode(mode)
	}
}
