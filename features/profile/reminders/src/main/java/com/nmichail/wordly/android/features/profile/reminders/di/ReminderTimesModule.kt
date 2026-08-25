package com.nmichail.wordly.android.features.profile.reminders.di

import com.nmichail.wordly.android.core.network.di.GeneralRetrofit
import com.nmichail.wordly.android.features.profile.reminders.data.api.ReminderTimesApi
import com.nmichail.wordly.android.features.profile.reminders.data.datasource.ReminderTimesDataSource
import com.nmichail.wordly.android.features.profile.reminders.data.datasource.ReminderTimesDataSourceImpl
import com.nmichail.wordly.android.features.profile.reminders.data.repository.ReminderTimesRepositoryImpl
import com.nmichail.wordly.android.features.profile.reminders.domain.repository.ReminderTimesRepository
import com.nmichail.wordly.android.features.profile.reminders.presentation.DefaultReminderTimesComponentFactory
import com.nmichail.wordly.android.features.profile.reminders.presentation.ReminderTimesComponent
import dagger.Binds
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
abstract class ReminderTimesModule {

	@Binds
	abstract fun bindReminderTimesDataSource(
		impl: ReminderTimesDataSourceImpl,
	): ReminderTimesDataSource

	@Binds
	abstract fun bindReminderTimesRepository(
		impl: ReminderTimesRepositoryImpl,
	): ReminderTimesRepository

	@Binds
	internal abstract fun bindReminderTimesComponentFactory(
		impl: DefaultReminderTimesComponentFactory,
	): ReminderTimesComponent.Factory

	companion object {

		@Provides
		@Singleton
		fun provideReminderTimesApi(
			@GeneralRetrofit retrofit: Retrofit,
		): ReminderTimesApi =
			retrofit.create(ReminderTimesApi::class.java)
	}
}
