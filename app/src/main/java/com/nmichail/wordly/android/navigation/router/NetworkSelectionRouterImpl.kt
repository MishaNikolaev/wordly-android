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

	override fun restartApp() {
		context.packageManager
			.getLaunchIntentForPackage(context.packageName)?.let {
				val restartIntent = makeRestartActivityTask(it.component)
				Handler(Looper.getMainLooper())
					.postDelayed(
						{
							context.startActivity(restartIntent)
							exitProcess(CODE_EXIT_PROCESS)
						},
						RELOAD_APP_TIME,
					)
			}
	}

	private companion object {

		private const val RELOAD_APP_TIME: Long = 500
		private const val CODE_EXIT_PROCESS = 0
	}
}
