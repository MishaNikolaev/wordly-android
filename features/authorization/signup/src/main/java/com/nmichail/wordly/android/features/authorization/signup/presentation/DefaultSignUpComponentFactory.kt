package com.nmichail.wordly.android.features.authorization.signup.presentation

import com.arkivanov.decompose.ComponentContext
import javax.inject.Inject

internal class DefaultSignUpComponentFactory @Inject constructor(
    private val signUpStoreFactory: SignUpStoreFactory,
) : SignUpComponent.Factory {

    override fun invoke(
        componentContext: ComponentContext,
        signUpRouter: SignUpRouter,
    ): SignUpComponent =
        DefaultSignUpComponent(
            componentContext = componentContext,
            signUpStoreFactory = signUpStoreFactory,
            signUpRouter = signUpRouter,
        )
}