package com.nmichail.wordly.android.shared.error.presentation

interface ErrorLogoutRouter {

	fun navigateToLogoutScreen(userBlocked: Boolean)

	fun attach(navigateToSignIn: (userBlocked: Boolean) -> Unit)

	fun detach()
}
