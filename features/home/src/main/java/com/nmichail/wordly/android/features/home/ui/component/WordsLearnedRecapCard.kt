package com.nmichail.wordly.android.features.home.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.wui.theme.Wui
import com.nmichail.wordly.android.component.wui.theme.WuiTheme
import com.nmichail.wordly.android.component.wui.theme.WuiTypography
import com.nmichail.wordly.android.features.home.R
import com.nmichail.wordly.android.component.wui.R as WuiR

@Composable
fun WordsLearnedRecapCard(
	onCtaClick: () -> Unit,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme

	Row(
		modifier = modifier.fillMaxWidth(),
		verticalAlignment = Alignment.Bottom,
	) {
		Column(
			modifier = Modifier
				.weight(1f)
				.padding(end = 8.dp),
			verticalArrangement = Arrangement.spacedBy(16.dp),
		) {
			Text(
				text = stringResource(R.string.home_recap_title),
				style = WuiTypography.homeRecapTitle,
				color = colorScheme.onSurface,
			)
			Text(
				text = stringResource(R.string.home_recap_subtitle),
				style = WuiTypography.homeRecapSubtitle,
				color = colorScheme.onSurfaceVariant,
			)
			Box(
				modifier = Modifier
					.height(44.dp)
					.width(158.dp)
					.clip(RoundedCornerShape(22.dp))
					.background(Wui.colors.recapCta)
					.clickable(role = Role.Button, onClick = onCtaClick),
				contentAlignment = Alignment.Center,
			) {
				Text(
					text = stringResource(R.string.home_recap_cta),
					style = WuiTypography.homeRecapCta,
					color = colorScheme.onSurface,
				)
			}
		}
		Image(
			painter = painterResource(WuiR.drawable.bull),
			contentDescription = null,
			modifier = Modifier.size(width = 128.dp, height = 154.dp),
		)
	}
}

@Preview(showBackground = true)
@Composable
private fun WordsLearnedRecapCardPreview() {
	WuiTheme {
		WordsLearnedRecapCard(
			onCtaClick = {},
			modifier = Modifier.padding(16.dp),
		)
	}
}