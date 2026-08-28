package com.nmichail.wordly.android.component.wui.theme

import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import com.nmichail.wordly.android.component.wui.R

val InterFontFamily = FontFamily(
	Font(R.font.inter_regular, FontWeight.Normal),
)

val JetBrainsMonoFontFamily = FontFamily(
	Font(R.font.jetbrains_mono_medium, FontWeight.Medium),
)

@OptIn(ExperimentalTextApi::class)
val OnestFontFamily = FontFamily(
	Font(
		resId = R.font.onest,
		weight = FontWeight.Normal,
		variationSettings = FontVariation.Settings(FontVariation.weight(400)),
	),
	Font(
		resId = R.font.onest,
		weight = FontWeight.Medium,
		variationSettings = FontVariation.Settings(FontVariation.weight(500)),
	),
	Font(
		resId = R.font.onest,
		weight = FontWeight.SemiBold,
		variationSettings = FontVariation.Settings(FontVariation.weight(600)),
	),
	Font(
		resId = R.font.onest,
		weight = FontWeight.Bold,
		variationSettings = FontVariation.Settings(FontVariation.weight(700)),
	),
	Font(
		resId = R.font.onest,
		weight = FontWeight.ExtraBold,
		variationSettings = FontVariation.Settings(FontVariation.weight(800)),
	),
)

val KazimirTextFontFamily = FontFamily(
	Font(R.font.kazimirtext, FontWeight.Normal),
)
