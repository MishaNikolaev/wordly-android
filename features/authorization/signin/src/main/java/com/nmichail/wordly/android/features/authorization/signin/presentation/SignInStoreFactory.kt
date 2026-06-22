package com.nmichail.wordly.android.features.authorization.signin.presentation

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.arkivanov.mvikotlin.logging.store.LoggingStoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory

class SignInStoreFactory {

	private val storeFactory: StoreFactory = LoggingStoreFactory(DefaultStoreFactory())

	fun create(): SignInStore = object : SignInStore,
				 Store<SignInStore.Intent, SignInStore.State, SignInStore.Label> by storeFactory.create(
					 name = "SignInStore",
					 initialState = SignInStore.State(
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

	private object ReducerImpl : Reducer<SignInStore.State, Msg> {

		override fun SignInStore.State.reduce(msg: Msg): SignInStore.State =
			when (msg) {
				is Msg.ChangeEmail    -> copy(email = msg.email)
				is Msg.ChangePassword -> copy(password = msg.password)
			}
	}

	private class ExecutorImpl :
		CoroutineExecutor<SignInStore.Intent, Nothing, SignInStore.State, Msg, SignInStore.Label>() {

		override fun executeIntent(intent: SignInStore.Intent) {
			when (intent) {
				is SignInStore.Intent.ChangeEmail    -> dispatch(Msg.ChangeEmail(email = intent.email))
				is SignInStore.Intent.ChangePassword -> dispatch(Msg.ChangePassword(password = intent.password))
				SignInStore.Intent.Submit            -> Unit
				SignInStore.Intent.NavigateToSignUp  -> publish(SignInStore.Label.OpenSignUp)
			}
		}
	}
}