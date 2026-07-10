package com.nmichail.wordly.android.features.authorization.signin.di

import com.nmichail.wordly.android.features.authorization.signin.presentation.DefaultSignInComponentFactory
import com.nmichail.wordly.android.features.authorization.signin.presentation.SignInComponent
import dagger.Binds
import dagger.Module

@Module
abstract class SignInModule {

	@Binds
	abstract fun bindSignInComponentFactory(
		impl: DefaultSignInComponentFactory,
	): SignInComponent.Factory
}