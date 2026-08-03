package com.nmichail.wordly.android.component.ui.theme

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

enum class PreviewTheme {
	Light,
	Dark,
}

class PreviewThemeProvider : PreviewParameterProvider<PreviewTheme> {
	override val values: Sequence<PreviewTheme> = PreviewTheme.entries.asSequence()
}

/** Light + Dark previews; pair with [PreviewTheme] / [PreviewThemeProvider] and explicit `darkTheme`. */
@Preview(showBackground = true)
annotation class WordlyPreviews
