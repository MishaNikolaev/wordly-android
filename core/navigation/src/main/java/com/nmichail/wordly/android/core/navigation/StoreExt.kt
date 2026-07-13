package com.nmichail.wordly.android.core.navigation

import com.arkivanov.decompose.Cancellation
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.rx.observer
import com.arkivanov.mvikotlin.core.store.Store

fun <State : Any> Store<*, State, *>.asValue(): Value<State> =
	object : Value<State>() {
		override val value: State get() = state

		override fun subscribe(observer: (State) -> Unit): Cancellation {
			val disposable = states(observer(onNext = observer))
			return Cancellation { disposable.dispose() }
		}
	}