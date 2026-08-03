package com.nmichail.wordly.android.component.ui.components.field

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.R
import com.nmichail.wordly.android.component.ui.theme.WordlyAndroidTheme
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.nmichail.wordly.android.component.ui.theme.PreviewTheme
import com.nmichail.wordly.android.component.ui.theme.PreviewThemeProvider
import com.nmichail.wordly.android.component.ui.theme.WordlyPreviews

@Composable
fun CustomTextField(
	label: String,
	value: String,
	onValueChange: (String) -> Unit,
	modifier: Modifier = Modifier,
	isPassword: Boolean = false,
	readOnly: Boolean = false,
	enabled: Boolean = true,
	keyboardType: KeyboardType = KeyboardType.Text,
	errorVisible: Boolean = false,
	errorMessage: String = "",
) {
	var passwordVisible by remember { mutableStateOf(false) }

	Column(modifier = modifier.fillMaxWidth()) {
		Text(
			text = label,
			style = MaterialTheme.typography.labelMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			modifier = Modifier.padding(bottom = 8.dp),
		)
		OutlinedTextField(
			value = value,
			onValueChange = onValueChange,
			modifier = Modifier.fillMaxWidth(),
			enabled = enabled,
			readOnly = readOnly,
			trailingIcon = passwordTrailingIcon(
				isPassword = isPassword,
				passwordVisible = passwordVisible,
				onToggle = { passwordVisible = !passwordVisible },
			),
			shape = MaterialTheme.shapes.extraSmall,
			singleLine = true,
			isError = errorVisible,
			supportingText = fieldErrorText(errorMessage),
			visualTransformation = passwordVisualTransformation(
				isPassword = isPassword,
				passwordVisible = passwordVisible,
			),
			keyboardOptions = KeyboardOptions(
				keyboardType = if (isPassword) KeyboardType.Password else keyboardType,
			),
			colors = customTextFieldColors(),
		)
	}
}

@Composable
private fun passwordTrailingIcon(
	isPassword: Boolean,
	passwordVisible: Boolean,
	onToggle: () -> Unit,
): (@Composable () -> Unit)? =
	if (isPassword) {
		{
			PasswordVisibilityToggle(
				visible = passwordVisible,
				onToggle = onToggle,
			)
		}
	} else {
		null
	}

private fun passwordVisualTransformation(
	isPassword: Boolean,
	passwordVisible: Boolean,
): VisualTransformation =
	if (isPassword && !passwordVisible) {
		PasswordVisualTransformation()
	} else {
		VisualTransformation.None
	}

@Composable
private fun PasswordVisibilityToggle(
	visible: Boolean,
	onToggle: () -> Unit,
) {
	IconButton(onClick = onToggle) {
		Icon(
			imageVector = if (visible) {
				Icons.Outlined.Visibility
			} else {
				Icons.Outlined.VisibilityOff
			},
			contentDescription = stringResource(
				if (visible) {
					R.string.common_password_hide
				} else {
					R.string.common_password_show
				},
			),
			tint = MaterialTheme.colorScheme.onSurfaceVariant,
		)
	}
}

@Composable
private fun customTextFieldColors(): TextFieldColors =
	OutlinedTextFieldDefaults.colors(
		focusedContainerColor = MaterialTheme.colorScheme.surface,
		unfocusedContainerColor = MaterialTheme.colorScheme.surface,
		disabledContainerColor = MaterialTheme.colorScheme.surface,
		focusedBorderColor = MaterialTheme.colorScheme.primary,
		unfocusedBorderColor = MaterialTheme.colorScheme.outline,
		errorBorderColor = MaterialTheme.colorScheme.error,
		errorSupportingTextColor = MaterialTheme.colorScheme.error,
	)

@Composable
private fun fieldErrorText(errorMessage: String): (@Composable () -> Unit)? =
	if (errorMessage.isNotEmpty()) {
		{
			Text(
				text = errorMessage,
				color = MaterialTheme.colorScheme.error,
			)
		}
	} else {
		null
	}

@WordlyPreviews
@Composable
private fun CustomTextFieldPreview(
	@PreviewParameter(PreviewThemeProvider::class) theme: PreviewTheme,
) {
	WordlyAndroidTheme(darkTheme = theme == PreviewTheme.Dark) {
		Column(
			modifier = Modifier.padding(16.dp),
			verticalArrangement = Arrangement.spacedBy(12.dp),
		) {
			CustomTextField(label = "Email", value = "demo@wordly.app", onValueChange = {})
			CustomTextField(label = "Password", value = "secret", onValueChange = {}, isPassword = true)
			CustomTextField(
				label = "Name",
				value = "",
				onValueChange = {},
				errorVisible = true,
				errorMessage = "Обязательное поле",
			)
		}
	}
}