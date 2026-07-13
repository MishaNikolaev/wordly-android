package com.nmichail.wordly.android.util

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import kotlin.system.exitProcess

private const val RESTART_DELAY_MS = 300L
private const val EXIT_CODE = 0

object AppRestarter {

	fun restart(context: Context) {
		val packageManager = context.packageManager
		val intent = packageManager.getLaunchIntentForPackage(context.packageName) ?: return
		val restartIntent = Intent.makeRestartActivityTask(intent.component)
		Handler(Looper.getMainLooper()).postDelayed({
			context.startActivity(restartIntent)
			exitProcess(EXIT_CODE)
		}, RESTART_DELAY_MS)
	}
}
