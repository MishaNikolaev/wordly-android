package com.nmichail.wordly.android.core.preferences.domain.repository

import com.nmichail.wordly.android.core.preferences.domain.entity.AppThemeMode
import kotlinx.coroutines.flow.StateFlow

interface ThemeRepository {

	val themeMode: StateFlow<AppThemeMode>

	fun getThemeMode(): AppThemeMode

	fun setThemeMode(mode: AppThemeMode)
}