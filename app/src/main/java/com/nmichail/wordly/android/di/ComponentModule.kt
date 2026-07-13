package com.nmichail.wordly.android.di

import com.nmichail.wordly.android.features.authorization.signin.presentation.DefaultSignInComponentFactory
import com.nmichail.wordly.android.features.authorization.signin.presentation.SignInComponent
import com.nmichail.wordly.android.features.authorization.signup.presentation.DefaultSignUpComponentFactory
import com.nmichail.wordly.android.features.authorization.signup.presentation.SignUpComponent
import com.nmichail.wordly.android.mainhost.presentation.DefaultRootComponentFactory
import com.nmichail.wordly.android.mainhost.presentation.RootComponent
import dagger.Binds
import dagger.Module

@Module
abstract class ComponentModule {

	@Binds
	abstract fun bindSignInComponentFactory(
		impl: DefaultSignInComponentFactory,
	): SignInComponent.Factory

	@Binds
	abstract fun bindSignUpComponentFactory(
		impl: DefaultSignUpComponentFactory,
	): SignUpComponent.Factory

	@Binds
	abstract fun bindRootComponentFactory(
		impl: DefaultRootComponentFactory,
	): RootComponent.Factory
}
