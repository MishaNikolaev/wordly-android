package com.nmichail.wordly.android.features.authorization.signin.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.component.ui.R as ComponentR
import com.nmichail.wordly.android.component.ui.components.AuthBackground
import com.nmichail.wordly.android.component.ui.components.Button
import com.nmichail.wordly.android.component.ui.components.ScreenTitle
import com.nmichail.wordly.android.component.ui.components.SignInAuthHeader
import com.nmichail.wordly.android.component.ui.components.TextField
import com.nmichail.wordly.android.component.ui.components.TextLink
import com.nmichail.wordly.android.component.ui.components.snackbar.SnackBarHost
import com.nmichail.wordly.android.component.ui.components.snackbar.showErrorSnackBar
import com.nmichail.wordly.android.component.ui.validation.emailErrorMessage
import com.nmichail.wordly.android.component.ui.validation.passwordErrorMessage
import com.nmichail.wordly.android.core.validation.DefaultValidationState
import com.nmichail.wordly.android.features.authorization.signin.R
import com.nmichail.wordly.android.features.authorization.signin.presentation.SignInComponent
import com.nmichail.wordly.android.features.authorization.signin.presentation.areFieldsValid

@Composable
fun SignInContent(
	component: SignInComponent,
	devEnabled: Boolean,
	modifier: Modifier = Modifier,
) {
	val state by component.model.subscribeAsState()
	val snackBarHostState = remember { SnackbarHostState() }
	val context = LocalContext.current

	LaunchedEffect(state.error) {
		val error = state.error ?: return@LaunchedEffect
		val message = when (error) {
			SignInComponent.Error.InvalidCredentials -> context.getString(R.string.sign_in_invalid_credentials)
			SignInComponent.Error.NoConnection -> context.getString(R.string.sign_in_no_connection)
			SignInComponent.Error.Unknown -> context.getString(R.string.sign_in_unknown_error)
		}
		showErrorSnackBar(
			snackBarHostState = snackBarHostState,
			message = message,
		)
		component.handleErrorShown()
	}

	Scaffold(
		modifier = modifier.fillMaxSize(),
		containerColor = Color.Transparent,
		contentWindowInsets = WindowInsets(0, 0, 0, 0),
		snackbarHost = { SnackBarHost(snackBarHostState = snackBarHostState) },
	) {
		AuthBackground(
			modifier = Modifier.fillMaxSize(),
			header = {
				Column(modifier = Modifier.padding(horizontal = 20.dp)) {
					SignInAuthHeader()
				}
			},
			content = {
				Column(
					modifier = Modifier
						.padding(horizontal = 20.dp)
						.padding(bottom = 32.dp),
				) {
					SignInForm(
						state = state,
						component = component,
						devEnabled = devEnabled,
					)
				}
			},
		)
	}
}

@Composable
private fun SignInForm(
	state: SignInComponent.State,
	component: SignInComponent,
	devEnabled: Boolean,
) {
	ScreenTitle(
		title = stringResource(R.string.sign_in_title),
		modifier = Modifier.padding(top = 16.dp),
	)

	TextField(
		label = stringResource(ComponentR.string.common_label_email),
		value = state.email.data,
		onValueChange = component::handleChangeEmail,
		keyboardType = KeyboardType.Email,
		errorVisible = state.email.validationState is DefaultValidationState.Invalid,
		errorMessage = stringResource(id = emailErrorMessage(state = state.email.validationState)),
		modifier = Modifier.padding(top = 24.dp),
	)

	TextField(
		label = stringResource(ComponentR.string.common_label_password),
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
		enabled = !state.isSubmitting,
		modifier = Modifier.padding(top = 8.dp),
	)

	Button(
		text = stringResource(R.string.sign_in_submit),
		onClick = component::handleSubmit,
		enabled = state.areFieldsValid(),
		loading = state.isSubmitting,
		modifier = Modifier.padding(top = 24.dp),
	)

	TextLink(
		text = stringResource(R.string.sign_in_no_account),
		onClick = component::handleNavigateToSignUp,
		enabled = !state.isSubmitting,
		modifier = Modifier
			.fillMaxWidth()
			.padding(top = 20.dp),
	)
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
