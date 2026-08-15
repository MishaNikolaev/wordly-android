package com.nmichail.wordly.android.component.wui.components.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun CoroutineScope.showWuiErrorSnackBar(
	snackBarHostState: SnackbarHostState,
	title: String? = null,
	message: String,
	actionLabel: String? = null,
) {
	showWuiCustomSnackBar(
		snackBarHostState = snackBarHostState,
		title = title,
		message = message,
		actionLabel = actionLabel,
		withDismissAction = true,
		snackBarType = WuiSnackBarType.ERROR,
	)
}

fun CoroutineScope.showWuiInfoSnackBar(
	snackBarHostState: SnackbarHostState,
	message: String,
	title: String? = null,
) {
	showWuiCustomSnackBar(
		snackBarHostState = snackBarHostState,
		title = title,
		message = message,
		snackBarType = WuiSnackBarType.INFO,
	)
}

fun CoroutineScope.showWuiCustomSnackBar(
	snackBarHostState: SnackbarHostState,
	message: String,
	snackBarType: WuiSnackBarType,
	title: String? = null,
	actionLabel: String? = null,
	duration: SnackbarDuration =
		if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Long,
	withDismissAction: Boolean = false,
) {
	snackBarHostState.currentSnackbarData?.dismiss()

	launch {
		snackBarHostState.showSnackBar(
			title = title,
			message = message,
			actionLabel = actionLabel,
			withDismissAction = withDismissAction,
			duration = duration,
			snackBarType = snackBarType,
		)
	}
}

private suspend fun SnackbarHostState.showSnackBar(
	snackBarType: WuiSnackBarType,
	title: String? = null,
	message: String,
	actionLabel: String? = null,
	withDismissAction: Boolean = false,
	duration: SnackbarDuration =
		if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,
): SnackbarResult {
	val visuals = WuiSnackBarVisuals(
		title = title,
		message = message,
		actionLabel = actionLabel,
		withDismissAction = withDismissAction,
		duration = duration,
		snackBarType = snackBarType,
	)

	return showSnackbar(visuals)
}