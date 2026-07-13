package com.nmichail.wordly.android.shared.error.di

import com.nmichail.wordly.android.shared.error.presentation.ErrorDelegate
import com.nmichail.wordly.android.shared.error.presentation.ErrorDelegateImpl
import dagger.Binds
import dagger.Module

@Module
abstract class ErrorModule {

	@Binds
	abstract fun bindErrorDelegate(
		impl: ErrorDelegateImpl,
	): ErrorDelegate
}
