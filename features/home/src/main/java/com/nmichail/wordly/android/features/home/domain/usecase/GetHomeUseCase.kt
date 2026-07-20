package com.nmichail.wordly.android.features.home.domain.usecase

import com.nmichail.wordly.android.features.home.domain.entity.Home
import com.nmichail.wordly.android.features.home.domain.repository.HomeRepository
import javax.inject.Inject

class GetHomeUseCase @Inject constructor(
	private val homeRepository: HomeRepository,
) {

	suspend operator fun invoke(): Home =
		homeRepository.getHome()
}
