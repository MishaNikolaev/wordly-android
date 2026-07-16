package com.nmichail.wordly.android.features.authorization.signin.di

import com.nmichail.wordly.android.core.network.di.GeneralRetrofit
import com.nmichail.wordly.android.features.authorization.signin.data.api.SignInApi
import com.nmichail.wordly.android.features.authorization.signin.data.repository.SignInRepositoryImpl
import com.nmichail.wordly.android.features.authorization.signin.domain.repository.SignInRepository
import com.nmichail.wordly.android.features.authorization.signin.presentation.DefaultSignInComponentFactory
import com.nmichail.wordly.android.features.authorization.signin.presentation.SignInComponent
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class SignInModule {

	@Binds
	abstract fun bindSignInRepository(
		impl: SignInRepositoryImpl,
	): SignInRepository

	@Binds
	internal abstract fun bindSignInComponentFactory(
		impl: DefaultSignInComponentFactory,
	): SignInComponent.Factory

	companion object {

		@Provides
		@Singleton
		fun provideSignInApi(
			@GeneralRetrofit retrofit: Retrofit,
		): SignInApi =
			retrofit.create(SignInApi::class.java)
	}
}
