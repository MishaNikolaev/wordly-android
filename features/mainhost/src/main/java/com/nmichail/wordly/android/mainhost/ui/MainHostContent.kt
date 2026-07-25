package com.nmichail.wordly.android.mainhost.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.features.cards.ui.CardPracticeContent
import com.nmichail.wordly.android.features.cards.ui.CardsContent
import com.nmichail.wordly.android.features.home.ui.HomeContent
import com.nmichail.wordly.android.features.news.ui.NewsDetailContent
import com.nmichail.wordly.android.features.profile.ui.ProfileContent
import com.nmichail.wordly.android.features.review.ui.ReviewContent
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
	val activeChild = stack.active.instance
	val selectedTab = activeChild.toTab()
	val showBottomBar = selectedTab != null

	Scaffold(
		modifier = modifier,
		containerColor = MaterialTheme.colorScheme.background,
		bottomBar = {
			if (showBottomBar && selectedTab != null) {
				MainBottomBar(
					selectedTab = selectedTab,
					onTabSelected = component::handleSelectTab,
				)
			}
		},
	) { innerPadding ->
		Children(stack = component.stack) { child ->
			when (val instance = child.instance) {
				is MainHostComponent.Child.Home -> HomeContent(
					component = instance.component,
					modifier = Modifier.padding(innerPadding),
				)
				MainHostComponent.Child.Words -> WordContent(
					modifier = Modifier.padding(innerPadding),
				)
				MainHostComponent.Child.Stats -> StatsContent(
					modifier = Modifier.padding(innerPadding),
				)
				MainHostComponent.Child.Profile -> ProfileContent(
					modifier = Modifier.padding(innerPadding),
				)
				is MainHostComponent.Child.Review -> ReviewContent(
					component = instance.component,
					modifier = Modifier.padding(innerPadding),
				)
				is MainHostComponent.Child.Cards -> CardsContent(
					component = instance.component,
				)
				is MainHostComponent.Child.CardPractice -> CardPracticeContent(
					component = instance.component,
				)
				is MainHostComponent.Child.NewsDetail -> NewsDetailContent(
					component = instance.component,
				)
			}
		}
	}
}
