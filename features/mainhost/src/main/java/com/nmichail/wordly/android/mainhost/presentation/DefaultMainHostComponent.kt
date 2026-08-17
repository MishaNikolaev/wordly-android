package com.nmichail.wordly.android.mainhost.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.books.presentation.BooksComponent
import com.nmichail.wordly.android.features.books.presentation.BooksRouter
import com.nmichail.wordly.android.features.books.presentation.detail.BookReaderComponent
import com.nmichail.wordly.android.features.books.presentation.detail.BookReaderRouter
import com.nmichail.wordly.android.features.cards.presentation.CardsComponent
import com.nmichail.wordly.android.features.cards.presentation.CardsRouter
import com.nmichail.wordly.android.features.cards.presentation.detail.CardPracticeComponent
import com.nmichail.wordly.android.features.cards.presentation.detail.CardPracticeRouter
import com.nmichail.wordly.android.features.constructor.presentation.ConstructorComponent
import com.nmichail.wordly.android.features.constructor.presentation.ConstructorRouter
import com.nmichail.wordly.android.features.constructor.practice.presentation.ConstructorPracticeComponent
import com.nmichail.wordly.android.features.constructor.practice.presentation.ConstructorPracticeRouter
import com.nmichail.wordly.android.features.home.presentation.HomeComponent
import com.nmichail.wordly.android.features.home.presentation.HomeRouter
import com.nmichail.wordly.android.features.materials.presentation.MaterialsComponent
import com.nmichail.wordly.android.features.materials.presentation.detail.MaterialDetailComponent
import com.nmichail.wordly.android.features.materials.presentation.detail.MaterialDetailRouter
import com.nmichail.wordly.android.features.profile.presentation.ProfileComponent
import com.nmichail.wordly.android.features.profile.presentation.edit.ProfileEditComponent
import com.nmichail.wordly.android.features.profile.presentation.edit.ProfileEditRouter
import com.nmichail.wordly.android.features.profile.presentation.reminder.ReminderTimesComponent
import com.nmichail.wordly.android.features.profile.presentation.reminder.ReminderTimesRouter
import com.nmichail.wordly.android.features.review.presentation.ReviewComponent
import com.nmichail.wordly.android.features.review.presentation.ReviewRouter
import com.nmichail.wordly.android.features.words.presentation.WordsComponent
import kotlinx.serialization.Serializable

