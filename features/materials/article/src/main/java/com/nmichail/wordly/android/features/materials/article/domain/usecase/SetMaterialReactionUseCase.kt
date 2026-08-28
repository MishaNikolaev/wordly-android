package com.nmichail.wordly.android.features.materials.article.domain.usecase

import com.nmichail.wordly.android.features.materials.article.domain.entity.MaterialDetail
import com.nmichail.wordly.android.features.materials.article.domain.entity.MaterialReaction
import com.nmichail.wordly.android.features.materials.article.domain.repository.MaterialArticleRepository
import javax.inject.Inject

class SetMaterialReactionUseCase @Inject constructor(
	private val materialArticleRepository: MaterialArticleRepository,
) {
	suspend operator fun invoke(id: String, reaction: MaterialReaction?): MaterialDetail =
		materialArticleRepository.setReaction(id = id, reaction = reaction)
}

class GetMaterialReactionUseCase @Inject constructor(
	private val materialArticleRepository: MaterialArticleRepository,
) {
	suspend operator fun invoke(id: String): MaterialReaction? =
		materialArticleRepository.getReaction(id = id)
}
