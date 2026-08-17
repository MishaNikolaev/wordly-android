package com.nmichail.wordly.android.features.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.component.wui.components.button.WuiButton
import com.nmichail.wordly.android.features.profile.R
import com.nmichail.wordly.android.features.profile.domain.entity.NotificationTimeSlots
import com.nmichail.wordly.android.features.profile.presentation.reminder.ReminderTimesComponent
import com.nmichail.wordly.android.features.profile.presentation.reminder.ReminderTimesStore

@Composable
fun ReminderTimesContent(
    component: ReminderTimesComponent,
    modifier: Modifier = Modifier,
) {
    val state by component.model.subscribeAsState()

    when (val uiState = state) {
        ReminderTimesStore.State.Initial,
        ReminderTimesStore.State.Loading -> ReminderTimesLoading(modifier = modifier)

        is ReminderTimesStore.State.Content -> ReminderTimesLoaded(
            state = uiState,
            component = component,
            modifier = modifier,
        )

        ReminderTimesStore.State.Error -> ReminderTimesError(
            onRetryClick = component::handleRetry,
            onBackClick = component::handleBack,
            modifier = modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun ReminderTimesLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun ReminderTimesError(
    onRetryClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
			.background(MaterialTheme.colorScheme.background)
			.padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = stringResource(R.string.profile_error_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
        WuiButton(
            text = stringResource(R.string.profile_retry),
            onClick = onRetryClick,
            modifier = Modifier
				.fillMaxWidth()
				.padding(top = 24.dp),
        )
        WuiButton(
            text = stringResource(R.string.profile_edit_back),
            onClick = onBackClick,
            modifier = Modifier
				.fillMaxWidth()
				.padding(top = 12.dp),
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun ReminderTimesLoaded(
    state: ReminderTimesStore.State.Content,
    component: ReminderTimesComponent,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.padding(horizontal = 20.dp)
			.padding(top = 12.dp, bottom = 24.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = stringResource(R.string.profile_edit_back),
                tint = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
					.size(40.dp)
					.clickable(
						role = Role.Button,
						onClick = component::handleBack,
					)
					.padding(8.dp),
            )
            Text(
                text = stringResource(R.string.profile_reminder_time),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(start = 4.dp),
            )
        }
        Column(
            modifier = Modifier
				.weight(1f)
				.fillMaxWidth()
				.verticalScroll(rememberScrollState())
				.padding(top = 24.dp),
        ) {
            NotificationTimeSlots.options.forEach { slot ->
                ReminderTimeRow(
                    time = slot.time,
                    checked = slot.time in state.selectedTimes,
                    enabled = !state.saving,
                    onClick = { component.handleToggleTime(time = slot.time) },
                )
            }
        }
        WuiButton(
            text = stringResource(R.string.profile_edit_save),
            onClick = component::handleSave,
            loading = state.saving,
            modifier = Modifier
				.fillMaxWidth()
				.padding(top = 16.dp),
        )
    }
}

@Composable
private fun ReminderTimeRow(
    time: String,
    checked: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
			.fillMaxWidth()
			.clickable(
				enabled = enabled,
				interactionSource = remember { MutableInteractionSource() },
				indication = null,
				onClick = onClick,
			)
			.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { if (enabled) onClick() },
            enabled = enabled,
            modifier = Modifier.size(48.dp),
        )
        Text(
            text = time,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}