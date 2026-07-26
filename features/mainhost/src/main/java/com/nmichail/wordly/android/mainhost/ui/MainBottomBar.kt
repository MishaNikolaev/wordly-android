package com.nmichail.wordly.android.mainhost.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
	NavigationBar(
		modifier = modifier,
		containerColor = MaterialTheme.colorScheme.surface,
		contentColor = MaterialTheme.colorScheme.primary,
	) {
		bottomBarItems().forEach { item ->
			BottomBarNavigationItem(
				item = item,
				selected = selectedTab == item.tab,
				onClick = { onTabSelected(item.tab) },
			)
		}
	}
}

@Composable
private fun RowScope.BottomBarNavigationItem(
	item: BottomBarItem,
	selected: Boolean,
	onClick: () -> Unit,
) {
	val colorScheme = MaterialTheme.colorScheme

	NavigationBarItem(
		selected = selected,
		onClick = onClick,
		icon = {
			Icon(
				imageVector = item.icon,
				contentDescription = stringResource(item.labelResId),
				modifier = Modifier.size(24.dp),
			)
		},
		label = {
			Text(
				text = stringResource(item.labelResId),
				style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
			)
		},
		colors = NavigationBarItemDefaults.colors(
			selectedIconColor = colorScheme.primary,
			selectedTextColor = colorScheme.primary,
			unselectedIconColor = colorScheme.onSurfaceVariant,
			unselectedTextColor = colorScheme.onSurfaceVariant,
			indicatorColor = colorScheme.primaryContainer,
		),
	)
}

private fun bottomBarItems(): List<BottomBarItem> = listOf(
	BottomBarItem(MainHostTab.Home, R.string.bottom_nav_learn, Icons.Outlined.Home),
	BottomBarItem(MainHostTab.Words, R.string.bottom_nav_words, Icons.Outlined.MenuBook),
	BottomBarItem(MainHostTab.Stats, R.string.bottom_nav_materials, Icons.Outlined.Article),
	BottomBarItem(MainHostTab.Profile, R.string.bottom_nav_profile, Icons.Outlined.Person),
)

private data class BottomBarItem(
	val tab: MainHostTab,
	@StringRes val labelResId: Int,
	val icon: ImageVector,
)