@Suppress("TooManyFunctions")
internal class DefaultMainHostComponent(
	componentContext: ComponentContext,
	private val homeComponentFactory: HomeComponent.Factory,
	private val wordsComponentFactory: WordsComponent.Factory,
	private val materialsComponentFactory: MaterialsComponent.Factory,
	private val materialDetailComponentFactory: MaterialDetailComponent.Factory,
	private val profileComponentFactory: ProfileComponent.Factory,
	private val profileEditComponentFactory: ProfileEditComponent.Factory,
	private val reminderTimesComponentFactory: ReminderTimesComponent.Factory,
	private val reviewComponentFactory: ReviewComponent.Factory,
	private val cardsComponentFactory: CardsComponent.Factory,
	private val cardPracticeComponentFactory: CardPracticeComponent.Factory,
	private val constructorComponentFactory: ConstructorComponent.Factory,
	private val constructorPracticeComponentFactory: ConstructorPracticeComponent.Factory,
	private val booksComponentFactory: BooksComponent.Factory,
	private val bookReaderComponentFactory: BookReaderComponent.Factory,
) : MainHostComponent, ComponentContext by componentContext {

	private val navigation = StackNavigation<MainHostConfig>()
	private var profileComponent: ProfileComponent? = null

	override val stack: Value<ChildStack<*, MainHostComponent.Child>> = childStack(
		source = navigation,
		serializer = MainHostConfig.serializer(),
		initialStack = { listOf(MainHostConfig.Home) },
		handleBackButton = true,
		childFactory = ::child,
	)

	override fun handleSelectTab(tab: MainHostTab) {
		navigation.bringToFront(tab.toConfig())
	}

	@OptIn(DelicateDecomposeApi::class)
	@Suppress("CyclomaticComplexMethod")
	private fun child(
		config: MainHostConfig,
		componentContext: ComponentContext,
	): MainHostComponent.Child =
		when (config) {
			MainHostConfig.Home -> homeChild(componentContext)
			MainHostConfig.Words -> MainHostComponent.Child.Words(
				component = wordsComponentFactory(componentContext),
			)
			MainHostConfig.Materials -> materialsChild(componentContext)
			is MainHostConfig.MaterialDetail -> materialDetailChild(config.materialId, componentContext)
			MainHostConfig.Profile -> profileChild(componentContext)
			MainHostConfig.ProfileEdit -> profileEditChild(componentContext)
			MainHostConfig.ReminderTimes -> reminderTimesChild(componentContext)
			MainHostConfig.Review -> reviewChild(componentContext)
			MainHostConfig.Cards -> cardsChild(componentContext)
			is MainHostConfig.CardPractice -> cardPracticeChild(config.cardId, componentContext)
			MainHostConfig.Constructor -> constructorChild(componentContext)
			is MainHostConfig.ConstructorPractice -> {
				constructorPracticeChild(config.themeId, componentContext)
			}
			MainHostConfig.Books -> {
				val booksRouter = object : BooksRouter {
					override fun navigateBack() {
						navigation.pop()
					}
				}
				MainHostComponent.Child.Books(
					component = booksComponentFactory(
						componentContext = componentContext,
						booksRouter = booksRouter,
						onBookClick = { book ->
							navigation.push(MainHostConfig.BookReader(bookId = book.id))
						},
					),
				)
			}
			is MainHostConfig.BookReader -> {
				val bookReaderRouter = object : BookReaderRouter {
					override fun navigateBack() {
						navigation.pop()
					}
				}
				MainHostComponent.Child.BookReader(
					component = bookReaderComponentFactory(
						componentContext = componentContext,
						bookId = config.bookId,
						bookReaderRouter = bookReaderRouter,
						onAddWordToCard = {
							// TODO: реализовать добавление слова в карточку
						},
					),
				)
			}
		}

	@OptIn(DelicateDecomposeApi::class)
	private fun profileChild(componentContext: ComponentContext): MainHostComponent.Child {
		val component = profileComponentFactory(
			componentContext = componentContext,
			onOpenEdit = {
				navigation.push(MainHostConfig.ProfileEdit)
			},
			onOpenReminderTimes = {
				navigation.push(MainHostConfig.ReminderTimes)
			},
		)
		profileComponent = component
		return MainHostComponent.Child.Profile(component = component)
	}

	private fun profileEditChild(componentContext: ComponentContext): MainHostComponent.Child {
		val profileEditRouter = object : ProfileEditRouter {
			override fun navigateBack() {
				navigation.pop()
				profileComponent?.handleRefresh()
			}
		}
		return MainHostComponent.Child.ProfileEdit(
			component = profileEditComponentFactory(
				componentContext = componentContext,
				profileEditRouter = profileEditRouter,
			),
		)
	}

	private fun reminderTimesChild(componentContext: ComponentContext): MainHostComponent.Child {
		val reminderTimesRouter = object : ReminderTimesRouter {
			override fun navigateBack() {
				navigation.pop()
				profileComponent?.handleRefresh()
			}
		}
		return MainHostComponent.Child.ReminderTimes(
			component = reminderTimesComponentFactory(
				componentContext = componentContext,
				reminderTimesRouter = reminderTimesRouter,
			),
		)
	}

	@OptIn(DelicateDecomposeApi::class)
	private fun materialsChild(componentContext: ComponentContext): MainHostComponent.Child =
		MainHostComponent.Child.Materials(
			component = materialsComponentFactory(
				componentContext = componentContext,
				onMaterialClick = { material ->
					navigation.push(MainHostConfig.MaterialDetail(materialId = material.id))
				},
			),
		)

	private fun materialDetailChild(
		materialId: String,
		componentContext: ComponentContext,
	): MainHostComponent.Child {
		val materialDetailRouter = object : MaterialDetailRouter {
			override fun navigateBack() {
				navigation.pop()
			}
		}
		return MainHostComponent.Child.MaterialDetail(
			component = materialDetailComponentFactory(
				componentContext = componentContext,
				materialId = materialId,
				materialDetailRouter = materialDetailRouter,
			),
		)
	}

	@OptIn(DelicateDecomposeApi::class)
	private fun homeChild(componentContext: ComponentContext): MainHostComponent.Child {
		val homeRouter = object : HomeRouter {
			override fun navigateToReview() {
				navigation.push(MainHostConfig.Review)
			}

			override fun navigateToCards() {
				navigation.push(MainHostConfig.Cards)
			}

			override fun navigateToConstructor() {
				navigation.push(MainHostConfig.Constructor)
			}

			override fun navigateToBooks() {
				navigation.push(MainHostConfig.Books)
			}
		}
		return MainHostComponent.Child.Home(
			component = homeComponentFactory(
				componentContext = componentContext,
				homeRouter = homeRouter,
			),
		)
	}

	private fun reviewChild(componentContext: ComponentContext): MainHostComponent.Child {
		val reviewRouter = object : ReviewRouter {
			override fun navigateBack() {
				navigation.pop()
			}
		}
		return MainHostComponent.Child.Review(
			component = reviewComponentFactory(
				componentContext = componentContext,
				reviewRouter = reviewRouter,
			),
		)
	}

	@OptIn(DelicateDecomposeApi::class)
	private fun cardsChild(componentContext: ComponentContext): MainHostComponent.Child {
		val cardsRouter = object : CardsRouter {
			override fun navigateBack() {
				navigation.pop()
			}
		}
		return MainHostComponent.Child.Cards(
			component = cardsComponentFactory(
				componentContext = componentContext,
				cardsRouter = cardsRouter,
				onCardClick = { item ->
					navigation.push(MainHostConfig.CardPractice(cardId = item.id))
				},
			),
		)
	}

	private fun cardPracticeChild(
		cardId: String,
		componentContext: ComponentContext,
	): MainHostComponent.Child {
		val cardPracticeRouter = object : CardPracticeRouter {
			override fun navigateBack() {
				navigation.pop()
			}
		}
		return MainHostComponent.Child.CardPractice(
			component = cardPracticeComponentFactory(
				componentContext = componentContext,
				cardId = cardId,
				cardPracticeRouter = cardPracticeRouter,
			),
		)
	}

	@OptIn(DelicateDecomposeApi::class)
	private fun constructorChild(componentContext: ComponentContext): MainHostComponent.Child {
		val constructorRouter = object : ConstructorRouter {
			override fun navigateBack() {
				navigation.pop()
			}
		}
		return MainHostComponent.Child.Constructor(
			component = constructorComponentFactory(
				componentContext = componentContext,
				constructorRouter = constructorRouter,
				onThemeClick = { theme ->
					navigation.push(MainHostConfig.ConstructorPractice(themeId = theme.id))
				},
			),
		)
	}

	private fun constructorPracticeChild(
		themeId: String,
		componentContext: ComponentContext,
	): MainHostComponent.Child {
		val constructorPracticeRouter = object : ConstructorPracticeRouter {
			override fun navigateBack() {
				navigation.pop()
			}
		}
		return MainHostComponent.Child.ConstructorPractice(
			component = constructorPracticeComponentFactory(
				componentContext = componentContext,
				themeId = themeId,
				constructorPracticeRouter = constructorPracticeRouter,
			),
		)
	}
}

