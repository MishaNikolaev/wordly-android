package com.nmichail.wordly.android.features.review.di

import com.nmichail.wordly.android.features.review.presentation.DefaultReviewComponentFactory
import com.nmichail.wordly.android.features.review.presentation.ReviewComponent
import dagger.Binds
import dagger.Module

@Module
abstract class ReviewModule {

	@Binds
	internal abstract fun bindReviewComponentFactory(
		impl: DefaultReviewComponentFactory,
	): ReviewComponent.Factory
}
