package com.nmichail.wordly.android.mainhost.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
	badges: Map<MainHostTab, Int> = emptyMap(),
) {
	NavigationBar(
		modifier = modifier,
		containerColor = MaterialTheme.colorScheme.surface,
		contentColor = MaterialTheme.colorScheme.secondary,
	) {
		bottomBarItems().forEach { item ->
			BottomBarNavigationItem(
				item = item,
				selected = selectedTab == item.tab,
				badgeCount = badges[item.tab],
				onClick = { onTabSelected(item.tab) },
			)
		}
	}
}

@Composable
private fun RowScope.BottomBarNavigationItem(
	item: BottomBarItem,
	selected: Boolean,
	badgeCount: Int?,
	onClick: () -> Unit,
) {
	val colorScheme = MaterialTheme.colorScheme

	NavigationBarItem(
		selected = selected,
		onClick = onClick,
		icon = {
			BadgedBox(
				badge = {
					if (badgeCount != null && badgeCount > 0) {
						Badge(
							containerColor = colorScheme.error,
							contentColor = colorScheme.onError,
						) {
							Text(
								text = badgeCount.toString(),
								style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
							)
						}
					}
				},
			) {
				Icon(
					painter = painterResource(item.iconResId),
					contentDescription = stringResource(item.labelResId),
					modifier = Modifier.size(27.dp),
				)
			}
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
			indicatorColor = colorScheme.surface,
		),
	)
}

private fun bottomBarItems(): List<BottomBarItem> = listOf(
	BottomBarItem(MainHostTab.Home, R.string.bottom_nav_learn, R.drawable.learning),
	BottomBarItem(MainHostTab.Words, R.string.bottom_nav_words, R.drawable.words),
	BottomBarItem(MainHostTab.Stats, R.string.bottom_nav_stats, R.drawable.stats),
	BottomBarItem(MainHostTab.Profile, R.string.bottom_nav_profile, R.drawable.profile),
)

private data class BottomBarItem(
	val tab: MainHostTab,
	@StringRes val labelResId: Int,
	@DrawableRes val iconResId: Int,
)
