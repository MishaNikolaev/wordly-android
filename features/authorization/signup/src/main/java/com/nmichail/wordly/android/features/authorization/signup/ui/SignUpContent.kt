package com.nmichail.wordly.android.features.authorization.signup.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.shared.authorization.AuthBackground
import com.nmichail.wordly.android.component.wui.components.button.WuiButton
import com.nmichail.wordly.android.component.wui.components.text.WuiLinkedText
import com.nmichail.wordly.android.component.wui.components.text.WuiScreenTitle
import com.nmichail.wordly.android.component.wui.components.field.WuiSelectionField
import com.nmichail.wordly.android.features.authorization.signup.ui.component.SignUpAuthHeader
import com.nmichail.wordly.android.component.wui.components.field.WuiTextField
import com.nmichail.wordly.android.component.wui.components.button.WuiTextLink
import com.nmichail.wordly.android.component.wui.components.snackbar.WuiSnackBarHost
import com.nmichail.wordly.android.component.wui.components.snackbar.showWuiErrorSnackBar
import com.nmichail.wordly.android.core.validation.ui.emailErrorMessage
import com.nmichail.wordly.android.core.validation.ui.nameErrorMessage
import com.nmichail.wordly.android.core.validation.ui.notEmptyErrorMessage
import com.nmichail.wordly.android.core.validation.ui.passwordErrorMessage
import com.nmichail.wordly.android.core.validation.DefaultValidationState
import com.nmichail.wordly.android.core.validation.name.NamePart
import com.nmichail.wordly.android.core.validation.notEmpty.NotEmptyValidationState
import com.nmichail.wordly.android.features.authorization.signup.R
import com.nmichail.wordly.android.features.authorization.signup.presentation.SignUpComponent
import com.nmichail.wordly.android.features.authorization.signup.presentation.areFieldsValid

@Composable
fun SignUpContent(
	component: SignUpComponent,
	modifier: Modifier = Modifier,
) {
	val state by component.model.subscribeAsState()
	val snackBarHostState = remember { SnackbarHostState() }
	val context = LocalContext.current

	LaunchedEffect(state.error) {
		val error = state.error ?: return@LaunchedEffect
		val message = when (error) {
			SignUpComponent.Error.RegistrationFailed -> context.getString(R.string.sign_up_registration_error)
			SignUpComponent.Error.NoConnection -> context.getString(R.string.sign_up_no_connection)
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
			header = { SignUpAuthHeader() },
			content = {
				SignUpForm(
					state = state,
					component = component,
					modifier = Modifier
						.padding(horizontal = 20.dp)
						.padding(top = 28.dp, bottom = 32.dp),
				)
			},
		)
	}
}

@Composable
private fun SignUpForm(
	state: SignUpComponent.State,
	component: SignUpComponent,
	modifier: Modifier = Modifier,
) {
	Column(modifier = modifier.fillMaxWidth()) {
		WuiScreenTitle(
			title = stringResource(R.string.sign_up_title),
			subtitle = stringResource(R.string.sign_up_subtitle),
			textAlign = TextAlign.Start,
		)

		WuiTextField(
			label = stringResource(R.string.sign_up_label_email),
			value = state.email.data,
			onValueChange = component::handleChangeEmail,
			keyboardType = KeyboardType.Email,
			errorVisible = state.email.validationState is DefaultValidationState.Invalid,
			errorMessage = stringResource(id = emailErrorMessage(state = state.email.validationState)),
			modifier = Modifier.padding(top = 24.dp),
		)
		WuiTextField(
			label = stringResource(R.string.sign_up_label_password),
			value = state.password.data,
			onValueChange = component::handleChangePassword,
			isPassword = true,
			errorVisible = state.password.validationState is DefaultValidationState.Invalid,
			errorMessage = stringResource(id = passwordErrorMessage(state = state.password.validationState)),
			modifier = Modifier.padding(top = 16.dp),
		)

		SignUpNameFields(state = state, component = component)
		SignUpEnglishLevelField(state = state, component = component)

		WuiButton(
			text = stringResource(R.string.sign_up_submit),
			onClick = component::handleSubmit,
			enabled = state.areFieldsValid(),
			loading = state.isSubmitting,
			modifier = Modifier.padding(top = 24.dp),
		)
		WuiLinkedText(
			text = stringResource(R.string.sign_up_terms),
			linkText = stringResource(R.string.sign_up_terms_link),
			onLinkClick = { component.handleOpenTermsOfUse() },
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 20.dp),
		)
		WuiTextLink(
			text = stringResource(R.string.sign_up_has_account),
			onClick = component::handleNavigateToSignIn,
			enabled = !state.isSubmitting,
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 4.dp),
		)
	}
}

@Composable
private fun SignUpNameFields(
	state: SignUpComponent.State,
	component: SignUpComponent,
) {
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(top = 16.dp),
	) {
		WuiTextField(
			label = stringResource(R.string.sign_up_first_name_label),
			value = state.firstName.data,
			onValueChange = component::handleChangeFirstName,
			errorVisible = state.firstName.validationState is DefaultValidationState.Invalid,
			errorMessage = stringResource(
				id = nameErrorMessage(
					state = state.firstName.validationState,
					namePart = NamePart.NAME,
				),
			),
			modifier = Modifier
				.weight(1f)
				.padding(end = 8.dp),
		)
		WuiTextField(
			label = stringResource(R.string.sign_up_last_name_label),
			value = state.lastName.data,
			onValueChange = component::handleChangeLastName,
			errorVisible = state.lastName.validationState is DefaultValidationState.Invalid,
			errorMessage = stringResource(
				id = nameErrorMessage(
					state = state.lastName.validationState,
					namePart = NamePart.SURNAME,
				),
			),
			modifier = Modifier
				.weight(1f)
				.padding(start = 8.dp),
		)
	}
}

@Composable
private fun SignUpEnglishLevelField(
	state: SignUpComponent.State,
	component: SignUpComponent,
) {
	val englishLevelCodes = stringArrayResource(R.array.sign_up_english_level_codes).toList()
	val englishLevelLabels = stringArrayResource(R.array.sign_up_english_levels).toList()
	val selectedLabel = englishLevelLabels.getOrElse(englishLevelCodes.indexOf(state.englishLevel.data)) { "" }

	WuiSelectionField(
		label = stringResource(R.string.sign_up_english_level_label),
		value = selectedLabel,
		options = englishLevelLabels,
		onValueChange = { label ->
			val index = englishLevelLabels.indexOf(label)
			if (index >= 0) {
				component.handleChangeEnglishLevel(englishLevelCodes[index])
			}
		},
		errorVisible = state.englishLevel.validationState is NotEmptyValidationState.Invalid,
		errorMessage = stringResource(id = notEmptyErrorMessage(state = state.englishLevel.validationState)),
		modifier = Modifier.padding(top = 16.dp),
	)
}
