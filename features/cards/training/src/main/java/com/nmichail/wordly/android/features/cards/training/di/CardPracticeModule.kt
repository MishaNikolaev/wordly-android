package com.nmichail.wordly.android.features.cards.training.di

import com.nmichail.wordly.android.core.network.di.GeneralRetrofit
import com.nmichail.wordly.android.features.cards.training.data.api.CardPracticeApi
import com.nmichail.wordly.android.features.cards.training.data.repository.CardPracticeRepositoryImpl
import com.nmichail.wordly.android.features.cards.training.domain.repository.CardPracticeRepository
import com.nmichail.wordly.android.features.cards.training.presentation.CardPracticeComponent
import com.nmichail.wordly.android.features.cards.training.presentation.DefaultCardPracticeComponentFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class CardPracticeModule {

	@Binds
	abstract fun bindCardPracticeRepository(
		impl: CardPracticeRepositoryImpl,
	): CardPracticeRepository

	@Binds
	internal abstract fun bindCardPracticeComponentFactory(
		impl: DefaultCardPracticeComponentFactory,
	): CardPracticeComponent.Factory

	companion object {

		@Provides
		@Singleton
		fun provideCardPracticeApi(
			@GeneralRetrofit retrofit: Retrofit,
		): CardPracticeApi =
			retrofit.create(CardPracticeApi::class.java)
	}
}
