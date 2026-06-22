package com.nmichail.wordly.android.features.authorization.signup.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory

class SignUpStoreFactory {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): SignUpStore = object : SignUpStore,
				 Store<SignUpStore.Intent, SignUpStore.State, SignUpStore.Label> by storeFactory.create(
					 name = "SignUpStore",
					 initialState = SignUpStore.State(
						 email = "",
						 password = "",
					 ),
					 executorFactory = ::ExecutorImpl,
					 reducer = ReducerImpl,
				 ) {}

	private sealed interface Msg {

		data class ChangeEmail(val email: String) : Msg

		data class ChangePassword(val password: String) : Msg
	}

	private object ReducerImpl : Reducer<SignUpStore.State, Msg> {

		override fun SignUpStore.State.reduce(msg: Msg): SignUpStore.State =
			when (msg) {
				is Msg.ChangeEmail    -> copy(email = msg.email)
				is Msg.ChangePassword -> copy(password = msg.password)
			}
	}

	private class ExecutorImpl :
		CoroutineExecutor<SignUpStore.Intent, Nothing, SignUpStore.State, Msg, SignUpStore.Label>() {

		override fun executeIntent(intent: SignUpStore.Intent) {
			when (intent) {
				is SignUpStore.Intent.ChangeEmail    -> dispatch(Msg.ChangeEmail(email = intent.email))
				is SignUpStore.Intent.ChangePassword -> dispatch(Msg.ChangePassword(password = intent.password))
				SignUpStore.Intent.Submit            -> Unit
				SignUpStore.Intent.NavigateToSignIn  -> publish(SignUpStore.Label.OpenSignIn)
			}
		}
	}
}
