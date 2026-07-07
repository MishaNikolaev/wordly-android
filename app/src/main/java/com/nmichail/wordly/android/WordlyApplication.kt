package com.nmichail.wordly.android

import android.app.Application
import com.nmichail.wordly.android.di.AppComponent
import com.nmichail.wordly.android.di.AppComponentProvider
import com.nmichail.wordly.android.di.DaggerAppComponent

class WordlyApplication : Application(), AppComponentProvider {

	override lateinit var appComponent: AppComponent
		private set

	override fun onCreate() {
		super.onCreate()
		appComponent = DaggerAppComponent.factory().create(application = this)
		appComponent.inject(this)
	}
}