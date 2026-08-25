package com.nmichail.wordly.android.core.preferences.domain.entity

import androidx.appcompat.app.AppCompatDelegate

fun AppThemeMode.toNightMode(): Int =
	when (this) {
		AppThemeMode.System -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
		AppThemeMode.Light -> AppCompatDelegate.MODE_NIGHT_NO
		AppThemeMode.Dark -> AppCompatDelegate.MODE_NIGHT_YES
	}