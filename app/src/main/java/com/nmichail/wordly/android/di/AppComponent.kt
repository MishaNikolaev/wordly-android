package com.nmichail.wordly.android.di

import android.app.Application
import com.nmichail.wordly.android.WordlyApplication
import com.nmichail.wordly.android.core.network.di.AuthModule
import com.nmichail.wordly.android.core.preferences.di.PreferencesModule
import com.nmichail.wordly.android.features.authorization.signin.di.SignInModule
import com.nmichail.wordly.android.features.authorization.signup.di.SignUpModule
import com.nmichail.wordly.android.features.dev.networkselection.di.NetworkSelectionModule
import com.nmichail.wordly.android.mainhost.di.MainHostModule
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
		AuthModule::class,
		SignInModule::class,
		SignUpModule::class,
		NetworkSelectionModule::class,
		ErrorModule::class,
		RoutersModule::class,
		ProcessRestarterModule::class,
		DevEnabledModule::class,
		MainHostModule::class,
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
