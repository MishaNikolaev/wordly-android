package com.nmichail.wordly.android.features.profile.domain.usecase

import com.nmichail.wordly.android.core.firebase.data.datasource.FirebaseAuthDataSource
import com.nmichail.wordly.android.core.network.datasource.MockDataSource
import com.nmichail.wordly.android.core.preferences.domain.usecase.ClearAuthTokensUseCase
import com.nmichail.wordly.android.features.profile.domain.repository.ProfileRepository
import javax.inject.Inject
import kotlinx.coroutines.withTimeout

private const val LOGOUT_NETWORK_TIMEOUT_MS = 3_000L

class LogoutUseCase @Inject constructor(
	private val profileRepository: ProfileRepository,
	private val firebaseAuthDataSource: FirebaseAuthDataSource,
	private val clearAuthTokensUseCase: ClearAuthTokensUseCase,
	private val mockDataSource: MockDataSource,
) {

	suspend operator fun invoke() {
		if (!mockDataSource.isMock()) {
			try {
				withTimeout(LOGOUT_NETWORK_TIMEOUT_MS) {
					profileRepository.logout()
				}
			} catch (_: Exception) {
			}
			firebaseAuthDataSource.signOut()
		}
		clearAuthTokensUseCase()
	}
}
