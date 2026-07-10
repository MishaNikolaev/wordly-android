package com.nmichail.wordly.android.di

import android.app.Application
import com.nmichail.wordly.android.WordlyApplication
import com.nmichail.wordly.android.features.authorization.signin.di.SignInModule
import com.nmichail.wordly.android.features.authorization.signup.di.SignUpModule
import com.nmichail.wordly.android.mainhost.di.MainHostModule
import com.nmichail.wordly.android.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
	modules = [
		AppModule::class,
		SignInModule::class,
		SignUpModule::class,
		MainHostModule::class
	]
)
interface AppComponent {

	fun inject(application: WordlyApplication)

	fun inject(activity: MainActivity)

	@Component.Factory
	interface Factory {

		fun create(@BindsInstance application: Application): AppComponent
	}
}