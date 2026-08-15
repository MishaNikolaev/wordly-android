package com.nmichail.wordly.android.features.profile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.nmichail.wordly.android.component.wui.R as ComponentR
import com.nmichail.wordly.android.features.profile.R
import com.nmichail.wordly.android.features.profile.domain.entity.NotificationTimeSlot
import com.nmichail.wordly.android.features.profile.presentation.ProfileComponent

@Composable
fun NotificationsDialog(
	dialog: ProfileComponent.NotificationsDialogState,
	onToggle: (NotificationTimeSlot) -> Unit,
	onConfirm: () -> Unit,
	onDismiss: () -> Unit,
) {
	Dialog(
		onDismissRequest = onDismiss,
		properties = DialogProperties(usePlatformDefaultWidth = false),
	) {
		Surface(
			modifier = Modifier
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
			) {
				Text(
					text = stringResource(R.string.profile_reminder_time_dialog_title),
					style = MaterialTheme.typography.titleLarge,
					fontWeight = FontWeight.Bold,
					color = MaterialTheme.colorScheme.onSurface,
				)
				Column(modifier = Modifier.padding(top = 16.dp)) {
					dialog.options.forEach { slot ->
						NotificationOptionRow(
							time = slot.time,
							checked = slot.time in dialog.selected,
							onClick = { onToggle(slot) },
						)
					}
				}
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(top = 24.dp),
					horizontalArrangement = Arrangement.End,
					verticalAlignment = Alignment.CenterVertically,
				) {
					TextButton(onClick = onDismiss) {
						Text(text = stringResource(ComponentR.string.common_cancel))
					}
					TextButton(onClick = onConfirm) {
						Text(text = stringResource(ComponentR.string.common_save))
					}
				}
			}
		}
	}
}

@Composable
private fun NotificationOptionRow(
	time: String,
	checked: Boolean,
	onClick: () -> Unit,
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			)
			.padding(vertical = 4.dp),
		verticalAlignment = Alignment.CenterVertically,
	) {
		Checkbox(
			checked = checked,
			onCheckedChange = { onClick() },
			modifier = Modifier.size(48.dp),
		)
		Text(
			text = time,
			style = MaterialTheme.typography.bodyLarge,
			color = MaterialTheme.colorScheme.onSurface,
		)
	}
}
