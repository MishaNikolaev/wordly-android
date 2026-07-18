package com.nmichail.wordly.android.component.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nmichail.wordly.android.component.ui.R
import java.time.LocalTime

private const val MORNING_HOUR_START = 5
private const val MORNING_HOUR_END = 11
private const val DAY_HOUR_START = 12
private const val DAY_HOUR_END = 16
private const val EVENING_HOUR_START = 17
private const val EVENING_HOUR_END = 22

@Composable
fun homeGreeting(firstName: String): String {
	if (firstName.isBlank()) {
		return stringResource(R.string.home_screen_title)
	}
	val greetingRes = when (LocalTime.now().hour) {
		in MORNING_HOUR_START..MORNING_HOUR_END -> R.string.home_greeting_morning
		in DAY_HOUR_START..DAY_HOUR_END -> R.string.home_greeting_day
		in EVENING_HOUR_START..EVENING_HOUR_END -> R.string.home_greeting_evening
		else -> R.string.home_greeting_night
	}
	return stringResource(greetingRes, firstName)
}
