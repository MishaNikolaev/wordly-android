package com.nmichail.wordly.android.di

import com.nmichail.wordly.android.BuildConfig
import com.nmichail.wordly.android.features.authorization.signin.presentation.DEV_ENABLED
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
object DevToolsModule {

	@Provides
	@Named(DEV_ENABLED)
	fun provideDevEnabled(): Boolean = BuildConfig.DEV_ENABLED
}
