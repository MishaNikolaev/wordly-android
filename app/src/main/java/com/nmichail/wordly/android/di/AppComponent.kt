package com.nmichail.wordly.android.di

import android.app.Application
import com.nmichail.wordly.android.WordlyApplication
import com.nmichail.wordly.android.core.preferences.di.PreferencesModule
import com.nmichail.wordly.android.shared.englishlevel.di.EnglishLevelModule
import com.nmichail.wordly.android.shared.error.di.ErrorModule
import com.nmichail.wordly.android.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
	modules = [
		AppModule::class,
		NetworkModule::class,
		PreferencesModule::class,
		EnglishLevelModule::class,
		FeaturesModule::class,
		ErrorModule::class,
		RoutersModule::class,
		DevEnabledModule::class,
	],
)
interface AppComponent {

	fun inject(application: WordlyApplication)

	fun inject(activity: MainActivity)

	@Component.Factory
	interface Factory {

		fun create(@BindsInstance application: Application): AppComponent
	}
}