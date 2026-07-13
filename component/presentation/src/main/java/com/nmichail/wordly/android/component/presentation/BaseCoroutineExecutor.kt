package com.nmichail.wordly.android.component.presentation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.CoroutineScope

abstract class BaseCoroutineExecutor<
	in Intent : Any,
	Action : Any,
	State : Any,
	Message : Any,
	Label : Any,
> : CoroutineExecutor<Intent, Action, State, Message, Label>() {

	protected fun launchTry(
		tryBlock: suspend CoroutineScope.() -> Unit,
	): LaunchBuilder = scope.launchTry(tryBlock)
}