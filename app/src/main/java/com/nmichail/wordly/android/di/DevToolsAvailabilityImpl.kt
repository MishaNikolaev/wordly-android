package com.nmichail.wordly.android.di

import com.nmichail.wordly.android.BuildConfig
import com.nmichail.wordly.android.features.authorization.signin.presentation.DevToolsAvailability
import javax.inject.Inject

class DevToolsAvailabilityImpl @Inject constructor() : DevToolsAvailability {

	override val isEnabled: Boolean = BuildConfig.DEV_TOOLS_ENABLED
}
