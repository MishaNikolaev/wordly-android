package com.nmichail.wordly.android.di

import android.content.Context
import com.nmichail.wordly.android.mainhost.presentation.ProcessRestarter
import com.nmichail.wordly.android.util.AppRestarter
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object ProcessRestarterModule {

	@Provides
	@Singleton
	fun provideProcessRestarter(context: Context): ProcessRestarter =
		ProcessRestarter { AppRestarter.restart(context) }
}
