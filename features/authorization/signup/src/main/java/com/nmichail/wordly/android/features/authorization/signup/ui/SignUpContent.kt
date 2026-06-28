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
		value = state.email,
		onValueChange = component::onEmailChanged,
		keyboardType = KeyboardType.Email,
		modifier = Modifier.padding(top = 24.dp),
	)
	TextField(
		label = stringResource(ComponentR.string.common_label_password),
		value = state.password,
		onValueChange = component::onPasswordChanged,
		isPassword = true,
		modifier = Modifier.padding(top = 16.dp),
	)

	SignUpNameFields(state = state, component = component)
	SignUpEnglishLevelField(state = state, component = component)

	Button(
		text = stringResource(R.string.sign_up_submit),
		onClick = component::onSubmitClicked,
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
			value = state.firstName,
			onValueChange = component::onFirstNameChanged,
			modifier = Modifier
				.weight(1f)
				.padding(end = 8.dp),
		)
		TextField(
			label = stringResource(R.string.sign_up_last_name_label),
			value = state.lastName,
			onValueChange = component::onLastNameChanged,
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
		value = state.englishLevel,
		options = englishLevels,
		onValueChange = component::onEnglishLevelChanged,
		modifier = Modifier.padding(top = 16.dp),
	)
}