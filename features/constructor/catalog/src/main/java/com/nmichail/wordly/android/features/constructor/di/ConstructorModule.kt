package com.nmichail.wordly.android.features.constructor.di

import com.nmichail.wordly.android.core.network.di.GeneralRetrofit
import com.nmichail.wordly.android.features.constructor.data.api.ConstructorApi
import com.nmichail.wordly.android.features.constructor.data.datasource.ConstructorDataSource
import com.nmichail.wordly.android.features.constructor.data.datasource.ConstructorDataSourceImpl
import com.nmichail.wordly.android.features.constructor.data.repository.ConstructorRepositoryImpl
import com.nmichail.wordly.android.features.constructor.domain.repository.ConstructorRepository
import com.nmichail.wordly.android.features.constructor.presentation.ConstructorComponent
import com.nmichail.wordly.android.features.constructor.presentation.DefaultConstructorComponentFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class ConstructorModule {

    @Binds
    abstract fun bindConstructorDataSource(
        impl: ConstructorDataSourceImpl,
    ): ConstructorDataSource

    @Binds
    abstract fun bindConstructorRepository(
        impl: ConstructorRepositoryImpl,
    ): ConstructorRepository

    @Binds
    internal abstract fun bindConstructorComponentFactory(
        impl: DefaultConstructorComponentFactory,
    ): ConstructorComponent.Factory

    companion object {

        @Provides
        @Singleton
        fun provideConstructorApi(
            @GeneralRetrofit retrofit: Retrofit,
        ): ConstructorApi =
            retrofit.create(ConstructorApi::class.java)
    }
}