package com.nmichail.wordly.android.features.profile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nmichail.wordly.android.component.ui.R as ComponentR
import com.nmichail.wordly.android.component.ui.components.button.CustomButton
import com.nmichail.wordly.android.component.ui.components.dialog.SelectionDialog
import com.nmichail.wordly.android.core.preferences.domain.entity.AppThemeMode
import com.nmichail.wordly.android.features.profile.R
import com.nmichail.wordly.android.features.profile.domain.entity.DailyGoal
import com.nmichail.wordly.android.features.profile.presentation.ProfileComponent

private val LogoutDialogMaxWidth = 320.dp

@Composable
internal fun ProfileDialogs(
	state: ProfileComponent.State.Content,
	component: ProfileComponent,
) {
	state.levelDialog?.let { dialog ->
		LevelDialog(
			dialog = dialog,
			onConfirm = component::handleConfirmLevel,
			onDismiss = component::handleDismissLevel,
		)
	}
	state.dailyGoalDialog?.let { dialog ->
		DailyGoalDialog(
			dialog = dialog,
			onConfirm = component::handleConfirmDailyGoal,
			onDismiss = component::handleDismissDailyGoal,
		)
	}
	state.notificationsDialog?.let { dialog ->
		NotificationsDialog(
			dialog = dialog,
			onToggle = component::handleToggleNotification,
			onConfirm = component::handleConfirmNotifications,
			onDismiss = component::handleDismissNotifications,
		)
	}
	state.themeDialog?.let { dialog ->
		ThemeDialog(
			dialog = dialog,
			onSelect = component::handleSelectTheme,
			onConfirm = component::handleConfirmTheme,
			onDismiss = component::handleDismissTheme,
		)
	}
	if (state.logoutDialogVisible) {
		LogoutDialog(
			loggingOut = state.loggingOut,
			onConfirm = component::handleConfirmLogout,
			onDismiss = component::handleDismissLogout,
		)
	}
}

@Composable
private fun LevelDialog(
	dialog: ProfileComponent.LevelDialogState,
	onConfirm: (String) -> Unit,
	onDismiss: () -> Unit,
) {
	val levelCodes = stringArrayResource(R.array.profile_english_level_codes).toList()
	val levelLabels = stringArrayResource(R.array.profile_english_levels).toList()
	val options = dialog.options.map { code ->
		val index = levelCodes.indexOf(code)
		if (index >= 0) levelLabels[index] else code
	}
	val selected = run {
		val index = levelCodes.indexOf(dialog.selected)
		if (index >= 0) levelLabels[index] else dialog.selected
	}

	SelectionDialog(
		title = stringResource(R.string.profile_level_dialog_title),
		options = options,
		selectedOption = selected,
		saveButtonText = stringResource(ComponentR.string.common_ok),
		cancelButtonText = stringResource(ComponentR.string.common_cancel),
		onDismiss = onDismiss,
		onSave = { label ->
			val index = options.indexOf(label)
			if (index >= 0) {
				onConfirm(dialog.options[index])
			}
		},
	)
}

@Composable
private fun DailyGoalDialog(
	dialog: ProfileComponent.DailyGoalDialogState,
	onConfirm: (DailyGoal) -> Unit,
	onDismiss: () -> Unit,
) {
	val options = dialog.options.map { goal ->
		stringResource(R.string.profile_daily_goal_value, goal.wordsPerDay)
	}
	val selected = stringResource(
		R.string.profile_daily_goal_value,
		dialog.selected.wordsPerDay,
	)

	SelectionDialog(
		title = stringResource(R.string.profile_daily_goal_dialog_title),
		options = options,
		selectedOption = selected,
		saveButtonText = stringResource(ComponentR.string.common_ok),
		cancelButtonText = stringResource(ComponentR.string.common_cancel),
		onDismiss = onDismiss,
		onSave = { label ->
			val index = options.indexOf(label)
			if (index >= 0) {
				onConfirm(dialog.options[index])
			}
		},
	)
}

@Composable
private fun ThemeDialog(
	dialog: ProfileComponent.ThemeDialogState,
	onSelect: (AppThemeMode) -> Unit,
	onConfirm: () -> Unit,
	onDismiss: () -> Unit,
) {
	val options = dialog.options.map { themeModeLabel(mode = it) }
	val selected = themeModeLabel(mode = dialog.selected)

	SelectionDialog(
		title = stringResource(R.string.profile_theme_dialog_title),
		options = options,
		selectedOption = selected,
		saveButtonText = stringResource(ComponentR.string.common_ok),
		cancelButtonText = stringResource(ComponentR.string.common_cancel),
		onDismiss = onConfirm,
		onCancel = onDismiss,
		onOptionSelected = { label ->
			val index = options.indexOf(label)
			if (index >= 0) {
				onSelect(dialog.options[index])
			}
		},
		onSave = { onConfirm() },
	)
}

@Composable
private fun LogoutDialog(
	loggingOut: Boolean,
	onConfirm: () -> Unit,
	onDismiss: () -> Unit,
) {
	Dialog(
		onDismissRequest = { if (!loggingOut) onDismiss() },
		properties = DialogProperties(usePlatformDefaultWidth = false),
	) {
		Surface(
			modifier = Modifier
				.widthIn(max = LogoutDialogMaxWidth)
				.fillMaxWidth()
				.padding(horizontal = 20.dp),
			shape = MaterialTheme.shapes.large,
			color = MaterialTheme.colorScheme.surfaceContainerHigh,
		) {
			Column(
				modifier = Modifier.padding(
					horizontal = 20.dp,
					vertical = 24.dp,
				),
				horizontalAlignment = Alignment.CenterHorizontally,
			) {
				Text(
					text = stringResource(R.string.profile_logout_title),
					style = MaterialTheme.typography.titleLarge,
					fontWeight = FontWeight.Bold,
					color = MaterialTheme.colorScheme.onSurface,
					textAlign = TextAlign.Center,
				)
				Text(
					text = stringResource(R.string.profile_logout_message),
					style = MaterialTheme.typography.bodyMedium,
					color = MaterialTheme.colorScheme.onSurfaceVariant,
					textAlign = TextAlign.Center,
					modifier = Modifier.padding(top = 8.dp),
				)
				CustomButton(
					text = stringResource(R.string.profile_logout_confirm),
					onClick = onConfirm,
					loading = loggingOut,
					modifier = Modifier.padding(top = 24.dp),
				)
				CustomButton(
					text = stringResource(R.string.profile_logout_cancel),
					onClick = onDismiss,
					enabled = !loggingOut,
					modifier = Modifier.padding(top = 12.dp),
					containerColor = MaterialTheme.colorScheme.surfaceVariant,
					contentColor = MaterialTheme.colorScheme.onSurface,
				)
			}
		}
	}
}

@Composable
internal fun themeModeLabel(mode: AppThemeMode): String =
	stringResource(
		when (mode) {
			AppThemeMode.System -> R.string.profile_theme_system
			AppThemeMode.Light -> R.string.profile_theme_light
			AppThemeMode.Dark -> R.string.profile_theme_dark
		},
	)
