package com.nmichail.wordly.android.features.authorization.signup.di

import com.nmichail.wordly.android.features.authorization.signup.presentation.DefaultSignUpComponentFactory
import com.nmichail.wordly.android.features.authorization.signup.presentation.SignUpComponent
import dagger.Binds
import dagger.Module

@Module
abstract class SignUpModule {

	@Binds
	abstract fun bindSignUpComponentFactory(
		impl: DefaultSignUpComponentFactory,
	): SignUpComponent.Factory
}