package com.nmichail.wordly.android.di

import com.nmichail.wordly.android.features.authorization.signin.di.SignInModule
import com.nmichail.wordly.android.features.authorization.signup.di.SignUpModule
import com.nmichail.wordly.android.features.books.di.BooksModule
import com.nmichail.wordly.android.features.books.reader.di.BookReaderModule
import com.nmichail.wordly.android.features.cards.di.CardsModule
import com.nmichail.wordly.android.features.cards.training.di.CardPracticeModule
import com.nmichail.wordly.android.features.constructor.di.ConstructorModule
import com.nmichail.wordly.android.features.constructor.practice.di.ConstructorPracticeModule
import com.nmichail.wordly.android.features.dev.networkselection.di.NetworkSelectionModule
import com.nmichail.wordly.android.features.home.di.HomeModule
import com.nmichail.wordly.android.features.materials.di.MaterialsModule
import com.nmichail.wordly.android.features.materials.article.di.MaterialArticleModule
import com.nmichail.wordly.android.features.profile.di.ProfileModule
import com.nmichail.wordly.android.features.profile.editor.di.ProfileEditModule
import com.nmichail.wordly.android.features.profile.reminders.di.ReminderTimesModule
import com.nmichail.wordly.android.features.review.di.ReviewModule
import com.nmichail.wordly.android.features.words.di.WordsModule
import com.nmichail.wordly.android.mainhost.di.MainHostModule
import dagger.Module

@Module(
	includes = [
		SignInModule::class,
		SignUpModule::class,
		NetworkSelectionModule::class,
		HomeModule::class,
		ReviewModule::class,
		CardsModule::class,
		CardPracticeModule::class,
		ConstructorModule::class,
		ConstructorPracticeModule::class,
		BooksModule::class,
		BookReaderModule::class,
		WordsModule::class,
		MaterialsModule::class,
		MaterialArticleModule::class,
		ProfileModule::class,
		ProfileEditModule::class,
		ReminderTimesModule::class,
		MainHostModule::class,
	],
)
interface FeaturesModule