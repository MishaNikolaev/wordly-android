package com.nmichail.wordly.android.features.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.component.ui.R as ComponentR
import com.nmichail.wordly.android.component.ui.components.Button
import com.nmichail.wordly.android.component.ui.components.SelectionField
import com.nmichail.wordly.android.component.ui.components.TextField
import com.nmichail.wordly.android.features.profile.R
import com.nmichail.wordly.android.features.profile.presentation.edit.ProfileEditComponent

@Composable
fun ProfileEditContent(
	component: ProfileEditComponent,
	modifier: Modifier = Modifier,
) {
	val state by component.model.subscribeAsState()

	when (val current = state) {
		ProfileEditComponent.State.Loading -> ProfileEditLoading(modifier = modifier)
		ProfileEditComponent.State.Error -> ProfileEditError(
			onRetryClick = component::handleRetry,
			onBackClick = component::handleBack,
			modifier = modifier.fillMaxSize(),
		)
		is ProfileEditComponent.State.Content -> ProfileEditLoaded(
			state = current,
			component = component,
			modifier = modifier,
		)
	}
}

@Composable
private fun ProfileEditLoading(modifier: Modifier = Modifier) {
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
private fun ProfileEditError(
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
		Button(
			text = stringResource(R.string.profile_retry),
			onClick = onRetryClick,
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 24.dp),
		)
		Button(
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
private fun ProfileEditLoaded(
	state: ProfileEditComponent.State.Content,
	component: ProfileEditComponent,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.verticalScroll(rememberScrollState())
			.padding(horizontal = 20.dp)
			.padding(top = 16.dp, bottom = 24.dp),
	) {
		ProfileEditBackButton(onClick = component::handleBack)
		ProfileEditForm(
			state = state,
			component = component,
			modifier = Modifier.padding(top = 20.dp),
		)
		Button(
			text = stringResource(R.string.profile_edit_save),
			onClick = component::handleSave,
			loading = state.saving,
			enabled = state.firstName.isNotBlank() && state.lastName.isNotBlank(),
			modifier = Modifier.padding(top = 32.dp),
		)
	}
}

@Composable
private fun ProfileEditBackButton(
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme
	Box(
		modifier = modifier
			.size(34.dp)
			.clip(RoundedCornerShape(10.dp))
			.background(colorScheme.surfaceVariant)
			.clickable(role = Role.Button, onClick = onClick),
		contentAlignment = Alignment.Center,
	) {
		Icon(
			imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
			contentDescription = stringResource(R.string.profile_edit_back),
			tint = colorScheme.onSurfaceVariant,
			modifier = Modifier.size(20.dp),
		)
	}
}

@Composable
private fun ProfileEditForm(
	state: ProfileEditComponent.State.Content,
	component: ProfileEditComponent,
	modifier: Modifier = Modifier,
) {
	val levelCodes = stringArrayResource(R.array.profile_english_level_codes).toList()
	val levelLabels = stringArrayResource(R.array.profile_english_levels).toList()
	val selectedLevelLabel = levelCodes.indexOf(state.englishLevel)
		.takeIf { it >= 0 }
		?.let { levelLabels[it] }
		?: state.englishLevel

	Column(modifier = modifier) {
		TextField(
			label = stringResource(R.string.profile_first_name_label),
			value = state.firstName,
			onValueChange = component::handleChangeFirstName,
		)
		TextField(
			label = stringResource(R.string.profile_last_name_label),
			value = state.lastName,
			onValueChange = component::handleChangeLastName,
			modifier = Modifier.padding(top = 16.dp),
		)
		TextField(
			label = stringResource(ComponentR.string.common_label_email),
			value = state.email,
			onValueChange = {},
			readOnly = true,
			enabled = false,
			keyboardType = KeyboardType.Email,
			modifier = Modifier.padding(top = 16.dp),
		)
		SelectionField(
			label = stringResource(R.string.profile_english_level_label),
			value = selectedLevelLabel,
			options = levelLabels,
			onValueChange = { label ->
				val index = levelLabels.indexOf(label)
				if (index >= 0) {
					component.handleChangeEnglishLevel(levelCodes[index])
				}
			},
			modifier = Modifier.padding(top = 16.dp),
		)
	}
}
