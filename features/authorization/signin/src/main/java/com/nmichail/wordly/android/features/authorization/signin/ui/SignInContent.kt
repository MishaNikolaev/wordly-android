package com.nmichail.wordly.android.features.authorization.signin.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.nmichail.wordly.android.features.authorization.signin.presentation.SignInComponent

@Composable
fun SignInContent(
	component: SignInComponent,
	modifier: Modifier = Modifier,
) {
	Box(
		modifier = modifier.fillMaxSize(),
		contentAlignment = Alignment.Center,
	) {
		Text(
			text = "Sign In",
			style = MaterialTheme.typography.headlineMedium,
		)
	}
}