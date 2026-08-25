package com.nmichail.wordly.android.features.books.reader.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.wui.components.progress.WuiBookReadingProgress
import com.nmichail.wordly.android.component.wui.theme.PreviewTheme
import com.nmichail.wordly.android.component.wui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.wui.theme.WuiPreviews
import com.nmichail.wordly.android.component.wui.theme.WuiTheme

@Composable
fun BookReaderProgressBar(
	currentPage: Int,
	pageCount: Int,
	onPageChange: (Int) -> Unit,
	modifier: Modifier = Modifier,
) {
	WuiBookReadingProgress(
		currentPage = currentPage,
		pageCount = pageCount,
		onPageChange = onPageChange,
		modifier = modifier
			.fillMaxWidth()
			.padding(horizontal = 20.dp, vertical = 13.dp),
	)
}

@WuiPreviews
@Composable
private fun BookReaderProgressBarPreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WuiTheme(darkTheme = theme == PreviewTheme.Dark) {
		BookReaderProgressBar(
			currentPage = 0,
			pageCount = 20,
			onPageChange = {},
		)
	}
}