package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun TextField(
	label: String,
	value: String,
	onValueChange: (String) -> Unit,
	modifier: Modifier = Modifier,
	isPassword: Boolean = false,
	keyboardType: KeyboardType = KeyboardType.Text,
	errorVisible: Boolean = false,
	errorMessage: String = "",
) {
	val leadingIcon = when {
		isPassword                         -> Icons.Outlined.Lock
		keyboardType == KeyboardType.Email -> Icons.Outlined.Email
		else                               -> null
	}

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
			leadingIcon = leadingIcon?.let { icon ->
				{
					Icon(
						imageVector = icon,
						contentDescription = null,
						tint = MaterialTheme.colorScheme.onSurfaceVariant,
					)
				}
			},
			shape = MaterialTheme.shapes.small,
			singleLine = true,
			isError = errorVisible,
			supportingText = fieldErrorText(errorMessage),
			visualTransformation = if (isPassword) {
				PasswordVisualTransformation()
			} else {
				VisualTransformation.None
			},
			keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
			colors = OutlinedTextFieldDefaults.colors(
				focusedContainerColor = MaterialTheme.colorScheme.background,
				unfocusedContainerColor = MaterialTheme.colorScheme.background,
				disabledContainerColor = MaterialTheme.colorScheme.background,
				focusedBorderColor = MaterialTheme.colorScheme.primary,
				unfocusedBorderColor = MaterialTheme.colorScheme.outline,
				errorBorderColor = MaterialTheme.colorScheme.error,
				errorSupportingTextColor = MaterialTheme.colorScheme.error,
			),
		)
	}
}

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