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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.shared.authorization.AuthBackground
import com.nmichail.wordly.android.component.wui.components.button.WuiButton
import com.nmichail.wordly.android.component.wui.components.text.WuiScreenTitle
import com.nmichail.wordly.android.features.authorization.signin.ui.component.SignInAuthHeader
import com.nmichail.wordly.android.component.wui.components.field.WuiTextField
import com.nmichail.wordly.android.component.wui.components.button.WuiTextLink
import com.nmichail.wordly.android.component.wui.components.snackbar.WuiSnackBarHost
import com.nmichail.wordly.android.component.wui.components.snackbar.showWuiErrorSnackBar
import com.nmichail.wordly.android.core.validation.ui.emailErrorMessage
import com.nmichail.wordly.android.core.validation.ui.passwordErrorMessage
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
		showWuiErrorSnackBar(
			snackBarHostState = snackBarHostState,
			message = message,
		)
		component.handleErrorShown()
	}

	Scaffold(
		modifier = modifier.fillMaxSize(),
		containerColor = Color.Transparent,
		contentWindowInsets = WindowInsets(0, 0, 0, 0),
		snackbarHost = { WuiSnackBarHost(snackBarHostState = snackBarHostState) },
	) {
		AuthBackground(
			modifier = Modifier.fillMaxSize(),
			header = { SignInAuthHeader() },
			content = {
				SignInForm(
					state = state,
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

@Composable
private fun SignInForm(
	state: SignInComponent.State,
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
			enabled = !state.isSubmitting,
			modifier = Modifier.padding(top = 8.dp),
		)

		WuiButton(
			text = stringResource(R.string.sign_in_submit),
			onClick = component::handleSubmit,
			enabled = state.areFieldsValid(),
			loading = state.isSubmitting,
			modifier = Modifier.padding(top = 24.dp),
		)

		WuiTextLink(
			text = stringResource(R.string.sign_in_no_account),
			onClick = component::handleNavigateToSignUp,
			enabled = !state.isSubmitting,
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
