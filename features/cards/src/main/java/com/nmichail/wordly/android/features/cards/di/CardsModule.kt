package com.nmichail.wordly.android.features.cards.di

import com.nmichail.wordly.android.core.network.di.GeneralRetrofit
import com.nmichail.wordly.android.features.cards.data.api.CardsApi
import com.nmichail.wordly.android.features.cards.data.repository.CardsRepositoryImpl
import com.nmichail.wordly.android.features.cards.domain.repository.CardsRepository
import com.nmichail.wordly.android.features.cards.presentation.CardsComponent
import com.nmichail.wordly.android.features.cards.presentation.DefaultCardsComponentFactory
import com.nmichail.wordly.android.features.cards.presentation.detail.CardPracticeComponent
import com.nmichail.wordly.android.features.cards.presentation.detail.DefaultCardPracticeComponentFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class CardsModule {

	@Binds
	abstract fun bindCardsRepository(
		impl: CardsRepositoryImpl,
	): CardsRepository

	@Binds
	internal abstract fun bindCardsComponentFactory(
		impl: DefaultCardsComponentFactory,
	): CardsComponent.Factory

	@Binds
	internal abstract fun bindCardPracticeComponentFactory(
		impl: DefaultCardPracticeComponentFactory,
	): CardPracticeComponent.Factory

	companion object {

		@Provides
		@Singleton
		fun provideCardsApi(
			@GeneralRetrofit retrofit: Retrofit,
		): CardsApi =
			retrofit.create(CardsApi::class.java)
	}
}
