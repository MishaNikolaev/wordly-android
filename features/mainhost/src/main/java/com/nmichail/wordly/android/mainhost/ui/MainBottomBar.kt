package com.nmichail.wordly.android.mainhost.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nmichail.wordly.android.features.mainhost.R
import com.nmichail.wordly.android.mainhost.presentation.MainHostTab

@Composable
fun MainBottomBar(
	selectedTab: MainHostTab,
	onTabSelected: (MainHostTab) -> Unit,
	modifier: Modifier = Modifier,
) {
	val items = listOf(
		BottomBarItem(
			tab = MainHostTab.Home,
			labelResId = R.string.bottom_nav_learn,
			iconResId = R.drawable.learning,
		),
		BottomBarItem(
			tab = MainHostTab.Words,
			labelResId = R.string.bottom_nav_words,
			iconResId = R.drawable.words,
		),
		BottomBarItem(
			tab = MainHostTab.Stats,
			labelResId = R.string.bottom_nav_stats,
			iconResId = R.drawable.stats,
		),
		BottomBarItem(
			tab = MainHostTab.Profile,
			labelResId = R.string.bottom_nav_profile,
			iconResId = R.drawable.profile,
		),
	)

	NavigationBar(
		modifier = modifier,
		containerColor = MaterialTheme.colorScheme.background,
		contentColor = MaterialTheme.colorScheme.secondary,
	) {
		items.forEach { item ->
			val selected = selectedTab == item.tab
			val colorScheme = MaterialTheme.colorScheme

			NavigationBarItem(
				selected = selected,
				onClick = { onTabSelected(item.tab) },
				icon = {
					Icon(
						painter = painterResource(item.iconResId),
						contentDescription = stringResource(item.labelResId),
						modifier = Modifier.size(27.dp),
					)
				},
				label = {
					Text(
						text = stringResource(item.labelResId),
						style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
					)
				},
				colors = NavigationBarItemDefaults.colors(
					selectedIconColor = colorScheme.secondary,
					selectedTextColor = colorScheme.secondary,
					unselectedIconColor = colorScheme.onSurfaceVariant,
					unselectedTextColor = colorScheme.onSurfaceVariant,
					indicatorColor = colorScheme.background,
				),
			)
		}
	}
}

private data class BottomBarItem(
	val tab: MainHostTab,
	@StringRes val labelResId: Int,
	@DrawableRes val iconResId: Int,
)