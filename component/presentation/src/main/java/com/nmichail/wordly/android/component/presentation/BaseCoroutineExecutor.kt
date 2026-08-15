package com.nmichail.wordly.android.component.presentation

import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

abstract class BaseCoroutineExecutor<
	in Intent : Any,
	Action : Any,
	State : Any,
	Message : Any,
	Label : Any,
>(
	componentContext: CoroutineContext = Dispatchers.Main.immediate + SupervisorJob(),
) : CoroutineExecutor<Intent, Action, State, Message, Label>(componentContext)