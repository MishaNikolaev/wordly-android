package com.nmichail.wordly.android.mainhost.presentation

import com.arkivanov.decompose.ComponentContext
import javax.inject.Inject

internal class DefaultMainHostComponentFactory @Inject constructor() : MainHostComponent.Factory {

	override fun invoke(componentContext: ComponentContext): MainHostComponent =
		DefaultMainHostComponent(componentContext = componentContext)
}
