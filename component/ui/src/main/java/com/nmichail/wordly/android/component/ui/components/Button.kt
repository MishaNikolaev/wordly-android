package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.theme.WordlyColors

@Composable
fun Button(
	text: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	loading: Boolean = false,
) {
	Button(
		onClick = onClick,
		modifier = modifier
			.fillMaxWidth()
			.height(52.dp),
		enabled = enabled && !loading,
		shape = MaterialTheme.shapes.small,
		colors = ButtonDefaults.buttonColors(
			containerColor = WordlyColors.Primary,
			contentColor = WordlyColors.OnPrimary,
			disabledContainerColor = WordlyColors.Primary.copy(alpha = 0.4f),
			disabledContentColor = WordlyColors.OnPrimary.copy(alpha = 0.6f),
		),
	) {
		if (loading) {
			CircularProgressIndicator(
				modifier = Modifier.size(24.dp),
				color = WordlyColors.OnPrimary,
				strokeWidth = 2.dp,
			)
		} else {
			Text(
				text = text,
				style = MaterialTheme.typography.labelLarge,
			)
		}
	}
}