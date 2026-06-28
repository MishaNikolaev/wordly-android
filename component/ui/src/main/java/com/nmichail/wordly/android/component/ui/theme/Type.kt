package com.nmichail.wordly.android.component.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Typography = Typography(
	displaySmall = TextStyle(
		fontWeight = FontWeight.Bold,
		fontSize = 32.sp,
		lineHeight = 40.sp,
	),
	headlineMedium = TextStyle(
		fontWeight = FontWeight.Bold,
		fontSize = 28.sp,
		lineHeight = 36.sp,
	),
	titleLarge = TextStyle(
		fontWeight = FontWeight.SemiBold,
		fontSize = 20.sp,
		lineHeight = 28.sp,
	),
	titleMedium = TextStyle(
		fontWeight = FontWeight.Medium,
		fontSize = 16.sp,
		lineHeight = 24.sp,
	),
	bodyLarge = TextStyle(
		fontWeight = FontWeight.Normal,
		fontSize = 16.sp,
		lineHeight = 24.sp,
	),
	bodyMedium = TextStyle(
		fontWeight = FontWeight.Normal,
		fontSize = 14.sp,
		lineHeight = 20.sp,
	),
	labelLarge = TextStyle(
		fontWeight = FontWeight.SemiBold,
		fontSize = 16.sp,
		lineHeight = 24.sp,
	),
	labelMedium = TextStyle(
		fontWeight = FontWeight.Medium,
		fontSize = 14.sp,
		lineHeight = 20.sp,
	),
	labelSmall = TextStyle(
		fontWeight = FontWeight.Normal,
		fontSize = 12.sp,
		lineHeight = 16.sp,
	),
)

object WordlyTypography {

	val mono = TextStyle(
		fontFamily = JetBrainsMonoFontFamily,
		fontWeight = FontWeight.Medium,
		fontSize = 14.sp,
		lineHeight = 20.sp,
	)

	val wordCardWord = TextStyle(
		fontWeight = FontWeight.ExtraBold,
		fontSize = 28.sp,
		lineHeight = 32.sp,
	)

	val wordCardPhonetic = TextStyle(
		fontFamily = JetBrainsMonoFontFamily,
		fontWeight = FontWeight.SemiBold,
		fontSize = 15.sp,
		lineHeight = 18.sp,
	)

	val authPreviewWord = TextStyle(
		fontWeight = FontWeight.Bold,
		fontSize = 18.sp,
		lineHeight = 22.sp,
	)

	val authPreviewTranslation = TextStyle(
		fontWeight = FontWeight.Normal,
		fontSize = 12.sp,
		lineHeight = 16.sp,
	)
}