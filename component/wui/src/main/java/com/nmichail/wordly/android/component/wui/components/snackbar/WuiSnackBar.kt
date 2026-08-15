package com.nmichail.wordly.android.component.wui.components.snackbar

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.wui.R
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.nmichail.wordly.android.component.wui.theme.WuiTheme
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.nmichail.wordly.android.component.wui.theme.PreviewTheme
import com.nmichail.wordly.android.component.wui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.wui.theme.WuiPreviews
import com.nmichail.wordly.android.component.wui.theme.Wui

enum class WuiSnackBarType {
	INFO,
	SUCCESS,
	ALERT,
	ERROR,
}

@Composable
fun WuiSnackBarHost(
	snackBarHostState: SnackbarHostState,
	modifier: Modifier = Modifier,
	onAction: (() -> Unit)? = null,
) {
	SnackbarHost(
		hostState = snackBarHostState,
		modifier = modifier,
		snackbar = { snackBarData ->
			WuiSnackBar(
				snackBarData = snackBarData,
				snackBarHostState = snackBarHostState,
				onAction = onAction,
			)
		},
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WuiSnackBar(
	snackBarData: SnackbarData,
	snackBarHostState: SnackbarHostState,
	modifier: Modifier = Modifier,
	onAction: (() -> Unit)? = null,
	onDismiss: (() -> Unit)? = null,
) {
	val visuals = snackBarData.visuals as WuiSnackBarVisuals
	val snackBarColor = getWuiSnackBarColor(visuals.snackBarType)

	val swipeToDismissState = rememberSwipeToDismissBoxState(
		confirmValueChange = { value ->
			if (value == SwipeToDismissBoxValue.EndToStart || value == SwipeToDismissBoxValue.StartToEnd) {
				snackBarHostState.currentSnackbarData?.dismiss()
				true
			} else {
				false
			}
		},
	)

	SwipeToDismissBox(
		state = swipeToDismissState,
		backgroundContent = {},
	) {
		Surface(
			color = MaterialTheme.colorScheme.surface,
			shadowElevation = 6.dp,
			modifier = modifier
				.padding(horizontal = 16.dp, vertical = 8.dp)
				.fillMaxWidth()
				.heightIn(min = 68.dp),
			shape = RoundedCornerShape(size = 4.dp),
		) {
			Row(
				verticalAlignment = Alignment.CenterVertically,
				modifier = Modifier.height(IntrinsicSize.Min),
			) {
				Box(
					modifier = Modifier
						.fillMaxHeight()
						.width(8.dp)
						.background(color = snackBarColor),
				)
				WuiSnackBarContent(
					visuals = visuals,
					snackBarColor = snackBarColor,
					onAction = onAction,
					onDismiss = onDismiss,
					snackBarHostState = snackBarHostState,
				)
			}
		}
	}
}

@Composable
private fun WuiSnackBarContent(
	visuals: WuiSnackBarVisuals,
	snackBarColor: Color,
	onAction: (() -> Unit)?,
	onDismiss: (() -> Unit)?,
	snackBarHostState: SnackbarHostState,
) {
	Row(
		verticalAlignment = Alignment.CenterVertically,
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 8.dp),
	) {
		Icon(
			imageVector = ImageVector.vectorResource(getWuiSnackBarIcon(visuals.snackBarType)),
			contentDescription = null,
			tint = snackBarColor,
			modifier = Modifier.padding(horizontal = 16.dp),
		)

		Column(
			modifier = Modifier.weight(weight = 0.5f),
		) {
			visuals.title?.let { title ->
				Text(
					text = title,
					style = MaterialTheme.typography.bodyMedium,
					color = snackBarColor,
				)
			}

			Text(
				text = visuals.message,
				style = MaterialTheme.typography.bodyMedium,
				color = MaterialTheme.colorScheme.onSurface,
			)
		}

		visuals.actionLabel?.let { actionLabel ->
			WuiSnackBarActionButton(
				actionLabel = actionLabel,
				snackBarColor = snackBarColor,
				onAction = onAction,
				snackBarHostState = snackBarHostState,
			)
		}

		if (visuals.withDismissAction) {
			WuiSnackBarDismissButton(
				onDismiss = onDismiss,
				snackBarHostState = snackBarHostState,
			)
		}
	}
}

@Composable
private fun RowScope.WuiSnackBarActionButton(
	actionLabel: String,
	snackBarColor: Color,
	onAction: (() -> Unit)?,
	snackBarHostState: SnackbarHostState,
) {
	TextButton(
		modifier = Modifier.weight(weight = 0.4f),
		onClick = {
			onAction?.invoke()
			snackBarHostState.currentSnackbarData?.performAction()
		},
	) {
		Text(
			text = actionLabel,
			style = MaterialTheme.typography.bodyMedium,
			color = snackBarColor,
		)
	}
}

@Composable
private fun RowScope.WuiSnackBarDismissButton(
	onDismiss: (() -> Unit)?,
	snackBarHostState: SnackbarHostState,
) {
	IconButton(
		modifier = Modifier.weight(weight = 0.1f),
		onClick = {
			onDismiss?.invoke()
			snackBarHostState.currentSnackbarData?.dismiss()
		},
	) {
		Icon(
			imageVector = ImageVector.vectorResource(R.drawable.ic_snackbar_dismiss),
			contentDescription = null,
			tint = MaterialTheme.colorScheme.onSurface,
		)
	}
}

internal data class WuiSnackBarVisuals(
	val title: String? = null,
	override val message: String,
	override val actionLabel: String? = null,
	override val withDismissAction: Boolean = false,
	override val duration: SnackbarDuration =
		if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,
	val snackBarType: WuiSnackBarType,
) : SnackbarVisuals

@Composable
private fun getWuiSnackBarColor(snackBarType: WuiSnackBarType): Color {
	val extended = Wui.colors
	return when (snackBarType) {
		WuiSnackBarType.INFO -> MaterialTheme.colorScheme.primary
		WuiSnackBarType.SUCCESS -> extended.success
		WuiSnackBarType.ALERT -> extended.warning
		WuiSnackBarType.ERROR -> MaterialTheme.colorScheme.error
	}
}

@DrawableRes
private fun getWuiSnackBarIcon(snackBarType: WuiSnackBarType): Int =
	when (snackBarType) {
		WuiSnackBarType.INFO -> R.drawable.ic_snackbar_info
		WuiSnackBarType.SUCCESS -> R.drawable.ic_snackbar_success
		WuiSnackBarType.ALERT -> R.drawable.ic_snackbar_alert
		WuiSnackBarType.ERROR -> R.drawable.ic_snackbar_error
	}

@WuiPreviews
@Composable
private fun SnackBarHostPreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WuiTheme(darkTheme = theme == PreviewTheme.Dark) {
		val hostState = remember { SnackbarHostState() }
		LaunchedEffect(Unit) {
			hostState.showSnackbar(
				WuiSnackBarVisuals(
					title = "Серия дней",
					message = "Вы занимаетесь 7 дней подряд",
					actionLabel = null,
					withDismissAction = true,
					duration = SnackbarDuration.Indefinite,
					snackBarType = WuiSnackBarType.SUCCESS,
				),
			)
		}
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.height(120.dp),
			contentAlignment = Alignment.BottomCenter,
		) {
			WuiSnackBarHost(snackBarHostState = hostState)
		}
	}
}
