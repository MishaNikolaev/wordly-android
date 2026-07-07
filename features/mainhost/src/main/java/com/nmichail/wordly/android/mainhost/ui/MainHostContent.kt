package com.nmichail.wordly.android.mainhost.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.features.home.ui.HomeContent
import com.nmichail.wordly.android.features.profile.ui.ProfileContent
import com.nmichail.wordly.android.features.stats.ui.StatsContent
import com.nmichail.wordly.android.features.words.ui.WordContent
import com.nmichail.wordly.android.mainhost.presentation.MainHostComponent
import com.nmichail.wordly.android.mainhost.presentation.toTab

@Composable
fun MainHostContent(
	component: MainHostComponent,
	modifier: Modifier = Modifier,
) {
	val stack by component.stack.subscribeAsState()
	val selectedTab = stack.active.instance.toTab()

	Scaffold(
		modifier = modifier,
		bottomBar = {
			MainBottomBar(
				selectedTab = selectedTab,
				onTabSelected = component::onTabSelected,
			)
		},
	) { innerPadding ->
		Children(stack = component.stack) { child ->
			val contentModifier = Modifier.padding(innerPadding)

			when (child.instance) {
				MainHostComponent.Child.Home -> HomeContent(modifier = contentModifier)
				MainHostComponent.Child.Words -> WordContent(modifier = contentModifier)
				MainHostComponent.Child.Stats -> StatsContent(modifier = contentModifier)
				MainHostComponent.Child.Profile -> ProfileContent(modifier = contentModifier)
			}
		}
	}
}