package com.nmichail.wordly.android.navigation.router

import com.nmichail.wordly.android.features.dev.networkselection.presentation.NetworkSelectionRouter
import com.nmichail.wordly.android.mainhost.presentation.ProcessRestarter
import javax.inject.Inject

class NetworkSelectionRouterImpl @Inject constructor(
	private val processRestarter: ProcessRestarter,
) : NetworkSelectionRouter {

	override fun restartApp() {
		processRestarter.restart()
	}
}
