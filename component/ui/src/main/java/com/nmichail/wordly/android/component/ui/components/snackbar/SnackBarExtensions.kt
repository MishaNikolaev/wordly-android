package com.nmichail.wordly.android.component.ui.components.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun CoroutineScope.showErrorSnackBar(
	snackBarHostState: SnackbarHostState,
	title: String? = null,
	message: String,
	actionLabel: String? = null,
) {
	showCustomSnackBar(
		snackBarHostState = snackBarHostState,
		title = title,
		message = message,
		actionLabel = actionLabel,
		withDismissAction = true,
		snackBarType = SnackBarType.ERROR,
	)
}

fun CoroutineScope.showCustomSnackBar(
	snackBarHostState: SnackbarHostState,
	message: String,
	snackBarType: SnackBarType,
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
	snackBarType: SnackBarType,
	title: String? = null,
	message: String,
	actionLabel: String? = null,
	withDismissAction: Boolean = false,
	duration: SnackbarDuration =
		if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,
): SnackbarResult {
	val visuals = CustomSnackBarVisuals(
		title = title,
		message = message,
		actionLabel = actionLabel,
		withDismissAction = withDismissAction,
		duration = duration,
		snackBarType = snackBarType,
	)

	return showSnackbar(visuals)
}