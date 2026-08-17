package com.nmichail.wordly.android.features.materials.article.domain.usecase

import com.nmichail.wordly.android.features.materials.article.domain.entity.MaterialDetail
import com.nmichail.wordly.android.features.materials.article.domain.repository.MaterialArticleRepository
import javax.inject.Inject

class GetMaterialUseCase @Inject constructor(
	materialArticleRepository: MaterialArticleRepository,
) : suspend (String) -> MaterialDetail by materialArticleRepository::getMaterial