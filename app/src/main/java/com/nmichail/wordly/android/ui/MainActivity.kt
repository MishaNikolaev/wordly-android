package com.nmichail.wordly.android.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.nmichail.wordly.android.di.appComponent
import com.nmichail.wordly.android.mainhost.presentation.DefaultRootComponentFactory
import com.nmichail.wordly.android.mainhost.ui.RootContent
import javax.inject.Inject

class MainActivity : ComponentActivity() {

	@Inject
	lateinit var rootComponentFactory: DefaultRootComponentFactory

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()

		appComponent().inject(this)
		val rootComponent = rootComponentFactory(defaultComponentContext())

		setContent {
			RootContent(component = rootComponent)
		}
	}
}