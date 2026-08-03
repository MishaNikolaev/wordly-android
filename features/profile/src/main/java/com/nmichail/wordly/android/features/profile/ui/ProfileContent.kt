package com.nmichail.wordly.android.features.profile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.component.ui.components.button.CustomButton
import com.nmichail.wordly.android.features.profile.R
import com.nmichail.wordly.android.features.profile.presentation.ProfileComponent

@Composable
fun ProfileContent(
	component: ProfileComponent,
	modifier: Modifier = Modifier,
) {
	val state by component.model.subscribeAsState()

	when (val current = state) {
		ProfileComponent.State.Loading -> ProfileLoading(modifier = modifier)
		ProfileComponent.State.Error -> ProfileError(
			onRetryClick = component::handleRetry,
			modifier = modifier.fillMaxSize(),
		)
		is ProfileComponent.State.Content -> ProfileLoaded(
			state = current,
			component = component,
			modifier = modifier,
		)
	}
}

@Composable
private fun ProfileLoading(modifier: Modifier = Modifier) {
	Box(
		modifier = modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background),
		contentAlignment = Alignment.Center,
	) {
		CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
	}
}

@Composable
private fun ProfileError(
	onRetryClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.background(MaterialTheme.colorScheme.background)
			.padding(horizontal = 24.dp),
		verticalArrangement = Arrangement.Center,
		horizontalAlignment = Alignment.CenterHorizontally,
	) {
		Text(
			text = stringResource(R.string.profile_error_title),
			style = MaterialTheme.typography.titleLarge,
			color = MaterialTheme.colorScheme.onBackground,
			textAlign = TextAlign.Center,
		)
		Text(
			text = stringResource(R.string.profile_error_subtitle),
			style = MaterialTheme.typography.bodyMedium,
			color = MaterialTheme.colorScheme.onSurfaceVariant,
			textAlign = TextAlign.Center,
			modifier = Modifier.padding(top = 8.dp),
		)
		CustomButton(
			text = stringResource(R.string.profile_retry),
			onClick = onRetryClick,
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 24.dp),
		)
	}
}

@Composable
private fun ProfileLoaded(
	state: ProfileComponent.State.Content,
	component: ProfileComponent,
	modifier: Modifier = Modifier,
) {
	Column(
		modifier = modifier
			.fillMaxSize()
			.background(MaterialTheme.colorScheme.background)
			.padding(horizontal = 20.dp)
			.padding(top = 12.dp, bottom = 24.dp),
	) {
		Column(
			modifier = Modifier
				.weight(1f)
				.verticalScroll(rememberScrollState()),
		) {
			ProfileHeader(
				fullName = state.profile.fullName,
			)
			ProfileLevelRow(
				level = state.profile.englishLevel,
				onClick = component::handleOpenLevel,
				modifier = Modifier.padding(top = 20.dp),
			)
			CustomButton(
				text = stringResource(R.string.profile_edit),
				onClick = component::handleOpenEdit,
				modifier = Modifier.padding(top = 20.dp),
				containerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
				contentColor = MaterialTheme.colorScheme.onSurface,
			)
			ProfileSettingsSection(
				state = state,
				component = component,
				modifier = Modifier.padding(top = 28.dp, bottom = 24.dp),
			)
		}
		ProfileLogoutButton(
			onClick = component::handleOpenLogout,
			loading = state.loggingOut,
			modifier = Modifier.padding(top = 8.dp),
		)
	}
	ProfileDialogs(state = state, component = component)
}

@Composable
private fun ProfileLogoutButton(
	onClick: () -> Unit,
	loading: Boolean,
	modifier: Modifier = Modifier,
) {
	val contentColor = Color.White
	Button(
		onClick = onClick,
		modifier = modifier
			.fillMaxWidth()
			.height(52.dp),
		enabled = !loading,
		shape = MaterialTheme.shapes.small,
		colors = ButtonDefaults.buttonColors(
			containerColor = MaterialTheme.colorScheme.primary,
			contentColor = contentColor,
			disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
			disabledContentColor = contentColor.copy(alpha = 0.6f),
		),
	) {
		if (loading) {
			CircularProgressIndicator(
				modifier = Modifier.size(24.dp),
				color = contentColor,
				strokeWidth = 2.dp,
			)
		} else {
			Text(
				text = stringResource(R.string.profile_logout),
				style = MaterialTheme.typography.labelLarge,
				fontWeight = FontWeight.Medium,
			)
		}
	}
}

@Composable
private fun ProfileHeader(
	fullName: String,
	modifier: Modifier = Modifier,
) {
	Row(
		modifier = modifier.fillMaxWidth(),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.spacedBy(16.dp),
	) {
		Box(
			modifier = Modifier
				.size(72.dp)
				.clip(CircleShape)
				.background(MaterialTheme.colorScheme.primaryContainer),
			contentAlignment = Alignment.Center,
		) {
			Icon(
				imageVector = Icons.Outlined.Person,
				contentDescription = null,
				tint = MaterialTheme.colorScheme.onPrimaryContainer,
				modifier = Modifier.size(36.dp),
			)
		}
		Text(
			text = fullName,
			style = MaterialTheme.typography.titleLarge,
			fontWeight = FontWeight.SemiBold,
			color = MaterialTheme.colorScheme.onBackground,
		)
	}
}
