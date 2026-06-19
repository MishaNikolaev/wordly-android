package com.nmichail.wordly.android.component.contract

sealed interface RootConfig : NavigationConfig {

	data object Authorization : RootConfig
}