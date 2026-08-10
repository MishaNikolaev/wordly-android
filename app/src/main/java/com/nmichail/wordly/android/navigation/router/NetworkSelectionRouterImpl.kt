package com.nmichail.wordly.android.navigation.router

import android.content.Context
import android.content.Intent.makeRestartActivityTask
import android.os.Handler
import android.os.Looper
import com.nmichail.wordly.android.features.dev.networkselection.presentation.NetworkSelectionRouter
import javax.inject.Inject
import kotlin.system.exitProcess

class NetworkSelectionRouterImpl @Inject constructor(
	private val context: Context,
) : NetworkSelectionRouter {

	private companion object {
		const val RESTART_DELAY_MS = 500L
		const val EXIT_CODE = 0
	}

	override fun restartApp() {
		context.packageManager
			.getLaunchIntentForPackage(context.packageName)
			?.let { launchIntent ->
				val restartIntent = makeRestartActivityTask(launchIntent.component)
				Handler(Looper.getMainLooper()).postDelayed(
					{
						context.startActivity(restartIntent)
						exitProcess(EXIT_CODE)
					},
					RESTART_DELAY_MS,
				)
			}
	}
}