@Serializable
private sealed interface MainHostConfig {

	@Serializable
	data object Home : MainHostConfig

	@Serializable
	data object Words : MainHostConfig

	@Serializable
	data object Materials : MainHostConfig

	@Serializable
	data class MaterialDetail(
		val materialId: String,
	) : MainHostConfig

	@Serializable
	data object Profile : MainHostConfig

	@Serializable
	data object ProfileEdit : MainHostConfig

	@Serializable
	data object ReminderTimes : MainHostConfig

	@Serializable
	data object Review : MainHostConfig

	@Serializable
	data object Cards : MainHostConfig

	@Serializable
	data class CardPractice(
		val cardId: String,
	) : MainHostConfig

	@Serializable
	data object Constructor : MainHostConfig

	@Serializable
	data class ConstructorPractice(
		val themeId: String,
	) : MainHostConfig

	@Serializable
	data object Books : MainHostConfig

	@Serializable
	data class BookReader(
		val bookId: String,
	) : MainHostConfig
}

private fun MainHostTab.toConfig(): MainHostConfig =
	when (this) {
		MainHostTab.Home -> MainHostConfig.Home
		MainHostTab.Words -> MainHostConfig.Words
		MainHostTab.Materials -> MainHostConfig.Materials
		MainHostTab.Profile -> MainHostConfig.Profile
	}

@Suppress("CyclomaticComplexMethod")
fun MainHostComponent.Child.toTab(): MainHostTab? =
	when (this) {
		is MainHostComponent.Child.Home -> MainHostTab.Home
		is MainHostComponent.Child.Words -> MainHostTab.Words
		is MainHostComponent.Child.Materials -> MainHostTab.Materials
		is MainHostComponent.Child.Profile -> MainHostTab.Profile
		is MainHostComponent.Child.ProfileEdit -> null
		is MainHostComponent.Child.ReminderTimes -> null
		is MainHostComponent.Child.MaterialDetail -> null
		is MainHostComponent.Child.Review -> null
		is MainHostComponent.Child.Cards -> null
		is MainHostComponent.Child.CardPractice -> null
		is MainHostComponent.Child.Constructor -> null
		is MainHostComponent.Child.ConstructorPractice -> null
		is MainHostComponent.Child.Books -> null
		is MainHostComponent.Child.BookReader -> null
	}