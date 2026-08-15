package com.nmichail.wordly.android.component.wui.theme

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider

enum class PreviewTheme {
	Light,
	Dark,
}

class PreviewThemeProvider : PreviewParameterProvider<PreviewTheme> {
	override val values: Sequence<PreviewTheme> = PreviewTheme.entries.asSequence()
}

@Preview(showBackground = true)
annotation class WuiPreviews
