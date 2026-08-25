package com.nmichail.wordly.android

import android.app.Application
import com.nmichail.wordly.android.core.preferences.domain.repository.ThemeRepository
import com.nmichail.wordly.android.di.AppComponent
import com.nmichail.wordly.android.di.AppComponentProvider
import com.nmichail.wordly.android.di.DaggerAppComponent
import javax.inject.Inject

class WordlyApplication : Application(), AppComponentProvider {

	override lateinit var appComponent: AppComponent
		private set

	@Inject
	lateinit var themeRepository: ThemeRepository

	override fun onCreate() {
		super.onCreate()
		appComponent = DaggerAppComponent.factory().create(application = this)
		appComponent.inject(this)
		// Touch repository so night mode is applied before any Activity is created.
		themeRepository.getThemeMode()
	}
}