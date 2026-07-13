package com.nmichail.wordly.android.core.network.di

import com.nmichail.wordly.android.core.network.api.AuthApi
import com.nmichail.wordly.android.core.network.data.repository.AuthRepositoryImpl
import com.nmichail.wordly.android.core.network.domain.repository.AuthRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class AuthModule {

	@Binds
	abstract fun bindAuthRepository(
		impl: AuthRepositoryImpl,
	): AuthRepository

	companion object {

		@Provides
		@Singleton
		fun provideAuthApi(
			@GeneralRetrofit retrofit: Retrofit,
		): AuthApi =
			retrofit.create(AuthApi::class.java)
	}
}
