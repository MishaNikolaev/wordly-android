package com.nmichail.wordly.android.di

import com.nmichail.wordly.android.BuildConfig
import com.nmichail.wordly.android.features.authorization.signin.di.DevEnabled
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DevEnabledModule {

	@Provides
	@Singleton
	@DevEnabled
	fun provideDevEnabled(): Boolean = BuildConfig.DEV_ENABLED
}
