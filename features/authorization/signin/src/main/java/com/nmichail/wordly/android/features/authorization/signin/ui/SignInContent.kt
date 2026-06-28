package com.nmichail.wordly.android.features.authorization.signin.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.R as ComponentR
import com.nmichail.wordly.android.component.ui.components.AuthBackground
import com.nmichail.wordly.android.component.ui.components.Button
import com.nmichail.wordly.android.component.ui.components.ScreenTitle
import com.nmichail.wordly.android.component.ui.components.SignInAuthHeader
import com.nmichail.wordly.android.component.ui.components.TextField
import com.nmichail.wordly.android.component.ui.components.TextLink
import com.nmichail.wordly.android.features.authorization.signin.R
import com.nmichail.wordly.android.features.authorization.signin.presentation.SignInComponent
import com.nmichail.wordly.android.features.authorization.signin.presentation.SignInStore

@Composable
fun SignInContent(
	component: SignInComponent,
	modifier: Modifier = Modifier,
) {
	val state by component.model.collectAsState()

	AuthBackground(
		modifier = modifier,
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
				SignInForm(state = state, component = component)
			}
		},
	)
}

@Composable
private fun SignInForm(
	state: SignInStore.State,
	component: SignInComponent,
) {
	ScreenTitle(
		title = stringResource(R.string.sign_in_title),
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
	Box(
		modifier = Modifier
			.fillMaxWidth()
			.padding(top = 8.dp),
		contentAlignment = Alignment.CenterEnd,
	) {
		TextLink(
			text = stringResource(R.string.sign_in_forgot_password),
			onClick = {},
			textAlign = TextAlign.End,
			style = MaterialTheme.typography.labelMedium,
		)
	}
	Button(
		text = stringResource(R.string.sign_in_submit),
		onClick = component::onSubmitClicked,
		modifier = Modifier.padding(top = 24.dp),
	)
	TextLink(
		text = stringResource(R.string.sign_in_no_account),
		onClick = component::onSignUpClicked,
		modifier = Modifier
			.fillMaxWidth()
			.padding(top = 20.dp),
	)
}