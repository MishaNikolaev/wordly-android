package com.nmichail.wordly.android.component.wui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
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
		fontWeight = FontWeight.ExtraBold,
		fontSize = 54.sp,
		lineHeight = 54.sp,
	)

	val dailyReviewCountLabel = TextStyle(
		fontWeight = FontWeight.Bold,
		fontSize = 17.sp,
		lineHeight = 20.sp,
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

	/** Book reader body — do not use outside book reader. */
	val bookReaderBody = TextStyle(
		fontFamily = KazimirTextFontFamily,
		fontWeight = FontWeight.Normal,
		fontSize = 20.sp,
		lineHeight = 30.sp,
	)

	/** Book reading progress label — do not use outside book reader. */
	val bookReaderProgress = TextStyle(
		fontFamily = KazimirTextFontFamily,
		fontWeight = FontWeight.Normal,
		fontSize = 14.sp,
		lineHeight = 18.sp,
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

	val homeQuote = TextStyle(
		fontFamily = OnestFontFamily,
		fontWeight = FontWeight.Medium,
		fontSize = 16.sp,
		lineHeight = 22.sp,
	)

	val homeTrainingsTitle = TextStyle(
		fontFamily = OnestFontFamily,
		fontWeight = FontWeight.ExtraBold,
		fontSize = 18.sp,
		lineHeight = 22.sp,
	)

	val homeTrainingLabel = TextStyle(
		fontFamily = OnestFontFamily,
		fontWeight = FontWeight.Medium,
		fontSize = 10.sp,
		lineHeight = 13.sp,
		platformStyle = PlatformTextStyle(includeFontPadding = false),
	)

	val homeRecapTitle = TextStyle(
		fontFamily = OnestFontFamily,
		fontWeight = FontWeight.ExtraBold,
		fontSize = 25.sp,
		lineHeight = 30.sp,
	)

	val homeRecapSubtitle = TextStyle(
		fontFamily = OnestFontFamily,
		fontWeight = FontWeight.Medium,
		fontSize = 13.sp,
		lineHeight = 18.sp,
	)

	val homeRecapCta = TextStyle(
		fontFamily = OnestFontFamily,
		fontWeight = FontWeight.Bold,
		fontSize = 14.sp,
		lineHeight = 18.sp,
	)
}