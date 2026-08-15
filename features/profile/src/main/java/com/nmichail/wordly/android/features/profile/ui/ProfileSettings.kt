package com.nmichail.wordly.android.features.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.wui.theme.OnestFontFamily
import com.nmichail.wordly.android.features.profile.R
import com.nmichail.wordly.android.features.profile.presentation.ProfileComponent

private val SettingsCardShape = RoundedCornerShape(20.dp)

@Composable
internal fun ProfileSettingsSection(
	state: ProfileComponent.State.Content,
	component: ProfileComponent,
	modifier: Modifier = Modifier,
) {
	val profile = state.profile
	val reminderValue = profile.notificationTimes
		.map { it.time }
		.sorted()
		.joinToString(separator = ", ")
		.ifBlank { stringResource(R.string.profile_reminder_time_empty) }

	Column(modifier = modifier.fillMaxWidth()) {
		ProfileSettingsCard {
			ProfileSettingsToggleRow(
				title = stringResource(R.string.profile_notifications),
				checked = state.notificationsEnabled,
				onCheckedChange = { component.handleToggleNotificationsEnabled() },
			)
			SettingsDivider()
			ProfileSettingsNavRow(
				title = stringResource(R.string.profile_reminder_time),
				value = reminderValue,
				onClick = component::handleOpenNotifications,
			)
			SettingsDivider()
			ProfileSettingsNavRow(
				title = stringResource(R.string.profile_daily_goal),
				value = stringResource(
					R.string.profile_daily_goal_value,
					profile.dailyGoal.wordsPerDay,
				),
				onClick = component::handleOpenDailyGoal,
			)
		}
		Text(
			text = stringResource(R.string.profile_appearance_section),
			style = MaterialTheme.typography.titleMedium.copy(
				fontFamily = OnestFontFamily,
				fontWeight = FontWeight.SemiBold,
			),
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			modifier = Modifier.padding(top = 28.dp, bottom = 12.dp),
		)
		ProfileSettingsCard {
			ProfileSettingsNavRow(
				title = stringResource(R.string.profile_theme),
				value = themeModeLabel(mode = state.themeMode),
				onClick = component::handleOpenTheme,
			)
		}
	}
}

@Composable
private fun ProfileSettingsCard(
	content: @Composable ColumnScope.() -> Unit,
) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.clip(SettingsCardShape)
			.border(
				width = 1.dp,
				color = MaterialTheme.colorScheme.outline,
				shape = SettingsCardShape,
			)
			.background(MaterialTheme.colorScheme.surface)
			.padding(horizontal = 16.dp),
		content = content,
	)
}

@Composable
private fun SettingsDivider() {
	HorizontalDivider(
		color = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
	)
}

@Composable
private fun ProfileSettingsToggleRow(
	title: String,
	checked: Boolean,
	onCheckedChange: (Boolean) -> Unit,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.padding(vertical = 12.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(12.dp),
	) {
		Text(
			text = title,
			style = MaterialTheme.typography.bodyLarge,
			color = MaterialTheme.colorScheme.onBackground,
			modifier = Modifier.weight(1f),
		)
		Switch(
			checked = checked,
			onCheckedChange = onCheckedChange,
			colors = SwitchDefaults.colors(
				checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
				checkedTrackColor = MaterialTheme.colorScheme.primary,
				uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
				uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
			),
		)
	}
}

@Composable
private fun ProfileSettingsNavRow(
	title: String,
	value: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier
			.fillMaxWidth()
			.clickable(onClick = onClick)
			.padding(vertical = 14.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		Text(
			text = title,
			style = MaterialTheme.typography.bodyLarge,
			color = MaterialTheme.colorScheme.onBackground,
			modifier = Modifier.weight(1f),
		)
		Text(
			text = value,
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			modifier = Modifier.padding(end = 2.dp),
		)
		Icon(
			imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
			contentDescription = null,
			tint = MaterialTheme.colorScheme.onSurfaceVariant,
		)
	}
}
