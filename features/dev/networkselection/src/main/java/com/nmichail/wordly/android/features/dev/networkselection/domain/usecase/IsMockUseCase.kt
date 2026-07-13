package com.nmichail.wordly.android.features.dev.networkselection.domain.usecase

import com.nmichail.wordly.android.features.dev.networkselection.domain.repository.MockRepository
import javax.inject.Inject

class IsMockUseCase @Inject constructor(
	repository: MockRepository,
) : () -> Boolean by repository::isMock
