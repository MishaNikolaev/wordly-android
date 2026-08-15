package com.nmichail.wordly.android.features.profile.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nmichail.wordly.android.component.wui.R as ComponentR
import com.nmichail.wordly.android.component.wui.components.button.WuiButton
import com.nmichail.wordly.android.component.wui.components.dialog.WuiSelectionDialog
import com.nmichail.wordly.android.core.preferences.domain.entity.AppThemeMode
import com.nmichail.wordly.android.features.profile.R
import com.nmichail.wordly.android.features.profile.domain.entity.DailyGoal
import com.nmichail.wordly.android.features.profile.domain.entity.DailyGoals
import com.nmichail.wordly.android.features.profile.domain.entity.EnglishLevels
import com.nmichail.wordly.android.features.profile.domain.entity.NotificationTimeSlots
import com.nmichail.wordly.android.features.profile.domain.entity.UserProfile
import com.nmichail.wordly.android.features.profile.presentation.ProfileComponent

private val LogoutDialogMaxWidth = 320.dp

internal enum class ProfileDialog {
	Level,
	DailyGoal,
	Notifications,
	Theme,
	Logout,
}

@Composable
internal fun ProfileDialogHost(
	profile: UserProfile,
	themeMode: AppThemeMode,
	loggingOut: Boolean,
	component: ProfileComponent,
	content: @Composable (openDialog: (ProfileDialog) -> Unit) -> Unit,
) {
	var openDialog by remember { mutableStateOf<ProfileDialog?>(null) }
	var notificationDraft by remember { mutableStateOf<Set<String>>(emptySet()) }
	var themeInitial by remember { mutableStateOf<AppThemeMode?>(null) }

	content { dialog ->
		when (dialog) {
			ProfileDialog.Notifications -> {
				notificationDraft = profile.notificationTimes.map { it.time }.toSet()
			}
			ProfileDialog.Theme -> {
				themeInitial = themeMode
			}
			else -> Unit
		}
		openDialog = dialog
	}

	ProfileDialogs(
		profile = profile,
		themeMode = themeMode,
		loggingOut = loggingOut,
		openDialog = openDialog,
		notificationDraft = notificationDraft,
		themeInitial = themeInitial,
		component = component,
		onOpenDialogChange = { openDialog = it },
		onNotificationDraftChange = { notificationDraft = it },
	)
}

@Composable
private fun ProfileDialogs(
	profile: UserProfile,
	themeMode: AppThemeMode,
	loggingOut: Boolean,
	openDialog: ProfileDialog?,
	notificationDraft: Set<String>,
	themeInitial: AppThemeMode?,
	component: ProfileComponent,
	onOpenDialogChange: (ProfileDialog?) -> Unit,
	onNotificationDraftChange: (Set<String>) -> Unit,
) {
	when (openDialog) {
		ProfileDialog.Level -> LevelDialog(
			selected = profile.englishLevel,
			onConfirm = { level ->
				component.handleUpdateLevel(level = level)
				onOpenDialogChange(null)
			},
			onDismiss = { onOpenDialogChange(null) },
		)
		ProfileDialog.DailyGoal -> DailyGoalDialog(
			selected = profile.dailyGoal,
			onConfirm = { goal ->
				component.handleUpdateDailyGoal(goal = goal)
				onOpenDialogChange(null)
			},
			onDismiss = { onOpenDialogChange(null) },
		)
		ProfileDialog.Notifications -> NotificationsDialog(
			options = NotificationTimeSlots.options,
			selected = notificationDraft,
			onToggle = { slot ->
				val next = notificationDraft.toMutableSet()
				if (slot.time in next) {
					next.remove(slot.time)
				} else {
					next.add(slot.time)
				}
				onNotificationDraftChange(next)
			},
			onConfirm = {
				component.handleUpdateNotificationTimes(times = notificationDraft.toList())
				onOpenDialogChange(null)
			},
			onDismiss = { onOpenDialogChange(null) },
		)
		ProfileDialog.Theme -> ThemeDialog(
			selected = themeMode,
			onSelect = component::handleSetThemeMode,
			onConfirm = { onOpenDialogChange(null) },
			onDismiss = {
				val initial = themeInitial
				if (initial != null && themeMode != initial) {
					component.handleSetThemeMode(mode = initial)
				}
				onOpenDialogChange(null)
			},
		)
		ProfileDialog.Logout -> LogoutDialog(
			loggingOut = loggingOut,
			onConfirm = component::handleLogout,
			onDismiss = { if (!loggingOut) onOpenDialogChange(null) },
		)
		null -> Unit
	}
}

@Composable
private fun LevelDialog(
	selected: String,
	onConfirm: (String) -> Unit,
	onDismiss: () -> Unit,
) {
	val levelCodes = stringArrayResource(R.array.profile_english_level_codes).toList()
	val levelLabels = stringArrayResource(R.array.profile_english_levels).toList()
	val options = EnglishLevels.codes.map { code ->
		val index = levelCodes.indexOf(code)
		if (index >= 0) levelLabels[index] else code
	}
	val selectedLabel = run {
		val index = levelCodes.indexOf(selected)
		if (index >= 0) levelLabels[index] else selected
	}

	WuiSelectionDialog(
		title = stringResource(R.string.profile_level_dialog_title),
		options = options,
		selectedOption = selectedLabel,
		saveButtonText = stringResource(ComponentR.string.common_ok),
		cancelButtonText = stringResource(ComponentR.string.common_cancel),
		onDismiss = onDismiss,
		onSave = { label ->
			val index = options.indexOf(label)
			if (index >= 0) {
				onConfirm(EnglishLevels.codes[index])
			}
		},
	)
}

@Composable
private fun DailyGoalDialog(
	selected: DailyGoal,
	onConfirm: (DailyGoal) -> Unit,
	onDismiss: () -> Unit,
) {
	val goals = DailyGoals.options
	val options = goals.map { goal ->
		stringResource(R.string.profile_daily_goal_value, goal.wordsPerDay)
	}
	val selectedLabel = stringResource(
		R.string.profile_daily_goal_value,
		selected.wordsPerDay,
	)

	WuiSelectionDialog(
		title = stringResource(R.string.profile_daily_goal_dialog_title),
		options = options,
		selectedOption = selectedLabel,
		saveButtonText = stringResource(ComponentR.string.common_ok),
		cancelButtonText = stringResource(ComponentR.string.common_cancel),
		onDismiss = onDismiss,
		onSave = { label ->
			val index = options.indexOf(label)
			if (index >= 0) {
				onConfirm(goals[index])
			}
		},
	)
}

@Composable
private fun ThemeDialog(
	selected: AppThemeMode,
	onSelect: (AppThemeMode) -> Unit,
	onConfirm: () -> Unit,
	onDismiss: () -> Unit,
) {
	val modes = AppThemeMode.entries
	val options = modes.map { themeModeLabel(mode = it) }
	val selectedLabel = themeModeLabel(mode = selected)

	WuiSelectionDialog(
		title = stringResource(R.string.profile_theme_dialog_title),
		options = options,
		selectedOption = selectedLabel,
		saveButtonText = stringResource(ComponentR.string.common_ok),
		cancelButtonText = stringResource(ComponentR.string.common_cancel),
		onDismiss = onConfirm,
		onCancel = onDismiss,
		onOptionSelected = { label ->
			val index = options.indexOf(label)
			if (index >= 0) {
				onSelect(modes[index])
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
				WuiButton(
					text = stringResource(R.string.profile_logout_confirm),
					onClick = onConfirm,
					loading = loggingOut,
					modifier = Modifier.padding(top = 24.dp),
				)
				WuiButton(
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
