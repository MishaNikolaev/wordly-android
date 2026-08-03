package com.nmichail.wordly.android.component.ui.components.snackbar

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
import com.nmichail.wordly.android.component.ui.R
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.nmichail.wordly.android.component.ui.theme.WordlyAndroidTheme
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.nmichail.wordly.android.component.ui.theme.PreviewTheme
import com.nmichail.wordly.android.component.ui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.ui.theme.WordlyPreviews
import com.nmichail.wordly.android.component.ui.theme.WordlyTheme

enum class SnackBarType {
	INFO,
	SUCCESS,
	ALERT,
	ERROR,
}

@Composable
fun SnackBarHost(
	snackBarHostState: SnackbarHostState,
	modifier: Modifier = Modifier,
	onAction: (() -> Unit)? = null,
) {
	SnackbarHost(
		hostState = snackBarHostState,
		modifier = modifier,
		snackbar = { snackBarData ->
			SnackBar(
				snackBarData = snackBarData,
				snackBarHostState = snackBarHostState,
				onAction = onAction,
			)
		},
	)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SnackBar(
	snackBarData: SnackbarData,
	snackBarHostState: SnackbarHostState,
	modifier: Modifier = Modifier,
	onAction: (() -> Unit)? = null,
	onDismiss: (() -> Unit)? = null,
) {
	val visuals = snackBarData.visuals as CustomSnackBarVisuals
	val snackBarColor = getSnackBarColor(visuals.snackBarType)

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
				SnackBarContent(
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
private fun SnackBarContent(
	visuals: CustomSnackBarVisuals,
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
			imageVector = ImageVector.vectorResource(getSnackBarIcon(visuals.snackBarType)),
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
			SnackBarActionButton(
				actionLabel = actionLabel,
				snackBarColor = snackBarColor,
				onAction = onAction,
				snackBarHostState = snackBarHostState,
			)
		}

		if (visuals.withDismissAction) {
			SnackBarDismissButton(
				onDismiss = onDismiss,
				snackBarHostState = snackBarHostState,
			)
		}
	}
}

@Composable
private fun RowScope.SnackBarActionButton(
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
private fun RowScope.SnackBarDismissButton(
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

internal data class CustomSnackBarVisuals(
	val title: String? = null,
	override val message: String,
	override val actionLabel: String? = null,
	override val withDismissAction: Boolean = false,
	override val duration: SnackbarDuration =
		if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,
	val snackBarType: SnackBarType,
) : SnackbarVisuals

@Composable
private fun getSnackBarColor(snackBarType: SnackBarType): Color {
	val extended = WordlyTheme.colors
	return when (snackBarType) {
		SnackBarType.INFO -> MaterialTheme.colorScheme.primary
		SnackBarType.SUCCESS -> extended.success
		SnackBarType.ALERT -> extended.warning
		SnackBarType.ERROR -> MaterialTheme.colorScheme.error
	}
}

@DrawableRes
private fun getSnackBarIcon(snackBarType: SnackBarType): Int =
	when (snackBarType) {
		SnackBarType.INFO -> R.drawable.ic_snackbar_info
		SnackBarType.SUCCESS -> R.drawable.ic_snackbar_success
		SnackBarType.ALERT -> R.drawable.ic_snackbar_alert
		SnackBarType.ERROR -> R.drawable.ic_snackbar_error
	}

@WordlyPreviews
@Composable
private fun SnackBarHostPreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WordlyAndroidTheme(darkTheme = theme == PreviewTheme.Dark) {
		val hostState = remember { SnackbarHostState() }
		LaunchedEffect(Unit) {
			hostState.showSnackbar(
				CustomSnackBarVisuals(
					title = "Серия дней",
					message = "Вы занимаетесь 7 дней подряд",
					actionLabel = null,
					withDismissAction = true,
					duration = SnackbarDuration.Indefinite,
					snackBarType = SnackBarType.SUCCESS,
				),
			)
		}
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.height(120.dp),
			contentAlignment = Alignment.BottomCenter,
		) {
			SnackBarHost(snackBarHostState = hostState)
		}
	}
}
