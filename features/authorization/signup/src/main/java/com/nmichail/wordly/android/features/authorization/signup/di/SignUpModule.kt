package com.nmichail.wordly.android.features.authorization.signup.di

import com.nmichail.wordly.android.core.network.di.GeneralRetrofit
import com.nmichail.wordly.android.features.authorization.signup.data.api.SignUpApi
import com.nmichail.wordly.android.features.authorization.signup.data.repository.SignUpRepositoryImpl
import com.nmichail.wordly.android.features.authorization.signup.domain.repository.SignUpRepository
import com.nmichail.wordly.android.features.authorization.signup.presentation.DefaultSignUpComponent
import com.nmichail.wordly.android.features.authorization.signup.presentation.SignUpComponent
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class SignUpModule {

	@Binds
	abstract fun bindSignUpRepository(
		impl: SignUpRepositoryImpl,
	): SignUpRepository

	@Binds
	internal abstract fun bindSignUpComponentFactory(
		impl: DefaultSignUpComponent.Factory,
	): SignUpComponent.Factory

	companion object {

		@Provides
		@Singleton
		fun provideSignUpApi(
			@GeneralRetrofit retrofit: Retrofit,
		): SignUpApi =
			retrofit.create(SignUpApi::class.java)
	}
}