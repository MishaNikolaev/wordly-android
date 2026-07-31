package com.nmichail.wordly.android.core.preferences.domain.usecase

import com.nmichail.wordly.android.core.preferences.domain.entity.AppThemeMode
import com.nmichail.wordly.android.core.preferences.domain.repository.ThemeRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow

class ObserveThemeModeUseCase @Inject constructor(
	private val themeRepository: ThemeRepository,
) {
	operator fun invoke(): StateFlow<AppThemeMode> = themeRepository.themeMode
}
