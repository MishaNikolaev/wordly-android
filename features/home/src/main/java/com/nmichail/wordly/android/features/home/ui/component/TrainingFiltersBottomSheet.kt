package com.nmichail.wordly.android.features.home.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nmichail.wordly.android.features.home.R

enum class HomeTrainingFilter {
	Cards,
	Constructor,
	Listening,
	Songs,
	Movies,
	Books,
	;

	fun toTabOrNull(): HomeTrainingTab? =
		when (this) {
			Cards -> HomeTrainingTab.Cards
			Constructor -> HomeTrainingTab.Constructor
			Listening -> HomeTrainingTab.Listening
			Books -> HomeTrainingTab.Books
			Songs,
			Movies,
			-> null
		}
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun TrainingFiltersBottomSheet(
	selectedTab: HomeTrainingTab?,
	onDismiss: () -> Unit,
	onFilterClick: (HomeTrainingFilter) -> Unit,
) {
	val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

	ModalBottomSheet(
		onDismissRequest = onDismiss,
		sheetState = sheetState,
		shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
		containerColor = MaterialTheme.colorScheme.surface,
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.navigationBarsPadding()
				.padding(horizontal = 20.dp)
				.padding(top = 8.dp, bottom = 24.dp),
			verticalArrangement = Arrangement.spacedBy(12.dp),
		) {
			Text(
				text = stringResource(R.string.home_trainings_section),
				style = MaterialTheme.typography.titleMedium,
				fontWeight = FontWeight.Bold,
				color = MaterialTheme.colorScheme.onSurface,
			)
			FlowRow(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.spacedBy(8.dp),
				verticalArrangement = Arrangement.spacedBy(8.dp),
			) {
				HomeTrainingFilter.entries.forEach { filter ->
					TrainingFilterChip(
						text = stringResource(filter.titleRes()),
						selected = filter.matches(selectedTab),
						onClick = { onFilterClick(filter) },
					)
				}
			}
		}
	}
}
