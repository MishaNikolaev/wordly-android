package com.nmichail.wordly.android.core.testutils

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import com.arkivanov.essenty.lifecycle.resume

fun createTestComponentContext(): ComponentContext {
	val lifecycle = LifecycleRegistry()
	val componentContext = DefaultComponentContext(lifecycle = lifecycle)
	lifecycle.resume()
	return componentContext
}

fun createTestLifecycle(): LifecycleRegistry {
	val lifecycle = LifecycleRegistry()
	lifecycle.resume()
	return lifecycle
}