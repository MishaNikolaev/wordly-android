package com.nmichail.wordly.android.features.constructor.practice.di

import com.nmichail.wordly.android.core.network.di.GeneralRetrofit
import com.nmichail.wordly.android.features.constructor.practice.data.api.ConstructorPracticeApi
import com.nmichail.wordly.android.features.constructor.practice.data.repository.ConstructorPracticeRepositoryImpl
import com.nmichail.wordly.android.features.constructor.practice.domain.repository.ConstructorPracticeRepository
import com.nmichail.wordly.android.features.constructor.practice.presentation.ConstructorPracticeComponent
import com.nmichail.wordly.android.features.constructor.practice.presentation.DefaultConstructorPracticeComponentFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class ConstructorPracticeModule {

    @Binds
    abstract fun bindConstructorPracticeRepository(
        impl: ConstructorPracticeRepositoryImpl,
    ): ConstructorPracticeRepository

    @Binds
    internal abstract fun bindConstructorPracticeComponentFactory(
        impl: DefaultConstructorPracticeComponentFactory,
    ): ConstructorPracticeComponent.Factory

    companion object {

        @Provides
        @Singleton
        fun provideConstructorPracticeApi(
            @GeneralRetrofit retrofit: Retrofit,
        ): ConstructorPracticeApi =
            retrofit.create(ConstructorPracticeApi::class.java)
    }
}