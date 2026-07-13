package com.nmichail.wordly.android.component.presentation

import com.arkivanov.decompose.ComponentContext
import com.nmichail.wordly.android.core.navigation.componentScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun CoroutineScope.launchTry(
	tryBlock: suspend CoroutineScope.() -> Unit,
): LaunchBuilder = LaunchBuilder(tryBlock, this)

fun ComponentContext.launchTry(
	tryBlock: suspend CoroutineScope.() -> Unit,
): LaunchBuilder = componentScope().launchTry(tryBlock)

class LaunchBuilder internal constructor(
	internal val tryBlock: suspend CoroutineScope.() -> Unit,
	private val scope: CoroutineScope,
) {

	infix fun catch(catchBlock: (Exception) -> Unit): Job {
		val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
			val exception = throwable as? Exception ?: throw throwable
			catchBlock(exception)
		}
		return scope.launch(exceptionHandler) {
			tryBlock()
		}
	}
}