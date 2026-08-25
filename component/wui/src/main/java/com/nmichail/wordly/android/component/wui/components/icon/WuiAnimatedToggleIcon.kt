package com.nmichail.wordly.android.component.wui.components.icon

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.wui.theme.PreviewTheme
import com.nmichail.wordly.android.component.wui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.wui.theme.WuiPreviews
import com.nmichail.wordly.android.component.wui.theme.WuiTheme

@Composable
fun WuiAnimatedToggleIcon(
	checked: Boolean,
	checkedIcon: ImageVector,
	uncheckedIcon: ImageVector,
	modifier: Modifier = Modifier,
	checkedTint: Color = MaterialTheme.colorScheme.primary,
	uncheckedTint: Color = MaterialTheme.colorScheme.onBackground,
	contentDescription: String? = null,
	iconSize: Dp = 28.dp,
) {
	val filledScale = remember { Animatable(0f) }
	var showUnchecked by remember { mutableStateOf(true) }

	LaunchedEffect(checked) {
		if (checked) {
			showUnchecked = true
			filledScale.snapTo(0f)
			filledScale.animateTo(
				targetValue = 1f,
				animationSpec = spring(
					dampingRatio = Spring.DampingRatioMediumBouncy,
					stiffness = Spring.StiffnessMediumLow,
				),
			)
			showUnchecked = false
		} else {
			filledScale.snapTo(0f)
			showUnchecked = true
		}
	}

	Box(
		modifier = modifier.size(iconSize),
		contentAlignment = Alignment.Center,
	) {
		if (showUnchecked) {
			Icon(
				imageVector = uncheckedIcon,
				contentDescription = contentDescription,
				tint = uncheckedTint,
				modifier = Modifier.size(iconSize),
			)
		}
		if (filledScale.value > 0f) {
			Icon(
				imageVector = checkedIcon,
				contentDescription = if (showUnchecked) null else contentDescription,
				tint = checkedTint,
				modifier = Modifier
					.size(iconSize)
					.scale(filledScale.value),
			)
		}
	}
}

@WuiPreviews
@Composable
private fun WuiAnimatedToggleIconPreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WuiTheme(darkTheme = theme == PreviewTheme.Dark) {
		WuiAnimatedToggleIcon(
			checked = true,
			checkedIcon = Icons.Filled.Bookmark,
			uncheckedIcon = Icons.Outlined.BookmarkBorder,
		)
	}
}
