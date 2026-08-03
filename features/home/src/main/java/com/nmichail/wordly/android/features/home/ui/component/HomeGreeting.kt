package com.nmichail.wordly.android.features.home.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nmichail.wordly.android.features.home.R
import java.time.LocalTime

@Composable
fun homeGreeting(firstName: String): String {
	if (firstName.isBlank()) {
		return stringResource(R.string.home_screen_title)
	}
	val greetingRes = when (LocalTime.now().hour) {
		in 5..11 -> R.string.home_greeting_morning
		in 12..16 -> R.string.home_greeting_day
		in 17..22 -> R.string.home_greeting_evening
		else -> R.string.home_greeting_night
	}
	return stringResource(greetingRes, firstName)
}