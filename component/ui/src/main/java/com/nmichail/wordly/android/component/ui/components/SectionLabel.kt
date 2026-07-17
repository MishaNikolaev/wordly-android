package com.nmichail.wordly.android.component.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun SectionLabel(
	text: String,
	modifier: Modifier = Modifier,
) {
	Text(
		text = text.uppercase(),
		modifier = modifier,
		style = MaterialTheme.typography.labelSmall,
		fontWeight = FontWeight.SemiBold,
		letterSpacing = 1.2.sp,
		color = MaterialTheme.colorScheme.onSurfaceVariant,
	)
}
