package com.nmichail.wordly.android.features.dev.networkselection.domain.usecase

import com.nmichail.wordly.android.core.network.domain.entity.Endpoint
import com.nmichail.wordly.android.features.dev.networkselection.domain.repository.EndpointRepository
import javax.inject.Inject

class SetEndpointUseCase @Inject constructor(
	repository: EndpointRepository,
) : (Endpoint) -> Unit by repository::setEndpoint
