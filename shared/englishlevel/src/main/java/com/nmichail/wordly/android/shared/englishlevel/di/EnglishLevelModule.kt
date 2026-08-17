package com.nmichail.wordly.android.shared.englishlevel.di

import com.nmichail.wordly.android.core.network.di.GeneralRetrofit
import com.nmichail.wordly.android.shared.englishlevel.data.api.EnglishLevelApi
import com.nmichail.wordly.android.shared.englishlevel.data.repository.EnglishLevelRepositoryImpl
import com.nmichail.wordly.android.shared.englishlevel.domain.repository.EnglishLevelRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class EnglishLevelModule {

    @Binds
    abstract fun bindEnglishLevelRepository(
        impl: EnglishLevelRepositoryImpl,
    ): EnglishLevelRepository

    companion object {

        @Provides
        @Singleton
        fun provideEnglishLevelApi(
            @GeneralRetrofit retrofit: Retrofit,
        ): EnglishLevelApi =
            retrofit.create(EnglishLevelApi::class.java)
    }
}