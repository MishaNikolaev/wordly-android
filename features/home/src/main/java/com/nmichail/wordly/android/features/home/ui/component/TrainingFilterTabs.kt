package com.nmichail.wordly.android.features.home.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.component.wui.theme.WuiTheme

enum class HomeTrainingTab {
	Cards,
	Constructor,
	Listening,
	Books,
}

private val visibleTabs = listOf(
	HomeTrainingTab.Cards,
	HomeTrainingTab.Constructor,
	HomeTrainingTab.Listening,
	HomeTrainingTab.Books,
)

@Composable
fun TrainingFilterTabs(
	onGridClick: () -> Unit,
	onTabSelected: (HomeTrainingTab) -> Unit,
	modifier: Modifier = Modifier,
) {
	LazyRow(
		modifier = modifier.fillMaxWidth(),
		contentPadding = PaddingValues(0.dp),
		horizontalArrangement = Arrangement.spacedBy(8.dp),
	) {
		item {
			TrainingGridTab(onClick = onGridClick)
		}
		items(visibleTabs) { tab ->
			TrainingFilterChip(
				text = stringResource(tab.titleRes()),
				selected = false,
				onClick = { onTabSelected(tab) },
			)
		}
	}
}

@Preview(showBackground = true)
@Composable
private fun TrainingFilterTabsPreview() {
	WuiTheme {
		TrainingFilterTabs(
			onGridClick = {},
			onTabSelected = {},
		)
	}
}
