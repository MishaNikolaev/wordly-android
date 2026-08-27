package com.nmichail.wordly.android.mainhost.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.nmichail.wordly.android.features.books.detail.ui.BookDetailContent
import com.nmichail.wordly.android.features.books.reader.ui.BookReaderContent
import com.nmichail.wordly.android.features.books.ui.BooksContent
import com.nmichail.wordly.android.features.cards.training.ui.CardPracticeContent
import com.nmichail.wordly.android.features.cards.ui.CardsContent
import com.nmichail.wordly.android.features.constructor.ui.ConstructorContent
import com.nmichail.wordly.android.features.constructor.practice.ui.ConstructorPracticeContent
import com.nmichail.wordly.android.features.home.ui.HomeContent
import com.nmichail.wordly.android.features.movies.ui.MoviesContent
import com.nmichail.wordly.android.features.recap.ui.RecapContent
import com.nmichail.wordly.android.features.profile.ui.ProfileContent
import com.nmichail.wordly.android.features.profile.editor.ui.ProfileEditContent
import com.nmichail.wordly.android.features.profile.reminders.ui.ReminderTimesContent
import com.nmichail.wordly.android.features.review.ui.ReviewContent
import com.nmichail.wordly.android.features.materials.article.ui.MaterialDetailContent
import com.nmichail.wordly.android.features.materials.ui.MaterialsContent
import com.nmichail.wordly.android.features.words.presentation.WordsComponent
import com.nmichail.wordly.android.features.words.presentation.detail.WordDetailStore
import com.nmichail.wordly.android.features.words.ui.list.WordContent
import com.nmichail.wordly.android.core.preferences.domain.entity.AppThemeMode
import com.nmichail.wordly.android.mainhost.presentation.MainHostComponent
import com.nmichail.wordly.android.mainhost.presentation.toTab

@Composable
fun MainHostContent(
	component: MainHostComponent,
	themeMode: AppThemeMode,
	devEnabled: Boolean,
	modifier: Modifier = Modifier,
) {
	val stack by component.stack.subscribeAsState()
	val selectedTab = stack.active.instance.toTab()
	var wordDetailOpen by remember { mutableStateOf(false) }

	Scaffold(
		modifier = modifier.fillMaxSize(),
		containerColor = MaterialTheme.colorScheme.background,
		bottomBar = {
			selectedTab?.takeIf { !wordDetailOpen }?.let { tab ->
				MainBottomBar(
					selectedTab = tab,
					onTabSelected = component::handleSelectTab,
				)
			}
		},
	) { innerPadding ->
		Children(
			stack = component.stack,
			modifier = Modifier.fillMaxSize(),
		) { child ->
			MainHostChildContent(
				child = child.instance,
				themeMode = themeMode,
				devEnabled = devEnabled,
				innerPadding = innerPadding,
				onWordDetailOpenChange = { wordDetailOpen = it },
			)
		}
	}
}

@Composable
private fun WordsDetailBottomBarSync(
	component: WordsComponent,
	onWordDetailOpenChange: (Boolean) -> Unit,
) {
	val detail by component.wordDetailModel.subscribeAsState()
	val isOpen = detail is WordDetailStore.State.Open
	LaunchedEffect(isOpen) {
		onWordDetailOpenChange(isOpen)
	}
	DisposableEffect(Unit) {
		onDispose { onWordDetailOpenChange(false) }
	}
}

@Suppress("CyclomaticComplexMethod", "LongMethod")
@Composable
private fun MainHostChildContent(
	child: MainHostComponent.Child,
	themeMode: AppThemeMode,
	devEnabled: Boolean,
	innerPadding: PaddingValues,
	onWordDetailOpenChange: (Boolean) -> Unit,
) {
	when (child) {
		is MainHostComponent.Child.Home -> HomeContent(
			component = child.component,
			modifier = Modifier.padding(innerPadding),
		)
		is MainHostComponent.Child.Words -> {
			WordsDetailBottomBarSync(
				component = child.component,
				onWordDetailOpenChange = onWordDetailOpenChange,
			)
			WordContent(
				component = child.component,
				modifier = Modifier.padding(innerPadding),
			)
		}
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
			devEnabled = devEnabled,
			modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
			bottomContentPadding = innerPadding.calculateBottomPadding(),
		)
		is MainHostComponent.Child.ProfileEdit -> ProfileEditContent(
			component = child.component,
			modifier = Modifier.padding(innerPadding),
		)
		is MainHostComponent.Child.ReminderTimes -> ReminderTimesContent(
			component = child.component,
			modifier = Modifier.padding(innerPadding),
		)
		is MainHostComponent.Child.Review -> ReviewContent(
			component = child.component,
			modifier = Modifier.padding(innerPadding),
		)
		is MainHostComponent.Child.Movies -> MoviesContent(
			component = child.component,
		)
		is MainHostComponent.Child.Recap -> RecapContent(
			component = child.component,
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
		is MainHostComponent.Child.BookDetail -> BookDetailContent(
			component = child.component,
			modifier = Modifier.fillMaxSize(),
		)
		is MainHostComponent.Child.BookReader -> BookReaderContent(
			component = child.component,
		)
	}
}