package com.nmichail.wordly.android.features.authorization.signin.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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

	LaunchedEffect(component) {
		for (label in component.labelsChannel()) {
			when (label) {
				SignInComponent.Label.ShowInvalidCredentials -> {
					showErrorSnackBar(
						snackBarHostState = snackBarHostState,
						message = context.getString(R.string.sign_in_invalid_credentials),
					)
				}
				SignInComponent.Label.ShowNoConnection -> {
					showErrorSnackBar(
						snackBarHostState = snackBarHostState,
						message = context.getString(R.string.sign_in_no_connection),
					)
				}
				SignInComponent.Label.ShowUnknownError -> {
					showErrorSnackBar(
						snackBarHostState = snackBarHostState,
						message = context.getString(R.string.sign_in_unknown_error),
					)
				}
				else -> Unit
			}
		}
	}

	Scaffold(
		modifier = modifier.fillMaxSize(),
		containerColor = Color.Transparent,
		snackbarHost = { SnackBarHost(snackBarHostState = snackBarHostState) },
	) { paddingValues ->
		AuthBackground(
			modifier = Modifier
				.fillMaxSize()
				.padding(paddingValues),
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

	ForgotPasswordRow(
		component = component,
		devEnabled = devEnabled,
		modifier = Modifier.padding(top = 8.dp),
	)

	Button(
		text = stringResource(R.string.sign_in_submit),
		onClick = component::handleSubmit,
		enabled = state.areFieldsValid(),
		modifier = Modifier.padding(top = 24.dp),
	)

	TextLink(
		text = stringResource(R.string.sign_in_no_account),
		onClick = component::handleNavigateToSignUp,
		modifier = Modifier
			.fillMaxWidth()
			.padding(top = 20.dp),
	)
}

@Composable
private fun ForgotPasswordRow(
	component: SignInComponent,
	devEnabled: Boolean,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier.fillMaxWidth(),
	) {
		Row(
			modifier = Modifier.align(Alignment.CenterEnd),
			horizontalArrangement = Arrangement.spacedBy(4.dp),
			verticalAlignment = Alignment.CenterVertically,
		) {
			TextLink(
				text = stringResource(R.string.sign_in_forgot_password),
				onClick = {},
				textAlign = TextAlign.End,
				style = MaterialTheme.typography.labelMedium,
			)

			if (devEnabled) {
				IconButton(onClick = component::handleNavigateToNetworkSelection) {
					Icon(
						imageVector = Icons.Outlined.Settings,
						contentDescription = stringResource(R.string.sign_in_network_selection),
						modifier = Modifier.size(28.dp),
						tint = MaterialTheme.colorScheme.onSurfaceVariant,
					)
				}
			}
		}
	}
}
