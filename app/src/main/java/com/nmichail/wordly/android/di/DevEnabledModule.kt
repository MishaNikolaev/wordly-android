package com.nmichail.wordly.android.di

import com.nmichail.wordly.android.BuildConfig
import com.nmichail.wordly.android.features.authorization.signin.di.DevEnabled
import dagger.Module
import dagger.Provides

@Module
object DevEnabledModule {

	@Provides
	@DevEnabled
	fun provideDevEnabled(): Boolean = BuildConfig.DEV_ENABLED
}
