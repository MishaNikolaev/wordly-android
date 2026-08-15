package com.nmichail.wordly.android.component.wui.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.wui.theme.WuiTheme
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.nmichail.wordly.android.component.wui.theme.PreviewTheme
import com.nmichail.wordly.android.component.wui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.wui.theme.WuiPreviews

@Composable
fun WuiButton(
	text: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
	enabled: Boolean = true,
	loading: Boolean = false,
	leadingIcon: ImageVector? = null,
	containerColor: Color = MaterialTheme.colorScheme.primary,
	contentColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
	Button(
		onClick = onClick,
		modifier = modifier
			.fillMaxWidth()
			.height(52.dp),
		enabled = enabled && !loading,
		shape = MaterialTheme.shapes.small,
		colors = ButtonDefaults.buttonColors(
			containerColor = containerColor,
			contentColor = contentColor,
			disabledContainerColor = containerColor.copy(alpha = 0.4f),
			disabledContentColor = contentColor.copy(alpha = 0.6f),
		),
	) {
		if (loading) {
			CircularProgressIndicator(
				modifier = Modifier.size(24.dp),
				color = contentColor,
				strokeWidth = 2.dp,
			)
		} else {
			Row(
				horizontalArrangement = Arrangement.spacedBy(8.dp),
				verticalAlignment = Alignment.CenterVertically,
			) {
				if (leadingIcon != null) {
					Icon(
						imageVector = leadingIcon,
						contentDescription = null,
						modifier = Modifier.size(20.dp),
					)
				}
				Text(
					text = text,
					style = MaterialTheme.typography.labelLarge,
				)
			}
		}
	}
}

@WuiPreviews
@Composable
private fun CustomButtonPreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WuiTheme(darkTheme = theme == PreviewTheme.Dark) {
		Column(
			modifier = Modifier.padding(16.dp),
			verticalArrangement = Arrangement.spacedBy(12.dp),
		) {
			WuiButton(text = "Сохранить", onClick = {})
			WuiButton(text = "Загрузка", onClick = {}, loading = true)
			WuiButton(text = "Disabled", onClick = {}, enabled = false)
		}
	}
}