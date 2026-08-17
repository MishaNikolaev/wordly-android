package com.nmichail.wordly.android.features.constructor.presentation

import com.arkivanov.decompose.ComponentContext
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorTheme
import javax.inject.Inject

internal class DefaultConstructorComponentFactory @Inject constructor(
    private val constructorStoreFactory: ConstructorStoreFactory,
) : ConstructorComponent.Factory {

    override fun invoke(
        componentContext: ComponentContext,
        constructorRouter: ConstructorRouter,
        onThemeClick: (ConstructorTheme) -> Unit,
    ): ConstructorComponent =
        DefaultConstructorComponent(
            componentContext = componentContext,
            constructorStoreFactory = constructorStoreFactory,
            constructorRouter = constructorRouter,
            onThemeClick = onThemeClick,
        )
}