package com.nmichail.wordly.android.features.authorization.signup.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.component.ui.R as ComponentR
import com.nmichail.wordly.android.component.ui.components.AuthBackground
import com.nmichail.wordly.android.component.ui.components.CaptionText
import com.nmichail.wordly.android.component.ui.components.SelectionField
import com.nmichail.wordly.android.component.ui.components.Button
import com.nmichail.wordly.android.component.ui.components.ScreenTitle
import com.nmichail.wordly.android.component.ui.components.SignUpAuthHeader
import com.nmichail.wordly.android.component.ui.components.TextField
import com.nmichail.wordly.android.component.ui.components.snackbar.SnackBarHost
import com.nmichail.wordly.android.component.ui.components.snackbar.showErrorSnackBar
import com.nmichail.wordly.android.component.ui.validation.emailErrorMessage
import com.nmichail.wordly.android.component.ui.validation.nameErrorMessage
import com.nmichail.wordly.android.component.ui.validation.notEmptyErrorMessage
import com.nmichail.wordly.android.component.ui.validation.passwordErrorMessage
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

	LaunchedEffect(component) {
		for (label in component.labelsChannel()) {
			when (label) {
				SignUpComponent.Label.ShowRegistrationError -> {
					showErrorSnackBar(
						snackBarHostState = snackBarHostState,
						message = context.getString(R.string.sign_up_registration_error),
					)
				}
				SignUpComponent.Label.ShowNoConnection -> {
					showErrorSnackBar(
						snackBarHostState = snackBarHostState,
						message = context.getString(R.string.sign_up_no_connection),
					)
				}
				SignUpComponent.Label.ShowUnknownError -> {
					showErrorSnackBar(
						snackBarHostState = snackBarHostState,
						message = context.getString(R.string.sign_up_unknown_error),
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
					SignUpAuthHeader()
				}
			},
			content = {
				Column(
					modifier = Modifier
						.padding(horizontal = 20.dp)
						.padding(bottom = 32.dp),
				) {
					SignUpForm(state = state, component = component)
				}
			},
		)
	}
}

@Composable
private fun SignUpForm(
	state: SignUpComponent.State,
	component: SignUpComponent,
) {
	ScreenTitle(
		title = stringResource(R.string.sign_up_title),
		subtitle = stringResource(R.string.sign_up_subtitle),
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

	SignUpNameFields(state = state, component = component)
	SignUpEnglishLevelField(state = state, component = component)

	Button(
		text = stringResource(R.string.sign_up_submit),
		onClick = component::handleSubmit,
		enabled = state.areFieldsValid(),
		modifier = Modifier.padding(top = 24.dp),
	)
	CaptionText(
		text = stringResource(R.string.sign_up_terms),
		modifier = Modifier
			.fillMaxWidth()
			.padding(top = 20.dp),
	)
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
		TextField(
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
		TextField(
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
	val englishLevels = stringArrayResource(R.array.sign_up_english_levels).toList()

	SelectionField(
		label = stringResource(R.string.sign_up_english_level_label),
		value = state.englishLevel.data,
		options = englishLevels,
		onValueChange = component::handleChangeEnglishLevel,
		errorVisible = state.englishLevel.validationState is NotEmptyValidationState.Invalid,
		errorMessage = stringResource(id = notEmptyErrorMessage(state = state.englishLevel.validationState)),
		modifier = Modifier.padding(top = 16.dp),
	)
}
