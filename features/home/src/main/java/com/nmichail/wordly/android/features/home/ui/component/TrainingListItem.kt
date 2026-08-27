package com.nmichail.wordly.android.features.home.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.wui.components.card.WuiAppCard
import com.nmichail.wordly.android.component.wui.R as WuiR
import com.nmichail.wordly.android.component.wui.theme.WuiTheme
import com.nmichail.wordly.android.component.wui.theme.WuiTypography

private val TrainingCardHeight = 80.dp
private val TrainingImageWidth = 80.dp
private val TrainingImageShape = RoundedCornerShape(
	topStart = 16.dp,
	bottomStart = 16.dp,
)

@Composable
fun TrainingListItem(
	title: String,
	@DrawableRes iconRes: Int,
	onClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	WuiAppCard(
		modifier = modifier.fillMaxWidth(),
		onClick = onClick,
		contentPadding = PaddingValues(0.dp),
	) {
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.height(TrainingCardHeight),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.spacedBy(12.dp),
		) {
			Image(
				painter = painterResource(iconRes),
				contentDescription = null,
				contentScale = ContentScale.Crop,
				modifier = Modifier
					.fillMaxHeight()
					.width(TrainingImageWidth)
					.clip(TrainingImageShape),
			)
			Text(
				text = title,
				style = WuiTypography.homeTrainingLabel,
				color = MaterialTheme.colorScheme.onSurface,
				modifier = Modifier.padding(end = 12.dp),
			)
		}
	}
}

@Preview(showBackground = true)
@Composable
private fun TrainingListItemPreview() {
	WuiTheme {
		TrainingListItem(
			title = "Карточки",
			iconRes = WuiR.drawable.cards,
			onClick = {},
			modifier = Modifier.padding(16.dp),
		)
	}
}
