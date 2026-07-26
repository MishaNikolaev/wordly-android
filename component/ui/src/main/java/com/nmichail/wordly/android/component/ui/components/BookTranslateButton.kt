package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.R

@Composable
fun BookTranslateButton(
	isTranslating: Boolean,
	isTranslated: Boolean,
	contentDescription: String,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme
	val background = when {
		isTranslated -> colorScheme.primary
		else -> colorScheme.surfaceVariant
	}
	val iconTint = when {
		isTranslated -> colorScheme.onPrimary
		else -> colorScheme.onBackground
	}

	Box(
		modifier = modifier
			.size(40.dp)
			.clip(RoundedCornerShape(12.dp))
			.background(background)
			.clickable(
				enabled = !isTranslating,
				role = Role.Button,
				onClick = onClick,
			),
		contentAlignment = Alignment.Center,
	) {
		if (isTranslating) {
			CircularProgressIndicator(
				modifier = Modifier.size(22.dp),
				color = colorScheme.primary,
				strokeWidth = 2.dp,
			)
		} else {
			Icon(
				painter = painterResource(R.drawable.paragraph),
				contentDescription = contentDescription,
				tint = iconTint,
				modifier = Modifier.size(17.dp),
			)
		}
	}
}
