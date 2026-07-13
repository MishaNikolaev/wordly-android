package com.nmichail.wordly.android.navigation.router

import com.nmichail.wordly.android.shared.error.presentation.ErrorLogoutRouter
import java.util.concurrent.atomic.AtomicReference
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorLogoutRouterImpl @Inject constructor() : ErrorLogoutRouter {

	private val navigateToSignIn = AtomicReference<((Boolean) -> Unit)?>(null)

	override fun attach(navigateToSignIn: (userBlocked: Boolean) -> Unit) {
		this.navigateToSignIn.set(navigateToSignIn)
	}

	override fun detach() {
		navigateToSignIn.set(null)
	}

	override fun navigateToLogoutScreen(userBlocked: Boolean) {
		navigateToSignIn.get()?.invoke(userBlocked)
	}
}
