package com.nmichail.wordly.android.di

import com.nmichail.wordly.android.features.authorization.signin.di.SignInModule
import com.nmichail.wordly.android.features.authorization.signup.di.SignUpModule
import com.nmichail.wordly.android.features.books.di.BooksModule
import com.nmichail.wordly.android.features.cards.di.CardsModule
import com.nmichail.wordly.android.features.constructor.di.ConstructorModule
import com.nmichail.wordly.android.features.dev.networkselection.di.NetworkSelectionModule
import com.nmichail.wordly.android.features.home.di.HomeModule
import com.nmichail.wordly.android.features.news.di.NewsModule
import com.nmichail.wordly.android.features.review.di.ReviewModule
import com.nmichail.wordly.android.mainhost.di.MainHostModule
import dagger.Module

@Module(
	includes = [
		SignInModule::class,
		SignUpModule::class,
		NetworkSelectionModule::class,
		HomeModule::class,
		NewsModule::class,
		ReviewModule::class,
		CardsModule::class,
		ConstructorModule::class,
		BooksModule::class,
		MainHostModule::class,
	],
)
interface FeaturesModule