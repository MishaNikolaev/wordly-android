package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.R

@Composable
fun SearchField(
	value: String,
	onValueChange: (String) -> Unit,
	placeholder: String,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme

	OutlinedTextField(
		value = value,
		onValueChange = onValueChange,
		modifier = modifier.fillMaxWidth(),
		singleLine = true,
		shape = MaterialTheme.shapes.extraLarge,
		leadingIcon = {
			Icon(
				imageVector = Icons.Outlined.Search,
				contentDescription = stringResource(R.string.search_field_icon),
				tint = colorScheme.onSurfaceVariant,
				modifier = Modifier.size(20.dp),
			)
		},
		placeholder = {
			Text(
				text = placeholder,
				style = MaterialTheme.typography.bodyMedium,
				color = colorScheme.onSurfaceVariant,
			)
		},
		colors = OutlinedTextFieldDefaults.colors(
			focusedContainerColor = colorScheme.surfaceVariant,
			unfocusedContainerColor = colorScheme.surfaceVariant,
			disabledContainerColor = colorScheme.surfaceVariant,
			focusedBorderColor = colorScheme.surfaceVariant,
			unfocusedBorderColor = colorScheme.surfaceVariant,
			disabledBorderColor = colorScheme.surfaceVariant,
			cursorColor = colorScheme.primary,
			focusedTextColor = colorScheme.onSurface,
			unfocusedTextColor = colorScheme.onSurface,
		),
	)
}