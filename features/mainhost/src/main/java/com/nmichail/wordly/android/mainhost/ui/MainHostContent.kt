package com.nmichail.wordly.android.mainhost.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.features.books.ui.BookReaderContent
import com.nmichail.wordly.android.features.books.ui.BooksContent
import com.nmichail.wordly.android.features.cards.ui.CardPracticeContent
import com.nmichail.wordly.android.features.cards.ui.CardsContent
import com.nmichail.wordly.android.features.constructor.ui.ConstructorContent
import com.nmichail.wordly.android.features.constructor.ui.ConstructorPracticeContent
import com.nmichail.wordly.android.features.home.ui.HomeContent
import com.nmichail.wordly.android.features.profile.ui.ProfileContent
import com.nmichail.wordly.android.features.profile.ui.ProfileEditContent
import com.nmichail.wordly.android.features.review.ui.ReviewContent
import com.nmichail.wordly.android.features.materials.ui.MaterialDetailContent
import com.nmichail.wordly.android.features.materials.ui.MaterialsContent
import com.nmichail.wordly.android.features.words.ui.WordContent
import com.nmichail.wordly.android.core.preferences.domain.entity.AppThemeMode
import com.nmichail.wordly.android.mainhost.presentation.MainHostComponent
import com.nmichail.wordly.android.mainhost.presentation.toTab

@Composable
fun MainHostContent(
	component: MainHostComponent,
	themeMode: AppThemeMode,
	modifier: Modifier = Modifier,
) {
	val stack by component.stack.subscribeAsState()
	val selectedTab = stack.active.instance.toTab()
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
			MainHostChildContent(
				child = child.instance,
				themeMode = themeMode,
				innerPadding = innerPadding,
			)
		}
	}
}

@Composable
private fun MainHostChildContent(
	child: MainHostComponent.Child,
	themeMode: AppThemeMode,
	innerPadding: PaddingValues,
) {
	when (child) {
		is MainHostComponent.Child.Home -> HomeContent(
			component = child.component,
			modifier = Modifier.padding(innerPadding),
		)
		is MainHostComponent.Child.Words -> WordContent(
			component = child.component,
			modifier = Modifier.padding(innerPadding),
		)
		is MainHostComponent.Child.Materials -> MaterialsContent(
			component = child.component,
			modifier = Modifier.padding(innerPadding),
		)
		is MainHostComponent.Child.MaterialDetail -> MaterialDetailContent(
			component = child.component,
		)
		is MainHostComponent.Child.Profile -> ProfileContent(
			component = child.component,
			themeMode = themeMode,
			modifier = Modifier.padding(innerPadding),
		)
		is MainHostComponent.Child.ProfileEdit -> ProfileEditContent(
			component = child.component,
			modifier = Modifier.padding(innerPadding),
		)
		is MainHostComponent.Child.Review -> ReviewContent(
			component = child.component,
			modifier = Modifier.padding(innerPadding),
		)
		is MainHostComponent.Child.Cards -> CardsContent(
			component = child.component,
		)
		is MainHostComponent.Child.CardPractice -> CardPracticeContent(
			component = child.component,
		)
		is MainHostComponent.Child.Constructor -> ConstructorContent(
			component = child.component,
		)
		is MainHostComponent.Child.ConstructorPractice -> ConstructorPracticeContent(
			component = child.component,
		)
		is MainHostComponent.Child.Books -> BooksContent(
			component = child.component,
		)
		is MainHostComponent.Child.BookReader -> BookReaderContent(
			component = child.component,
		)
	}
}
