package com.nmichail.wordly.android.features.constructor.presentation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import com.nmichail.wordly.android.features.constructor.domain.entity.ConstructorTheme

interface ConstructorComponent {

    val model: Value<ConstructorStore.State>

    fun handleBack()

    fun handleRetry()

    fun handleSearchQueryChange(query: String)

    fun handleLevelChange(level: String)

    fun handleThemeClick(themeId: String)

    fun interface Factory {

        operator fun invoke(
            componentContext: ComponentContext,
            constructorRouter: ConstructorRouter,
            onThemeClick: (ConstructorTheme) -> Unit,
        ): ConstructorComponent
    }
}