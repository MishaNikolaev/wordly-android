package com.nmichail.wordly.android.features.authorization.signup.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.R as ComponentR
import com.nmichail.wordly.android.component.ui.components.AuthBackground
import com.nmichail.wordly.android.component.ui.components.CaptionText
import com.nmichail.wordly.android.component.ui.components.SelectionField
import com.nmichail.wordly.android.component.ui.components.Button
import com.nmichail.wordly.android.component.ui.components.ScreenTitle
import com.nmichail.wordly.android.component.ui.components.SignUpAuthHeader
import com.nmichail.wordly.android.component.ui.components.TextField
import com.nmichail.wordly.android.component.ui.validation.emailErrorMessage
import com.nmichail.wordly.android.component.ui.validation.nameErrorMessage
import com.nmichail.wordly.android.component.ui.validation.notEmptyErrorMessage
import com.nmichail.wordly.android.component.ui.validation.passwordErrorMessage
import com.nmichail.wordly.android.core.validation.DefaultValidationState
import com.nmichail.wordly.android.core.validation.name.NamePart
import com.nmichail.wordly.android.core.validation.notEmpty.NotEmptyValidationState
import com.nmichail.wordly.android.features.authorization.signup.R
import com.nmichail.wordly.android.features.authorization.signup.presentation.SignUpComponent
import com.nmichail.wordly.android.features.authorization.signup.presentation.SignUpStore

@Composable
fun SignUpContent(
	component: SignUpComponent,
	modifier: Modifier = Modifier,
) {
	val state by component.model.collectAsState()

	AuthBackground(
		modifier = modifier,
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

@Composable
private fun SignUpForm(
	state: SignUpStore.State,
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
		onValueChange = component::onEmailChanged,
		keyboardType = KeyboardType.Email,
		errorVisible = state.email.validationState is DefaultValidationState.Invalid,
		errorMessage = stringResource(id = emailErrorMessage(state = state.email.validationState)),
		modifier = Modifier.padding(top = 24.dp),
	)
	TextField(
		label = stringResource(ComponentR.string.common_label_password),
		value = state.password.data,
		onValueChange = component::onPasswordChanged,
		isPassword = true,
		errorVisible = state.password.validationState is DefaultValidationState.Invalid,
		errorMessage = stringResource(id = passwordErrorMessage(state = state.password.validationState)),
		modifier = Modifier.padding(top = 16.dp),
	)

	SignUpNameFields(state = state, component = component)
	SignUpEnglishLevelField(state = state, component = component)

	Button(
		text = stringResource(R.string.sign_up_submit),
		onClick = component::onSubmitClicked,
		// TODO: Убрать потом это — enabled = state.areFieldsValid()
		enabled = true,
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
	state: SignUpStore.State,
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
			onValueChange = component::onFirstNameChanged,
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
			onValueChange = component::onLastNameChanged,
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
	state: SignUpStore.State,
	component: SignUpComponent,
) {
	val englishLevels = stringArrayResource(R.array.sign_up_english_levels).toList()

	SelectionField(
		label = stringResource(R.string.sign_up_english_level_label),
		value = state.englishLevel.data,
		options = englishLevels,
		onValueChange = component::onEnglishLevelChanged,
		errorVisible = state.englishLevel.validationState is NotEmptyValidationState.Invalid,
		errorMessage = stringResource(id = notEmptyErrorMessage(state = state.englishLevel.validationState)),
		modifier = Modifier.padding(top = 16.dp),
	)
}