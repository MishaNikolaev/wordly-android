package com.nmichail.wordly.android.mainhost.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nmichail.wordly.android.component.wui.R as WuiR
import com.nmichail.wordly.android.features.mainhost.R
import com.nmichail.wordly.android.mainhost.presentation.MainHostTab

private val BottomNavInactiveColor = Color(0xFF757575)

@Composable
fun MainBottomBar(
	selectedTab: MainHostTab,
	onTabSelected: (MainHostTab) -> Unit,
	modifier: Modifier = Modifier,
) {
	val colorScheme = MaterialTheme.colorScheme

	NavigationBar(
		modifier = modifier,
		containerColor = colorScheme.surface,
		contentColor = colorScheme.onSurface,
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
	val iconRes = if (selected) item.selectedIconRes else item.unselectedIconRes

	NavigationBarItem(
		selected = selected,
		onClick = onClick,
		icon = {
			Icon(
				painter = painterResource(iconRes),
				contentDescription = stringResource(item.labelResId),
				modifier = Modifier.size(26.dp),
			)
		},
		label = {
			Text(
				text = stringResource(item.labelResId),
				style = MaterialTheme.typography.labelSmall.copy(
					fontSize = 12.sp,
					fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
				),
			)
		},
		colors = NavigationBarItemDefaults.colors(
			selectedIconColor = colorScheme.onSurface,
			selectedTextColor = colorScheme.onSurface,
			unselectedIconColor = BottomNavInactiveColor,
			unselectedTextColor = BottomNavInactiveColor,
			indicatorColor = Color.Transparent,
		),
	)
}

private fun bottomBarItems(): List<BottomBarItem> = listOf(
	BottomBarItem(
		tab = MainHostTab.Home,
		labelResId = R.string.bottom_nav_learn,
		selectedIconRes = WuiR.drawable.home,
		unselectedIconRes = WuiR.drawable.home_filled,
	),
	BottomBarItem(
		tab = MainHostTab.Words,
		labelResId = R.string.bottom_nav_words,
		selectedIconRes = WuiR.drawable.layers,
		unselectedIconRes = WuiR.drawable.layers_filled,
	),
	BottomBarItem(
		tab = MainHostTab.Materials,
		labelResId = R.string.bottom_nav_materials,
		selectedIconRes = WuiR.drawable.description,
		unselectedIconRes = WuiR.drawable.description_filled,
	),
	BottomBarItem(
		tab = MainHostTab.Profile,
		labelResId = R.string.bottom_nav_profile,
		selectedIconRes = WuiR.drawable.person,
		unselectedIconRes = WuiR.drawable.person_filled,
	),
)

private data class BottomBarItem(
	val tab: MainHostTab,
	@StringRes val labelResId: Int,
	@DrawableRes val selectedIconRes: Int,
	@DrawableRes val unselectedIconRes: Int,
)