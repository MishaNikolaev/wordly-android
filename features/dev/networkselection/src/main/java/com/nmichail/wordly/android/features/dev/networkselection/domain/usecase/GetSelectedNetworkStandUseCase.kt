package com.nmichail.wordly.android.features.dev.networkselection.domain.usecase

import com.nmichail.wordly.android.features.dev.networkselection.domain.entity.NetworkStand
import com.nmichail.wordly.android.features.dev.networkselection.domain.repository.NetworkStandRepository
import javax.inject.Inject

class GetSelectedNetworkStandUseCase @Inject constructor(
	networkStandRepository: NetworkStandRepository,
) : () -> NetworkStand by networkStandRepository::getSelected
