package com.nmichail.wordly.android.component.wui.theme

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

object WuiTypography {

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

	val wordDetailWord = TextStyle(
		fontWeight = FontWeight.ExtraBold,
		fontSize = 30.sp,
		lineHeight = 34.sp,
	)

	val wordDetailTranslation = TextStyle(
		fontFamily = OnestFontFamily,
		fontWeight = FontWeight.Normal,
		fontSize = 16.sp,
		lineHeight = 22.sp,
	)

	val wordDetailExamplesTitle = TextStyle(
		fontFamily = OnestFontFamily,
		fontWeight = FontWeight.Medium,
		fontSize = 11.sp,
		lineHeight = 14.sp,
		letterSpacing = 0.6.sp,
	)

	val wordDetailExample = TextStyle(
		fontFamily = OnestFontFamily,
		fontWeight = FontWeight.Normal,
		fontSize = 15.sp,
		lineHeight = 20.sp,
	)

	val wordDetailExampleTranslation = TextStyle(
		fontFamily = OnestFontFamily,
		fontWeight = FontWeight.Normal,
		fontSize = 14.sp,
		lineHeight = 18.sp,
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

	val homeScreenTitle = TextStyle(
		fontWeight = FontWeight.Bold,
		fontSize = 24.sp,
		lineHeight = 32.sp,
	)

	val dailyReviewCount = TextStyle(
		fontWeight = FontWeight.Black,
		fontSize = 56.sp,
		lineHeight = 56.sp,
	)

	val dailyReviewCountLabel = TextStyle(
		fontWeight = FontWeight.Bold,
		fontSize = 18.sp,
		lineHeight = 22.sp,
	)

	val trainingTileTitle = TextStyle(
		fontWeight = FontWeight.Bold,
		fontSize = 17.sp,
		lineHeight = 22.sp,
	)

	val trainingTileSubtitle = TextStyle(
		fontWeight = FontWeight.Normal,
		fontSize = 12.sp,
		lineHeight = 16.sp,
	)

	val bookReaderBody = TextStyle(
		fontFamily = InterFontFamily,
		fontWeight = FontWeight.Normal,
		fontSize = 20.sp,
		lineHeight = 30.sp,
	)

	val addWordTitle = TextStyle(
		fontWeight = FontWeight.Bold,
		fontSize = 20.sp,
		lineHeight = 28.sp,
	)

	val addWordInput = TextStyle(
		fontFamily = OnestFontFamily,
		fontWeight = FontWeight.Medium,
		fontSize = 16.sp,
		lineHeight = 22.sp,
	)

	val addWordAutofillLabel = TextStyle(
		fontFamily = OnestFontFamily,
		fontWeight = FontWeight.Medium,
		fontSize = 13.sp,
		lineHeight = 18.sp,
	)

	val addWordPhonetic = TextStyle(
		fontFamily = JetBrainsMonoFontFamily,
		fontWeight = FontWeight.Medium,
		fontSize = 15.sp,
		lineHeight = 20.sp,
	)

	val addWordExamplesTitle = TextStyle(
		fontFamily = OnestFontFamily,
		fontWeight = FontWeight.Bold,
		fontSize = 11.sp,
		lineHeight = 14.sp,
		letterSpacing = 0.8.sp,
	)

	val addWordExample = TextStyle(
		fontFamily = OnestFontFamily,
		fontWeight = FontWeight.SemiBold,
		fontSize = 14.sp,
		lineHeight = 20.sp,
	)
}