package com.nmichail.wordly.android.component.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.ui.R

private val ActionsHeight = 52.dp
private val ActionsShape = RoundedCornerShape(14.dp)

@Composable
fun NewsDetailActions(
	onShareClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme

	Button(
		onClick = onShareClick,
		modifier = modifier
			.fillMaxWidth()
			.height(ActionsHeight),
		shape = ActionsShape,
		contentPadding = PaddingValues(horizontal = 16.dp),
		colors = ButtonDefaults.buttonColors(
			containerColor = colorScheme.primary,
			contentColor = colorScheme.onPrimary,
		),
	) {
		Row(
			horizontalArrangement = Arrangement.spacedBy(8.dp),
			verticalAlignment = Alignment.CenterVertically,
		) {
			Icon(
				imageVector = Icons.Outlined.Share,
				contentDescription = null,
				modifier = Modifier.size(20.dp),
			)
			Text(
				text = stringResource(R.string.news_detail_share),
				style = MaterialTheme.typography.labelLarge,
			)
		}
	}
}
