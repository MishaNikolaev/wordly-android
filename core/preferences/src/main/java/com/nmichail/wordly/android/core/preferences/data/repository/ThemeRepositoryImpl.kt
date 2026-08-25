package com.nmichail.wordly.android.core.preferences.data.repository

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.nmichail.wordly.android.core.preferences.domain.entity.AppThemeMode
import com.nmichail.wordly.android.core.preferences.domain.entity.toNightMode
import com.nmichail.wordly.android.core.preferences.domain.repository.ThemeRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

private const val THEME_PREFERENCES = "APP_THEME_PREFERENCES"
private const val THEME_MODE_KEY = "THEME_MODE"

@Singleton
class ThemeRepositoryImpl @Inject constructor(
	context: Context,
) : ThemeRepository {

	private val preferences = context.getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)
	private val _themeMode = MutableStateFlow(readThemeMode())
	override val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

	init {
		applyNightMode(_themeMode.value)
	}

	override fun getThemeMode(): AppThemeMode = _themeMode.value

	override fun setThemeMode(mode: AppThemeMode) {
		preferences.edit()
			.putString(THEME_MODE_KEY, mode.name)
			.apply()
		_themeMode.value = mode
		applyNightMode(mode)
	}

	private fun readThemeMode(): AppThemeMode {
		val raw = preferences.getString(THEME_MODE_KEY, AppThemeMode.System.name)
		return AppThemeMode.entries.firstOrNull { it.name == raw } ?: AppThemeMode.System
	}

	private fun applyNightMode(mode: AppThemeMode) {
		AppCompatDelegate.setDefaultNightMode(mode.toNightMode())
	}
}