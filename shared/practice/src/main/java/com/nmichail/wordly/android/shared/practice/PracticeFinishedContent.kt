package com.nmichail.wordly.android.shared.practice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.components.button.CustomButton
import com.nmichail.wordly.android.component.ui.theme.WordlyTheme

@Composable
fun PracticeFinishedContent(
	correctCount: Int,
	totalCount: Int,
	subtitle: String,
	primaryActionText: String,
	onPrimaryClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.statusBarsPadding()
			.navigationBarsPadding()
			.padding(horizontal = 24.dp),
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		PracticeFinishedResult(
			correctCount = correctCount,
			totalCount = totalCount,
			subtitle = subtitle,
			modifier = Modifier
				.weight(1f)
				.fillMaxWidth(),
		)
		CustomButton(
			text = primaryActionText,
			onClick = onPrimaryClick,
			modifier = Modifier
				.fillMaxWidth()
				.padding(bottom = 8.dp),
		)
	}
}

@Composable
private fun PracticeFinishedResult(
	correctCount: Int,
	totalCount: Int,
	subtitle: String,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme
	val isGreatResult = correctCount * 2 > totalCount
	Column(
		modifier = modifier,
		horizontalAlignment = Alignment.CenterHorizontally,
		verticalArrangement = Arrangement.Center,
	) {
		Icon(
			imageVector = Icons.Rounded.CheckCircle,
			contentDescription = null,
			tint = WordlyTheme.colors.success,
			modifier = Modifier.size(72.dp),
		)
		Text(
			text = stringResource(
				if (isGreatResult) {
					R.string.practice_finished_title_great
				} else {
					R.string.practice_finished_title_done
				},
			),
			style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
			color = colorScheme.onSurface,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(top = 20.dp),
		)
		Text(
			text = subtitle,
			style = MaterialTheme.typography.bodyLarge,
			color = colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(top = 8.dp),
		)
	}
}
