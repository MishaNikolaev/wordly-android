package com.nmichail.wordly.android.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.nmichail.wordly.android.mainhost.presentation.DefaultRootComponentFactory
import com.nmichail.wordly.android.mainhost.ui.RootContent

class MainActivity : ComponentActivity() {

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()

		val rootComponent = DefaultRootComponentFactory()(defaultComponentContext())

		setContent {
			RootContent(component = rootComponent)
		}
	}
}
