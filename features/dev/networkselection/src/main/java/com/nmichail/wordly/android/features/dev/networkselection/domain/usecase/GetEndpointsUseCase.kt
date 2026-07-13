package com.nmichail.wordly.android.features.dev.networkselection.domain.usecase

import com.nmichail.wordly.android.core.network.domain.entity.Endpoint
import com.nmichail.wordly.android.features.dev.networkselection.domain.repository.EndpointRepository
import javax.inject.Inject

class GetEndpointsUseCase @Inject constructor(
	repository: EndpointRepository,
) : () -> List<Endpoint> by repository::getEndpoints
