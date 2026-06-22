package com.nmichail.wordly.android.di

import android.content.Context

interface AppComponentProvider {

	val appComponent: AppComponent
}

fun Context.appComponent(): AppComponent =
	(applicationContext as AppComponentProvider).appComponent