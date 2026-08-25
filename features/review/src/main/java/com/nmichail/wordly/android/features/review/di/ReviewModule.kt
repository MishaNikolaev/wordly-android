package com.nmichail.wordly.android.features.review.di

import com.nmichail.wordly.android.core.network.di.GeneralRetrofit
import com.nmichail.wordly.android.features.review.data.api.ReviewApi
import com.nmichail.wordly.android.features.review.data.repository.ReviewRepositoryImpl
import com.nmichail.wordly.android.features.review.domain.repository.ReviewRepository
import com.nmichail.wordly.android.features.review.presentation.DefaultReviewComponent
import com.nmichail.wordly.android.features.review.presentation.ReviewComponent
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class ReviewModule {

	@Binds
	abstract fun bindReviewRepository(
		impl: ReviewRepositoryImpl,
	): ReviewRepository

	@Binds
	internal abstract fun bindReviewComponentFactory(
		impl: DefaultReviewComponent.Factory,
	): ReviewComponent.Factory

	companion object {

		@Provides
		@Singleton
		fun provideReviewApi(
			@GeneralRetrofit retrofit: Retrofit,
		): ReviewApi =
			retrofit.create(ReviewApi::class.java)
	}
}