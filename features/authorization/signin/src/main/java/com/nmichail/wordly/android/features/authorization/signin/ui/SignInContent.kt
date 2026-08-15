package com.nmichail.wordly.android.features.authorization.signin.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.component.wui.components.button.WuiButton
import com.nmichail.wordly.android.component.wui.components.button.WuiTextLink
import com.nmichail.wordly.android.component.wui.components.field.WuiTextField
import com.nmichail.wordly.android.component.wui.components.text.WuiScreenTitle
import com.nmichail.wordly.android.core.validation.DefaultValidationState
import com.nmichail.wordly.android.core.validation.ui.emailErrorMessage
import com.nmichail.wordly.android.core.validation.ui.passwordErrorMessage
import com.nmichail.wordly.android.features.authorization.signin.R
import com.nmichail.wordly.android.features.authorization.signin.presentation.SignInComponent
import com.nmichail.wordly.android.features.authorization.signin.presentation.SignInStore
import com.nmichail.wordly.android.features.authorization.signin.presentation.areFieldsValid
import com.nmichail.wordly.android.features.authorization.signin.ui.component.SignInAuthHeader
import com.nmichail.wordly.android.shared.authorization.AuthBackground

@Composable
fun SignInContent(
	component: SignInComponent,
	devEnabled: Boolean,
	modifier: Modifier = Modifier,
) {
	val state by component.model.subscribeAsState()

	when (val currentState = state) {
		SignInStore.State.Loading -> {
			Box(
				modifier = modifier.fillMaxSize(),
				contentAlignment = Alignment.Center,
			) {
				CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
			}
		}
		is SignInStore.State.Error -> {
			SignInError(
				onRetryClick = component::handleRetry,
				modifier = modifier.fillMaxSize(),
			)
		}
		is SignInStore.State.Content -> {
			Scaffold(
				modifier = modifier.fillMaxSize(),
				containerColor = Color.Transparent,
				contentWindowInsets = WindowInsets(0, 0, 0, 0),
			) {
				AuthBackground(
					modifier = Modifier.fillMaxSize(),
					header = { SignInAuthHeader() },
					content = {
						SignInForm(
							state = currentState,
							component = component,
							devEnabled = devEnabled,
							modifier = Modifier
								.padding(horizontal = 20.dp)
								.padding(top = 28.dp, bottom = 32.dp),
						)
					},
				)
			}
		}
	}
}

@Composable
private fun SignInError(
	onRetryClick: () -> Unit,
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
			text = stringResource(R.string.sign_in_error_title),
			style = MaterialTheme.typography.titleMedium,
			color = MaterialTheme.colorScheme.onSurface,
			textAlign = TextAlign.Center,
		)
		Text(
			text = stringResource(R.string.sign_in_error_description),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(top = 8.dp),
		)
		WuiButton(
			text = stringResource(R.string.sign_in_retry),
			onClick = onRetryClick,
			modifier = Modifier.padding(top = 24.dp),
		)
	}
}

@Composable
private fun SignInForm(
	state: SignInStore.State.Content,
	component: SignInComponent,
	devEnabled: Boolean,
	modifier: Modifier = Modifier,
) {
	Column(modifier = modifier.fillMaxWidth()) {
		WuiScreenTitle(
			title = stringResource(R.string.sign_in_title),
			subtitle = stringResource(R.string.sign_in_subtitle),
			textAlign = TextAlign.Start,
		)

		WuiTextField(
			label = stringResource(R.string.sign_in_label_email),
			value = state.email.data,
			onValueChange = component::handleChangeEmail,
			keyboardType = KeyboardType.Email,
			errorVisible = state.email.validationState is DefaultValidationState.Invalid,
			errorMessage = stringResource(id = emailErrorMessage(state = state.email.validationState)),
			modifier = Modifier.padding(top = 24.dp),
		)

		WuiTextField(
			label = stringResource(R.string.sign_in_label_password),
			value = state.password.data,
			onValueChange = component::handleChangePassword,
			isPassword = true,
			errorVisible = state.password.validationState is DefaultValidationState.Invalid,
			errorMessage = stringResource(id = passwordErrorMessage(state = state.password.validationState)),
			modifier = Modifier.padding(top = 16.dp),
		)

		DevSettingsRow(
			component = component,
			devEnabled = devEnabled,
			enabled = !state.submitting,
			modifier = Modifier.padding(top = 8.dp),
		)

		WuiButton(
			text = stringResource(R.string.sign_in_submit),
			onClick = component::handleSubmit,
			enabled = state.areFieldsValid(),
			loading = state.submitting,
			modifier = Modifier.padding(top = 24.dp),
		)

		WuiTextLink(
			text = stringResource(R.string.sign_in_no_account),
			onClick = component::handleNavigateToSignUp,
			enabled = !state.submitting,
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 12.dp),
		)
	}
}

@Composable
private fun DevSettingsRow(
	component: SignInComponent,
	devEnabled: Boolean,
	enabled: Boolean,
	modifier: Modifier = Modifier,
) {
	if (!devEnabled) return

	Box(
		modifier = modifier.fillMaxWidth(),
	) {
		IconButton(
			onClick = component::handleNavigateToNetworkSelection,
			enabled = enabled,
			modifier = Modifier.align(Alignment.CenterEnd),
		) {
			Icon(
				imageVector = Icons.Outlined.Settings,
				contentDescription = stringResource(R.string.sign_in_network_selection),
				modifier = Modifier.size(28.dp),
				tint = MaterialTheme.colorScheme.onSurfaceVariant,
			)
		}
	}
}